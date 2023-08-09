package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping(path = "/Duel")
public class DuelController {

    private Map<String, CardDuel> duels = new HashMap<>();


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping(path = "getHandCards/{userName}/{gameID}")
    @CrossOrigin
    public List<CardDisplay> getHand(@PathVariable String userName, @PathVariable String gameID){
        return duels.get(gameID).getHandOf(userName);
    }

    @GetMapping(path = "getDeckCards/{userName}/{gameID}")
    @CrossOrigin
    public List<CardDisplay> getDeck(@PathVariable String userName, @PathVariable String gameID) {
        return duels.get(gameID).getDeckOf(userName);
    }

    @GetMapping(path = "getGraveyardCards/{userName}/{gameID}")
    @CrossOrigin
    public List<CardDisplay> getGraveyard(@PathVariable String userName, @PathVariable String gameID) {
        return duels.get(gameID).getGraveyardOf(userName);
    }

    @GetMapping(path = "getCardsOnRow/{userName}/{rowNumber}/{gameID}")
    @CrossOrigin
    public List<CardDisplay> getRow(@PathVariable String userName, @PathVariable int rowNumber, @PathVariable String gameID){
        return duels.get(gameID).getRowOf(userName, rowNumber);
    }


    @GetMapping(path = "getRowsPoints/{userName}/{gameID}")
    @CrossOrigin
    public List<Integer> getRowsPoints(@PathVariable String userName, @PathVariable String gameID){
        return IntStream.range(0, Consts.rowsNumber).mapToObj(i -> duels.get(gameID).getRowPointsOf(userName, i)).collect(Collectors.toList());
    }


    @GetMapping(path = "getWonRounds/{userName}/{gameID}")
    @CrossOrigin
    public int getWonRounds(@PathVariable String userName, @PathVariable String gameID){
        return duels.get(gameID).getWonRoundsOf(userName);
    }


    @PostMapping(path = "playCard")
    @CrossOrigin
    public CardDisplay playCard(@RequestBody List<CardDisplay> specificCards, @RequestParam String userName, @RequestParam int affectedRow,@RequestParam int rowNumber,
                         @RequestParam String gameID){
        int cardPlayedIndex = 0;
        int cardTargetedIndex = 1;
        if(specificCards.get(cardPlayedIndex).getName().isEmpty() == false)
            return duels.get(gameID).playCardAs(new PlayerPlay(specificCards.get(cardPlayedIndex), rowNumber, specificCards.get(cardTargetedIndex), affectedRow), userName);
        return new CardDisplay();
    }

    @PostMapping(path = "endRound/{userName}/{gameID}")
    @CrossOrigin
    public void endRound(@PathVariable String userName, @PathVariable String gameID){
        if(duels.get(gameID).didEnemyEndedRound(userName)) {
            simpMessagingTemplate.convertAndSendToUser(duels.get(gameID).getOpponentOf(userName), "/newRoundStarted", "New round started");
            simpMessagingTemplate.convertAndSendToUser(userName, "/newRoundStarted", "New round started");
        }
        else {
            simpMessagingTemplate.convertAndSendToUser(duels.get(gameID).getOpponentOf(userName), "/enemyEndRound", "Enemy ended round");
        }
        duels.get(gameID).endRoundFor(userName);
    }

    @GetMapping(path = "isTurnOf/{userName}/{gameID}")
    @CrossOrigin
    public boolean isTurnOf(@PathVariable String userName, @PathVariable String gameID){
        return duels.get(gameID).isTurnOf(userName);
    }

    @GetMapping(path = "didWon/{userName}/{gameID}")
    @CrossOrigin
    public boolean didWon(@PathVariable String userName, @PathVariable String gameID){
        return duels.get(gameID).didWon(userName);
    }
    @PostMapping(path = "getPossibleRowsToAffect/{gameID}")
    @CrossOrigin
    public List<Integer> getPossibleRowsToAffect(@RequestBody CardDisplay cardPlayed, @PathVariable String gameID){
        return duels.get(gameID).getPossibleRowsToAffect(cardPlayed);
    }

    @PostMapping(path = "getPossibleTargets/{userName}/{gameID}")
    @CrossOrigin
    public List<CardDisplay> getTargetableCards(@PathVariable String userName, @PathVariable String gameID, @RequestBody CardDisplay cardPlayed){
        return duels.get(gameID).getPossibleTargetsOf(cardPlayed, userName);
    }


    @PostMapping(path="registerUser/{userName}/{gameID}")
    @CrossOrigin
    public void registerUser(@RequestBody List<CardDisplay> deck, @PathVariable String userName, @PathVariable String gameID) {
        if(duels.containsKey(gameID) == false) {
            duels.put(gameID, CardDuel.createDuel());
            duels.get(gameID).registerPlayerToDuel(userName);
            duels.get(gameID).parseCardsFor(deck, userName);
        }
        else {
            duels.get(gameID).registerPlayerToDuel(userName);
            duels.get(gameID).parseCardsFor(deck, userName);
            duels.get(gameID).dealCards();
        }
    }
    @GetMapping(path="getEnemyOf/{userName}/{gameID}")
    @CrossOrigin
    public String getEnemyIf(@PathVariable String userName, @PathVariable String gameID) {
        return duels.get(gameID).getOpponentOf(userName);
    }

    @GetMapping(path="getRowsStatus/{userName}/{gameID}")
    @CrossOrigin
    public List<String> getRowsStatusNames(@PathVariable String userName, @PathVariable String gameID) {

        return IntStream.range(0, Consts.rowsNumber).mapToObj(i -> duels.get(gameID).getRowStatusOf(userName, i)).collect(Collectors.toList());
    }
    @GetMapping(path="getHandSize/{userName}/{gameID}")
    @CrossOrigin
    public int getEnemyHandSize(@PathVariable String userName, @PathVariable String gameID) {
        return duels.get(gameID).getHandOf(userName).size();
    }

    @PostMapping(path = "mulliganCard/{userName}/{gameID}")
    @CrossOrigin
    public void mulliganCard(@PathVariable String userName, @PathVariable String gameID, @RequestBody CardDisplay cardToMulligan) {
        duels.get(gameID).mulliganCardFor(cardToMulligan, userName);
    }


}
