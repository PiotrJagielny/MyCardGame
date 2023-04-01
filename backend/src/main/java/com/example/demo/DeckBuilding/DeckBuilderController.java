package com.example.demo.DeckBuilding;


import com.example.demo.Cards.Card;
import com.example.demo.Cards.Deck;
import com.example.demo.DeckBuilding.Services.DeckBuilder;
import com.example.demo.DeckBuilding.Services.DeckBuilderFactory;
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
        DeckBuilderService = DeckBuilderFactory.GetDeckBuilderService();
    }

    @GetMapping(path ="GetAllCards")
    @CrossOrigin
    public List<Card> GetAllCards(){
        return DeckBuilderService.GetCardsPossibleToAdd();
    }

    @GetMapping(path = "GetCardsInDeck")
    @CrossOrigin
    public List<Card> GetCardsInDeck(){return DeckBuilderService.GetPlayerDeck();}

    @GetMapping(path = "GetDecksNames")
    @CrossOrigin
    public List<String> GetDecksNames(){return DeckBuilderService.GetDecksNames();}

    @PostMapping(path = "PutCardToDeck")
    @CrossOrigin
    public String AddCardToDeck(@RequestBody String CardName) {
        String ResponseMessage = DeckBuilderService.AddCardToDeck(CardName);
        return ResponseMessage;
    }

    @PostMapping(path = "PutCardFromDeckBack")
    @CrossOrigin
    public void PutCardFromDeckBack(@RequestBody String CardName){
        DeckBuilderService.PutCardFromDeckBack(CardName);
    }

}
