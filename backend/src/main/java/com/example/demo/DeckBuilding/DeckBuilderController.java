package com.example.demo.DeckBuilding;


import com.example.demo.CardsServices.CardDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Service
@RequestMapping(path = "/DeckBuilder")
public class DeckBuilderController {

    private DeckBuilder deckBuilder;
    private Map<String, DeckBuilder> betterDeckBuilders = new HashMap<>();

    @Autowired
    public DeckBuilderController() {
        deckBuilder = new DeckBuilder();
    }
    @PostMapping("setupBuilder")
    @CrossOrigin
    public void setupBuilder(@RequestBody String userName) {
        System.out.println("Setting up builder");
        //TODO add a feature so that there cant be two same user names
        betterDeckBuilders.put(userName, new DeckBuilder());
    }

    @GetMapping(path ="GetAllCards/{userName}")
    @CrossOrigin
    public List<CardDisplay> GetAllCards(@PathVariable String userName){
        System.out.println("getting cards");
        return betterDeckBuilders.get(userName).getCardsPossibleToAdd();
    }

    @GetMapping(path = "GetCardsInDeck/{userName}")
    @CrossOrigin
    public List<CardDisplay> GetCardsInDeck(@PathVariable String userName){
        return betterDeckBuilders.get(userName).getPlayerDeck();
    }

    @GetMapping(path = "GetDecksNames/{userName}")
    @CrossOrigin
    public List<String> GetDecksNames(@PathVariable String userName){return betterDeckBuilders.get(userName).getDecksNames();}

    @PostMapping(path = "PutCardToDeck/{userName}")
    @CrossOrigin
    public String AddCardToDeck(@RequestBody CardDisplay cardDisplay, @PathVariable String userName) {
        String ResponseMessage = betterDeckBuilders.get(userName).addCardToDeck(cardDisplay);
        return ResponseMessage;
    }

    @PostMapping(path = "PutCardFromDeckBack/{userName}")
    @CrossOrigin
    public void PutCardFromDeckBack(@RequestBody CardDisplay cardDisplay, @PathVariable String userName){
        betterDeckBuilders.get(userName).putCardFromDeckBack(cardDisplay);
    }

    @PostMapping(path = "CreateDeck/{userName}")
    @CrossOrigin
    public void CreateDeck(@RequestBody String deckName, @PathVariable String userName) {
        betterDeckBuilders.get(userName).createDeck(deckName);
    }

    @PostMapping(path = "SelectDeck/{userName}")
    @CrossOrigin
    public void SelectDeck(@RequestBody String deckName, @PathVariable String userName){
        betterDeckBuilders.get(userName).selectDeck(deckName);
    }

    @PostMapping(path = "DeleteDeck/{userName}")
    @CrossOrigin
    public String DeleteDeck(@PathVariable String userName) {
        return betterDeckBuilders.get(userName).deleteCurrentDeck();
    }

}
