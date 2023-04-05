package com.example.demo.DeckBuilding;


import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.DeckBuilding.Services.DeckBuilder;
import com.example.demo.DeckBuilding.Services.DeckBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Service
@RequestMapping(path = "/DeckBuilder")
public class DeckBuilderController {

    private DeckBuilder DeckBuilderService;

    @Autowired
    public DeckBuilderController() {
        DeckBuilderService = new DeckBuilderService();
    }

    @GetMapping(path ="GetAllCards")
    @CrossOrigin
    public List<CardDisplay> GetAllCards(){
        return DeckBuilderService.getCardsPossibleToAdd();
    }

    @GetMapping(path = "GetCardsInDeck")
    @CrossOrigin
    public List<CardDisplay> GetCardsInDeck(){return DeckBuilderService.getPlayerDeck();}

    @GetMapping(path = "GetDecksNames")
    @CrossOrigin
    public List<String> GetDecksNames(){return DeckBuilderService.getDecksNames();}

    @PostMapping(path = "PutCardToDeck")
    @CrossOrigin
    public String AddCardToDeck(@RequestBody CardDisplay cardDisplay) {
        String ResponseMessage = DeckBuilderService.addCardToDeck(cardDisplay);
        return ResponseMessage;
    }

    @PostMapping(path = "PutCardFromDeckBack")
    @CrossOrigin
    public void PutCardFromDeckBack(@RequestBody CardDisplay cardDisplay){
        DeckBuilderService.putCardFromDeckBack(cardDisplay);
    }

    @PostMapping(path = "CreateDeck")
    @CrossOrigin
    public void CreateDeck(@RequestBody String deckName) {
        DeckBuilderService.createDeck(deckName);
    }

    @PostMapping(path = "SelectDeck")
    @CrossOrigin
    public void SelectDeck(@RequestBody String deckName){
        DeckBuilderService.selectDeck(deckName);
    }

    @PostMapping(path = "DeleteDeck")
    @CrossOrigin
    public String DeleteDeck() {
        return DeckBuilderService.deleteCurrentDeck();
    }


}
