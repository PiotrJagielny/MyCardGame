package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.NormalDuel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return duel.getPlayerCardsInHandDisplay();
    }

    @GetMapping(path = "GetDeckCards")
    @CrossOrigin
    public List<CardDisplay> getPlayerDeck(){
        return duel.getPlayerCardsInDeckDisplay();
    }

    @GetMapping(path = "GetBoardCards")
    @CrossOrigin
    public List<CardDisplay> getCardsOnBoard(){
        return duel.getCardsOnBoardDisplay();
    }


}
