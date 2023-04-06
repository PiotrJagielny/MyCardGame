package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.NormalDuel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/Duel")
public class NormalDuelController {

    CardDuel duel;


    public NormalDuelController() {
        duel = new NormalDuel();
    }

    @GetMapping(path = "GetHandCards")
    @CrossOrigin
    public List<CardDisplay> getPlayerHand(){
        return List.of(new CardDisplay("Normalny"));
        //return duel.getPlayerCardsInHandDisplay();
    }

    @GetMapping(path = "GetDeckCards")
    @CrossOrigin
    public List<CardDisplay> getPlayerDeck() {

        return duel.getPlayerCardsInDeckDisplay();
    }

    @GetMapping(path = "GetBoardCards")
    @CrossOrigin
    public List<CardDisplay> getCardsOnBoard(){
        return duel.getCardsOnBoardDisplay();
    }

    @GetMapping(path = "GetBoardPoints")
    @CrossOrigin
    public int getPointsOnBoard(){

        return duel.getBoardPoints();
    }

    @PostMapping(path = "SetupDeck")
    @CrossOrigin
    public void SetupDeck(@RequestBody List<CardDisplay> cardsInDeck){
        duel.parseCards(cardsInDeck);
        //duel.dealCards();
    }

    @PostMapping(path = "PlayCard")
    @CrossOrigin
    public void PlayCard(@RequestBody CardDisplay cardPlayed){
        duel.playCard(cardPlayed);
    }


}
