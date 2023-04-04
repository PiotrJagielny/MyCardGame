package com.example.demo.DeckBuilding;


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
    public List<Card> GetAllCards(){
        return DeckBuilderService.getCardsPossibleToAdd();
    }

    @GetMapping(path = "GetCardsInDeck")
    @CrossOrigin
    public List<Card> GetCardsInDeck(){return DeckBuilderService.getPlayerDeck();}

    @GetMapping(path = "GetDecksNames")
    @CrossOrigin
    public List<String> GetDecksNames(){return DeckBuilderService.getDecksNames();}

    @PostMapping(path = "PutCardToDeck")
    @CrossOrigin
    public String AddCardToDeck(@RequestBody String cardDisplay) {
        String ResponseMessage = DeckBuilderService.addCardToDeck(cardDisplay);
        return ResponseMessage;
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

    @PostMapping(path = "PutCardFromDeckBack")
    @CrossOrigin
    public void PutCardFromDeckBack(@RequestBody String cardDisplay){
        DeckBuilderService.putCardFromDeckBack(cardDisplay);
    }

}
