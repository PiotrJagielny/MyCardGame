package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.PlayerNumber;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/Duel")
public class NormalDuelController {

    private CardDuel duel;


    @GetMapping(path = "getHandCards_player1")
    @CrossOrigin
    public List<CardDisplay> getHand_player1(){
        return duel.getCardsInHandDisplayOf(PlayerNumber.FirstPlayer.toString());
    }
    @GetMapping(path = "getHandCards_player2")
    @CrossOrigin
    public List<CardDisplay> getHand_player2(){
        return duel.getCardsInHandDisplayOf(PlayerNumber.SecondPlayer.toString());
    }


    @GetMapping(path = "getDeckCards_player1")
    @CrossOrigin
    public List<CardDisplay> getDeck_player1() {
        return duel.getCardsInDeckDisplayOf(PlayerNumber.FirstPlayer.toString());
    }
    @GetMapping(path = "getDeckCards_player2")
    @CrossOrigin
    public List<CardDisplay> getDeck_player2() {
        return duel.getCardsInDeckDisplayOf(PlayerNumber.SecondPlayer.toString());
    }

    @GetMapping(path = "getBoardCards_player1")
    @CrossOrigin
    public List<CardDisplay> getBoard_player1(){
        return duel.getCardsOnBoardDisplayOf(PlayerNumber.FirstPlayer.toString());
    }
    @GetMapping(path = "getBoardCards_player2")
    @CrossOrigin
    public List<CardDisplay> getBoard_player2(){
        return duel.getCardsOnBoardDisplayOf(PlayerNumber.SecondPlayer.toString());
    }


    @GetMapping(path = "getBoardPoints_player1")
    @CrossOrigin
    public int getPointsOnBoard_player1(){
        return duel.getBoardPointsOf(PlayerNumber.FirstPlayer.toString());
    }
    @GetMapping(path = "getBoardPoints_player2")
    @CrossOrigin
    public int getPointsOnBoard_player2(){
        return duel.getBoardPointsOf(PlayerNumber.SecondPlayer.toString());
    }


    @PostMapping(path = "playCard_player1")
    @CrossOrigin
    public void playCard_player1(@RequestBody CardDisplay cardPlayed){
        duel.playCardAs(cardPlayed, PlayerNumber.FirstPlayer.toString());
    }
    @PostMapping(path = "playCard_player2")
    @CrossOrigin
    public void playCard_player2(@RequestBody CardDisplay cardPlayed){
        duel.playCardAs(cardPlayed, PlayerNumber.SecondPlayer.toString());
    }


    @PostMapping(path = "SetupDecks")
    @CrossOrigin
    public void SetupDeck(@RequestBody List<CardDisplay> cardsInDeck){
        duel = CardDuel.createDuel();
        duel.parseCardsFor(cardsInDeck, PlayerNumber.FirstPlayer.toString());
        duel.parseCardsFor(cardsInDeck, PlayerNumber.SecondPlayer.toString());
        duel.dealCards();
        duel.dealCards();
    }

}
