package com.example.demo;

import com.example.demo.Cards.Card;
import com.example.demo.Cards.CardDisplay;
import com.example.demo.Duel.ClientAPI.CardDuel;
import com.example.demo.Duel.PlayerPlay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(path = "/Duel")
@CrossOrigin
public class DuelController {

    private Map<String, CardDuel> duels = new HashMap<>();


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping(path = "getHandCards/{userName}/{gameID}")
    public List<CardDisplay> getHand(@PathVariable String userName, @PathVariable String gameID){
        return duels.get(gameID).getHandOf(userName);
    }

    @GetMapping(path = "getDeckCards/{userName}/{gameID}")
    public List<CardDisplay> getDeck(@PathVariable String userName, @PathVariable String gameID) {
        return duels.get(gameID).getDeckOf(userName);
    }

    @GetMapping(path = "getGraveyardCards/{userName}/{gameID}")
    public List<CardDisplay> getGraveyard(@PathVariable String userName, @PathVariable String gameID) {
        return duels.get(gameID).getGraveyardOf(userName);
    }

    @GetMapping(path = "getCardsOnRow/{userName}/{rowNumber}/{gameID}")
    public List<CardDisplay> getRow(@PathVariable String userName, @PathVariable int rowNumber, @PathVariable String gameID){
        return duels.get(gameID).getRowOf(userName, rowNumber);
    }


    @GetMapping(path = "getRowsPoints/{userName}/{gameID}")
    public List<Integer> getRowsPoints(@PathVariable String userName, @PathVariable String gameID){
        return IntStream.range(0, Consts.rowsNumber).mapToObj(i -> duels.get(gameID).getRowPointsOf(userName, i)).collect(Collectors.toList());
    }


    @GetMapping(path = "getWonRounds/{userName}/{gameID}")
    public int getWonRounds(@PathVariable String userName, @PathVariable String gameID){
        return duels.get(gameID).getWonRoundsOf(userName);
    }


    @PostMapping(path = "playCard")
    public CardDisplay playCard(@RequestBody List<CardDisplay> specificCards, @RequestParam String userName, @RequestParam int affectedRow,@RequestParam int rowNumber,
                         @RequestParam String gameID){
        CardDisplay cardPlayed = specificCards.get(0);
        CardDisplay cardTargeted= specificCards.get(1);
        if(cardPlayed.getName().isEmpty() == false) {
            CardDisplay cardChain = duels.get(gameID).playCardAs(
                    new PlayerPlay(cardPlayed, rowNumber, cardTargeted, affectedRow), userName
            );
            String enemy = duels.get(gameID).getOpponentOf(userName);
            CardDisplay mirrorPlayerCard = duels.get(gameID).getHandOf(enemy).stream()
                    .filter(c -> c.getName().equals(cardPlayed.getName()))
                    .findFirst().orElse(new CardDisplay());
            duels.get(gameID).playCardAs(new PlayerPlay(mirrorPlayerCard, rowNumber, new CardDisplay(), affectedRow), enemy);
            return cardChain;
        }
        return new CardDisplay();
    }

    @PostMapping(path = "endRound/{userName}/{gameID}")
    public void endRound(@PathVariable String userName, @PathVariable String gameID){
        if(duels.get(gameID).didEnemyEndedRound(userName)) {
            simpMessagingTemplate.convertAndSendToUser(duels.get(gameID).getOpponentOf(userName), "/newRoundStarted", "New round started");
            simpMessagingTemplate.convertAndSendToUser(userName, "/newRoundStarted", "New round started");
        }
        else {
            simpMessagingTemplate.convertAndSendToUser(duels.get(gameID).getOpponentOf(userName), "/enemyEndRound", "Enemy ended round");
            simpMessagingTemplate.convertAndSendToUser(userName, "/playerEndedRound", "Player ended round");
        }
        duels.get(gameID).endRoundFor(userName);
    }

    @GetMapping(path = "isTurnOf/{userName}/{gameID}")
    public boolean isTurnOf(@PathVariable String userName, @PathVariable String gameID){
        return duels.get(gameID).isTurnOf(userName);
    }

    @GetMapping(path = "didWon/{userName}/{gameID}")
    public boolean didWon(@PathVariable String userName, @PathVariable String gameID){
        return duels.get(gameID).didWon(userName);
    }
    @PostMapping(path = "getPossibleRowsToAffect/{gameID}")
    public List<Integer> getPossibleRowsToAffect(@RequestBody CardDisplay cardPlayed, @PathVariable String gameID){
        return duels.get(gameID).getPossibleRowsToAffect(cardPlayed);
    }

    @PostMapping(path = "getPossibleTargets/{userName}/{gameID}")
    public List<CardDisplay> getTargetableCards(@PathVariable String userName, @PathVariable String gameID, @RequestBody CardDisplay cardPlayed){
        return duels.get(gameID).getPossibleTargetsOf(cardPlayed, userName);
    }


    @PostMapping(path="registerUser/{userName}/{gameID}/{deckFraction}")
    public void registerUser(@RequestBody List<CardDisplay> deck, @PathVariable String userName, @PathVariable String gameID, @PathVariable String deckFraction) {
        if(duels.containsKey(gameID) == false) {
            duels.put(gameID, CardDuel.createDuel());

            //Enemy bot
            String botName = userName + " bot";
            duels.get(gameID).registerPlayerToDuel(botName, deckFraction);
            duels.get(gameID).parseCardsFor(deck, botName);

            //Player
            duels.get(gameID).registerPlayerToDuel(userName, deckFraction);
            duels.get(gameID).parseCardsFor(deck, userName);

            duels.get(gameID).dealCards();
        }
        else {
            //Web socket communication diabled
//            duels.get(gameID).registerPlayerToDuel(userName,deckFraction);
//            duels.get(gameID).parseCardsFor(deck, userName);
//            duels.get(gameID).dealCards();
        }
    }
    @GetMapping(path="getEnemyOf/{userName}/{gameID}")
    public String getEnemyIf(@PathVariable String userName, @PathVariable String gameID) {
        return duels.get(gameID).getOpponentOf(userName);
    }

    @GetMapping(path="getRowsStatus/{userName}/{gameID}")
    public List<String> getRowsStatusNames(@PathVariable String userName, @PathVariable String gameID) {

        return IntStream.range(0, Consts.rowsNumber).mapToObj(i -> duels.get(gameID).getRowStatusOf(userName, i)).collect(Collectors.toList());
    }
    @GetMapping(path="getHandSize/{userName}/{gameID}")
    public int getEnemyHandSize(@PathVariable String userName, @PathVariable String gameID) {
        return duels.get(gameID).getHandOf(userName).size();
    }

    @PostMapping(path = "mulliganCard/{userName}/{gameID}")
    public void mulliganCard(@PathVariable String userName, @PathVariable String gameID, @RequestBody CardDisplay cardToMulligan) {
        duels.get(gameID).mulliganCardFor(cardToMulligan, userName);
    }


}
