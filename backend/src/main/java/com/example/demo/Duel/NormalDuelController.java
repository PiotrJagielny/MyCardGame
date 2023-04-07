package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
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
        return duel.getCardsInHandDisplay_player1();
    }
    @GetMapping(path = "getHandCards_player2")
    @CrossOrigin
    public List<CardDisplay> getHand_player2(){
        return duel.getCardsInHandDisplay_player2();
    }


    @GetMapping(path = "getDeckCards_player1")
    @CrossOrigin
    public List<CardDisplay> getDeck_player1() {
        return duel.getCardsInDeckDisplay_player1();
    }
    @GetMapping(path = "getDeckCards_player2")
    @CrossOrigin
    public List<CardDisplay> getDeck_player2() {
        return duel.getCardsInDeckDisplay_player2();
    }

    @GetMapping(path = "getBoardCards_player1")
    @CrossOrigin
    public List<CardDisplay> getBoard_player1(){
        return duel.getCardsOnBoardDisplay_player1();
    }
    @GetMapping(path = "getBoardCards_player2")
    @CrossOrigin
    public List<CardDisplay> getBoard_player2(){
        return duel.getCardsOnBoardDisplay_player1();
    }


    @GetMapping(path = "getBoardPoints_player1")
    @CrossOrigin
    public int getPointsOnBoard_player1(){
        return duel.getBoardPoints_player1();
    }
    @GetMapping(path = "getBoardPoints_player2")
    @CrossOrigin
    public int getPointsOnBoard_player2(){
        return duel.getBoardPoints_player2();
    }


    @PostMapping(path = "playCard_player1")
    @CrossOrigin
    public void playCard_player1(@RequestBody CardDisplay cardPlayed){
        duel.playCard_asPlayer1(cardPlayed);
    }
    @PostMapping(path = "playCard_player2")
    @CrossOrigin
    public void playCard_player2(@RequestBody CardDisplay cardPlayed){
        duel.playCard_asPlayer2(cardPlayed);
    }


    @PostMapping(path = "SetupDecks")
    @CrossOrigin
    public void SetupDeck(@RequestBody List<CardDisplay> cardsInDeck){
        duel = CardDuel.createDuel();
        duel.parseCards_forPlayer1(cardsInDeck);
        duel.parseCards_forPlayer2(cardsInDeck);
        duel.dealCards();
        duel.dealCards();
    }




}
