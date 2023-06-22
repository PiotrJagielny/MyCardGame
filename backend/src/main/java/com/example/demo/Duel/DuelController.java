package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/Duel")
public class DuelController {

    private CardDuel duel;


    @GetMapping(path = "getHandCards/{userName}")
    @CrossOrigin
    public List<CardDisplay> getHand(@PathVariable String userName){
        return duel.getCardsInHandDisplayOf(userName);
    }

    @GetMapping(path = "getDeckCards/{userName}")
    @CrossOrigin
    public List<CardDisplay> getDeck(@PathVariable String userName) {
        return duel.getCardsInDeckDisplayOf(userName);
    }

    @GetMapping(path = "getCardsOnRow/{userName}/{rowNumber}")
    @CrossOrigin
    public List<CardDisplay> getRow(@PathVariable String userName, @PathVariable int rowNumber){
        return duel.getCardsOnBoardDisplayOf(userName, rowNumber);
    }


    @GetMapping(path = "getBoardPoints/{userName}")
    @CrossOrigin
    public int getPointsOnBoard(@PathVariable String userName){
        return duel.getBoardPointsOf(userName);
    }


    @GetMapping(path = "getWonRounds/{userName}")
    @CrossOrigin
    public int getWonRounds(@PathVariable String userName){return duel.getWonRoundsOf(userName);}


    @PostMapping(path = "playCard")
    @CrossOrigin
    public void playCard(@RequestBody List<CardDisplay> specificCards, @RequestParam String userName, @RequestParam int affectedRow,@RequestParam int rowNumber){
        int cardPlayedIndex = 0;
        int cardTargetedIndex = 1;
        duel.playCardAs(new PlayerPlay(specificCards.get(cardPlayedIndex), rowNumber, specificCards.get(cardTargetedIndex), affectedRow), userName);
    }
    @GetMapping(path = "getCardInfo/{cardName}")
    @CrossOrigin
    public String GetCardInfo(@PathVariable String cardName){
        return CardsFactory.getCardInfo(cardName);
    }

    @PostMapping(path = "endRound")
    @CrossOrigin
    public void endRound(@RequestParam String userName){
        duel.endRoundFor(userName);
    }

    @GetMapping(path = "isTurnOf/{userName}")
    @CrossOrigin
    public boolean isTurnOf(@PathVariable String userName){
        return duel.isTurnOf(userName);
    }

    @GetMapping(path = "didWon/{userName}")
    @CrossOrigin
    public boolean didWon(@PathVariable String userName){
        return duel.didWon(userName);
    }
    @PostMapping(path = "getPossibleRowsToAffect")
    @CrossOrigin
    public List<Integer> getPossibleRowsToAffect(@RequestBody CardDisplay cardPlayed){
        return duel.getPossibleRowsToAffect(cardPlayed);
    }

    @PostMapping(path = "SetupDecks")
    @CrossOrigin
    public void SetupDeck(@RequestBody List<CardDisplay> cardsInDeck, @RequestParam String firstUser,@RequestParam String secondUser ){
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(firstUser);
        duel.registerPlayerToDuel(secondUser);
        duel.parseCardsFor(cardsInDeck, firstUser);
        duel.parseCardsFor(cardsInDeck, secondUser);
        duel.dealCards();
    }

    @PostMapping(path = "getPossibleTargets/{userName}")
    @CrossOrigin
    public List<CardDisplay> getTargetableCards(@PathVariable String userName, @RequestBody CardDisplay cardPlayed){
        return duel.getPossibleTargetsOf(cardPlayed, userName);
    }

}
