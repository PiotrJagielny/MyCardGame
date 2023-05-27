package com.example.demo.DeckBuilding;


import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.DeckBuilding.Services.DeckBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Service
@RequestMapping(path = "/DeckBuilder")
public class DeckBuilderController {

    private DeckBuilder deckBuilder;

    @Autowired
    public DeckBuilderController() {
        deckBuilder = new DeckBuilder();
    }

    @GetMapping(path ="GetAllCards")
    @CrossOrigin
    public List<CardDisplay> GetAllCards(){

        return deckBuilder.getCardsPossibleToAdd();
    }

    @GetMapping(path = "GetCardsInDeck")
    @CrossOrigin
    public List<CardDisplay> GetCardsInDeck(){return deckBuilder.getPlayerDeck();}

    @GetMapping(path = "GetDecksNames")
    @CrossOrigin
    public List<String> GetDecksNames(){return deckBuilder.getDecksNames();}

    @PostMapping(path = "PutCardToDeck")
    @CrossOrigin
    public String AddCardToDeck(@RequestBody CardDisplay cardDisplay) {
        String ResponseMessage = deckBuilder.addCardToDeck(cardDisplay);
        return ResponseMessage;
    }

    @PostMapping(path = "PutCardFromDeckBack")
    @CrossOrigin
    public void PutCardFromDeckBack(@RequestBody CardDisplay cardDisplay){
        deckBuilder.putCardFromDeckBack(cardDisplay);
    }

    @PostMapping(path = "CreateDeck")
    @CrossOrigin
    public void CreateDeck(@RequestBody String deckName) {
        deckBuilder.createDeck(deckName);
    }

    @PostMapping(path = "SelectDeck")
    @CrossOrigin
    public void SelectDeck(@RequestBody String deckName){
        deckBuilder.selectDeck(deckName);
    }

    @PostMapping(path = "DeleteDeck")
    @CrossOrigin
    public String DeleteDeck() {
        return deckBuilder.deleteCurrentDeck();
    }


}
