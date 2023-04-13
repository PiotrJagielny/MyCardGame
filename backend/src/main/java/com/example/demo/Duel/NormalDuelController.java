package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Consts;
import com.example.demo.Duel.Services.CardDuel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/Duel")
public class NormalDuelController {

    private CardDuel duel;


    @GetMapping(path = "getHandCards_player1")
    @CrossOrigin
    public List<CardDisplay> getHand_player1(){
        return duel.getCardsInHandDisplayOf(Consts.firstPlayer);
    }
    @GetMapping(path = "getHandCards_player2")
    @CrossOrigin
    public List<CardDisplay> getHand_player2(){
        return duel.getCardsInHandDisplayOf(Consts.secondPlayer);
    }


    @GetMapping(path = "getDeckCards_player1")
    @CrossOrigin
    public List<CardDisplay> getDeck_player1() {
        return duel.getCardsInDeckDisplayOf(Consts.firstPlayer);
    }
    @GetMapping(path = "getDeckCards_player2")
    @CrossOrigin
    public List<CardDisplay> getDeck_player2() {
        return duel.getCardsInDeckDisplayOf(Consts.secondPlayer);
    }

    @GetMapping(path = "getBoardCards_player1")
    @CrossOrigin
    public List<CardDisplay> getBoard_player1(){
        return duel.getCardsOnBoardDisplayOf(Consts.firstPlayer);
    }
    @GetMapping(path = "getBoardCards_player2")
    @CrossOrigin
    public List<CardDisplay> getBoard_player2(){
        return duel.getCardsOnBoardDisplayOf(Consts.secondPlayer);
    }


    @GetMapping(path = "getBoardPoints_player1")
    @CrossOrigin
    public int getPointsOnBoard_player1(){
        return duel.getBoardPointsOf(Consts.firstPlayer);
    }
    @GetMapping(path = "getBoardPoints_player2")
    @CrossOrigin
    public int getPointsOnBoard_player2(){
        return duel.getBoardPointsOf(Consts.secondPlayer);
    }


    @PostMapping(path = "playCard_player1")
    @CrossOrigin
    public void playCard_player1(@RequestBody CardDisplay cardPlayed){
        duel.playCardAs(cardPlayed, Consts.firstPlayer);
    }
    @PostMapping(path = "playCard_player2")
    @CrossOrigin
    public void playCard_player2(@RequestBody CardDisplay cardPlayed){
        duel.playCardAs(cardPlayed, Consts.secondPlayer);
    }


    @PostMapping(path = "SetupDecks")
    @CrossOrigin
    public void SetupDeck(@RequestBody List<CardDisplay> cardsInDeck){
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(Consts.firstPlayer);
        duel.registerPlayerToDuel(Consts.secondPlayer);
        duel.parseCardsFor(cardsInDeck, Consts.firstPlayer);
        duel.parseCardsFor(cardsInDeck, Consts.secondPlayer);
        duel.dealCards();
        duel.dealCards();
        duel.setTurnTo(Consts.firstPlayer);
    }

}
