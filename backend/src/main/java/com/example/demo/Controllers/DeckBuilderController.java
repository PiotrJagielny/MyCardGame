package com.example.demo.Controllers;


import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Controllers.DeckBuilding.DeckBuilder;
import com.example.demo.DecksPersistence.DecksDatabase;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Service
@RequestMapping(path = "/DeckBuilder")
public class DeckBuilderController {

    private Map<String, DeckBuilder> deckBuilders = new HashMap<>();

    @PostMapping("setupBuilder")
    @CrossOrigin
    public void setupBuilder(@RequestBody String userName) {
        //TODO add a feature so that there cant be two same user names
        if(deckBuilders.containsKey(userName) == false)
            deckBuilders.put(userName, DecksDatabase.load(userName));

    }

    @GetMapping(path ="GetAllCards/{userName}/{deckName}")
    @CrossOrigin
    public List<CardDisplay> GetAllCards(@PathVariable String userName, @PathVariable String deckName){
        return deckBuilders.get(userName).getCardsPossibleToAdd(deckName);
    }

    @GetMapping(path = "GetCardsInDeck/{userName}/{deckName}")
    @CrossOrigin
    public List<CardDisplay> GetCardsInDeck(@PathVariable String userName, @PathVariable String deckName){
        return deckBuilders.get(userName).getCurrentDeck(deckName);
    }

    @GetMapping(path = "GetDecksNames/{userName}")
    @CrossOrigin
    public List<String> GetDecksNames(@PathVariable String userName){return deckBuilders.get(userName).getDecksNames();}

    @PostMapping(path = "PutCardToDeck/{userName}/{deckName}")
    @CrossOrigin
    public String AddCardToDeck(@RequestBody CardDisplay cardDisplay, @PathVariable String userName, @PathVariable String deckName) {
        String ResponseMessage = deckBuilders.get(userName).addCardToDeck(cardDisplay, deckName);
        DecksDatabase.saveDeck(userName,deckName, deckBuilders.get(userName).getCurrentDeck(deckName));
        return ResponseMessage;
    }

    @PostMapping(path = "PutCardFromDeckBack/{userName}/{deckName}")
    @CrossOrigin
    public void PutCardFromDeckBack(@RequestBody CardDisplay cardDisplay, @PathVariable String userName, @PathVariable String deckName){
        deckBuilders.get(userName).putCardFromDeckBack(cardDisplay, deckName);
        DecksDatabase.saveDeck(userName,deckName, deckBuilders.get(userName).getCurrentDeck(deckName));
    }

    @PostMapping(path = "CreateDeck/{userName}")
    @CrossOrigin
    public void CreateDeck(@RequestBody String deckName, @PathVariable String userName) {
        deckBuilders.get(userName).createDeck(deckName);
        List<String> decks = deckBuilders.get(userName).getDecksNames();
        if(decks.contains(deckName)) {
            DecksDatabase.createDeck(userName,deckName);
        }
    }

    @PostMapping(path = "DeleteDeck/{userName}/{deckName}")
    @CrossOrigin
    public String DeleteDeck(@PathVariable String userName, @PathVariable String deckName) {
        DecksDatabase.deleteDeck(userName, deckName);
        String response = deckBuilders.get(userName).deleteCurrentDeck(deckName);
        return response;
    }
    @GetMapping(path = "ValidateDeck/{userName}/{deckName}")
    @CrossOrigin
    public boolean validateDeck(@PathVariable String deckName, @PathVariable String userName) {
        return deckBuilders.get(userName).isDeckValid(deckName);
    }

}
