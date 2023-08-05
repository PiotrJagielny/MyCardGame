package com.example.demo.DeckBuilding;


import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.DeckBuilding.DecksPersistence.DecksDatabase;
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

    @GetMapping(path ="GetAllCards/{userName}")
    @CrossOrigin
    public List<CardDisplay> GetAllCards(@PathVariable String userName){
        return deckBuilders.get(userName).getCardsPossibleToAdd();
    }

    @GetMapping(path = "GetCardsInDeck/{userName}")
    @CrossOrigin
    public List<CardDisplay> GetCardsInDeck(@PathVariable String userName){
        return deckBuilders.get(userName).getCurrentDeck();
    }

    @GetMapping(path = "GetDecksNames/{userName}")
    @CrossOrigin
    public List<String> GetDecksNames(@PathVariable String userName){return deckBuilders.get(userName).getDecksNames();}

    @PostMapping(path = "PutCardToDeck/{userName}")
    @CrossOrigin
    public String AddCardToDeck(@RequestBody CardDisplay cardDisplay, @PathVariable String userName) {
        String ResponseMessage = deckBuilders.get(userName).addCardToDeck(cardDisplay);
        DecksDatabase.save(deckBuilders.get(userName), userName);
        return ResponseMessage;
    }

    @PostMapping(path = "PutCardFromDeckBack/{userName}")
    @CrossOrigin
    public void PutCardFromDeckBack(@RequestBody CardDisplay cardDisplay, @PathVariable String userName){
        deckBuilders.get(userName).putCardFromDeckBack(cardDisplay);
        DecksDatabase.save(deckBuilders.get(userName), userName);
    }

    @PostMapping(path = "CreateDeck/{userName}")
    @CrossOrigin
    public void CreateDeck(@RequestBody String deckName, @PathVariable String userName) {
        deckBuilders.get(userName).createDeck(deckName);
        DecksDatabase.save(deckBuilders.get(userName), userName);
    }

    @PostMapping(path = "SelectDeck/{userName}")
    @CrossOrigin
    public void SelectDeck(@RequestBody String deckName, @PathVariable String userName){
        deckBuilders.get(userName).selectDeck(deckName);
    }

    @PostMapping(path = "DeleteDeck/{userName}")
    @CrossOrigin
    public String DeleteDeck(@PathVariable String userName) {
        String response = deckBuilders.get(userName).deleteCurrentDeck();
        DecksDatabase.save(deckBuilders.get(userName), userName);
        return response;
    }
    @GetMapping(path = "ValidateDeck/{userName}/{deckName}")
    @CrossOrigin
    public boolean validateDeck(@PathVariable String deckName, @PathVariable String userName) {
        return deckBuilders.get(userName).isDeckValid(deckName);
    }

}
