package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/Duel")
public class DuelController {

    private Map<String, CardDuel> betterDuel = new HashMap<>();


    @GetMapping(path = "getHandCards/{userName}/{gameID}")
    @CrossOrigin
    public List<CardDisplay> getHand(@PathVariable String userName, @PathVariable String gameID){
        return betterDuel.get(gameID).getCardsInHandDisplayOf(userName);
    }

    @GetMapping(path = "getDeckCards/{userName}/{gameID}")
    @CrossOrigin
    public List<CardDisplay> getDeck(@PathVariable String userName, @PathVariable String gameID) {
        return betterDuel.get(gameID).getCardsInDeckDisplayOf(userName);
    }

    @GetMapping(path = "getCardsOnRow/{userName}/{rowNumber}/{gameID}")
    @CrossOrigin
    public List<CardDisplay> getRow(@PathVariable String userName, @PathVariable int rowNumber, @PathVariable String gameID){
        return betterDuel.get(gameID).getCardsOnBoardDisplayOf(userName, rowNumber);
    }


    @GetMapping(path = "getBoardPoints/{userName}/{gameID}")
    @CrossOrigin
    public int getPointsOnBoard(@PathVariable String userName, @PathVariable String gameID){
        return betterDuel.get(gameID).getBoardPointsOf(userName);
    }


    @GetMapping(path = "getWonRounds/{userName}/{gameID}")
    @CrossOrigin
    public int getWonRounds(@PathVariable String userName, @PathVariable String gameID){
        return betterDuel.get(gameID).getWonRoundsOf(userName);
    }


    @PostMapping(path = "playCard/{gameID}")
    @CrossOrigin
    public void playCard(@RequestBody List<CardDisplay> specificCards, @RequestParam String userName, @RequestParam int affectedRow,@RequestParam int rowNumber,
                         @PathVariable String gameID){
        int cardPlayedIndex = 0;
        int cardTargetedIndex = 1;
        betterDuel.get(gameID).playCardAs(new PlayerPlay(specificCards.get(cardPlayedIndex), rowNumber, specificCards.get(cardTargetedIndex), affectedRow), userName);
    }
    @GetMapping(path = "getCardInfo/{cardName}")
    @CrossOrigin
    public String GetCardInfo(@PathVariable String cardName){
        return CardsFactory.getCardInfo(cardName);
    }

    @PostMapping(path = "endRound/{gameID}")
    @CrossOrigin
    public void endRound(@RequestParam String userName, @PathVariable String gameID){
        betterDuel.get(gameID).endRoundFor(userName);
    }

    @GetMapping(path = "isTurnOf/{userName}/{gameID}")
    @CrossOrigin
    public boolean isTurnOf(@PathVariable String userName, @PathVariable String gameID){
        return betterDuel.get(gameID).isTurnOf(userName);
    }

    @GetMapping(path = "didWon/{userName}/{gameID}")
    @CrossOrigin
    public boolean didWon(@PathVariable String userName, @PathVariable String gameID){
        return betterDuel.get(gameID).didWon(userName);
    }
    @PostMapping(path = "getPossibleRowsToAffect/{gameID}")
    @CrossOrigin
    public List<Integer> getPossibleRowsToAffect(@RequestBody CardDisplay cardPlayed, @PathVariable String gameID){
        return betterDuel.get(gameID).getPossibleRowsToAffect(cardPlayed);
    }

    @PostMapping(path = "getPossibleTargets/{userName}/{gameID}")
    @CrossOrigin
    public List<CardDisplay> getTargetableCards(@PathVariable String userName, @PathVariable String gameID, @RequestBody CardDisplay cardPlayed){
        return betterDuel.get(gameID).getPossibleTargetsOf(cardPlayed, userName);
    }


    @PostMapping(path="registerUser")
    public void registerUser(@RequestBody List<CardDisplay> deck, @RequestParam String name, @RequestParam String gameID) {
        if(betterDuel.containsKey(gameID) == false) {
            betterDuel.put(gameID, CardDuel.createDuel());
            duel.registerPlayerToDuel(name);
            duel.parseCardsFor(deck, name);
        }
        else {
            duel.registerPlayerToDuel(name);
            duel.parseCardsFor(deck, name);
            duel.dealCards();
        }
    }


}
