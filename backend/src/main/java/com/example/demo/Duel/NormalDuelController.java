package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Consts;
import com.example.demo.Duel.DataStructures.PlayerPlay;
import com.example.demo.Duel.Services.CardDuel;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(path = "/Duel")
public class NormalDuelController {

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
    public void playCard(@RequestBody CardDisplay cardPlayed, @RequestParam String userName, @RequestParam int rowNumber){
        duel.playCardAs(new PlayerPlay(cardPlayed, rowNumber), userName);
    }

    @PostMapping(path = "endRound")
    @CrossOrigin
    public void endRound(@RequestParam String userName){
        duel.endRoundFor(userName);
    }

    @GetMapping(path = "isTurnOf/{userName}")
    @CrossOrigin
    public boolean isTurnOf_player2(@PathVariable String userName){
        return duel.isTurnOf(userName);
    }

    @GetMapping(path = "didWon/{userName}")
    @CrossOrigin
    public boolean didWon_player2(@PathVariable String userName){
        return duel.didWon(userName);
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

}
