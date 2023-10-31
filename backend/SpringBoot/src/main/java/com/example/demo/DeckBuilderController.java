package com.example.demo;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.DeckBuilding.DeckBuilder;
import com.example.demo.cardsPersistence.DecksDatabase;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Service
@RequestMapping(path = "/DeckBuilder")
@CrossOrigin
public class DeckBuilderController {

    private Map<String, DeckBuilder> deckBuilders = new HashMap<>();

    @PostMapping("setupBuilder")
    public void setupBuilder(@RequestBody String userName) {
        //TODO add a feature so that there cant be two same user names
        if(deckBuilders.containsKey(userName) == false)
            deckBuilders.put(userName, DecksDatabase.load(userName));

    }

    @GetMapping(path ="GetAllCards/{userName}/{deckName}")
    public List<CardDisplay> GetAllCards(@PathVariable String userName, @PathVariable String deckName){
        return deckBuilders.get(userName).getCardsPossibleToAdd(deckName);
    }

    @PostMapping(path = "SortAddableCardsBy/{userName}/{deckName}/{criteria}")
    public List<CardDisplay> sortAddableCardsBy(@PathVariable String userName, @PathVariable String deckName, @PathVariable String criteria) {
        deckBuilders.get(userName).sortCardsPossibleToAddBy(deckName,criteria);
        return deckBuilders.get(userName).getCardsPossibleToAdd(deckName);
    }

    @PostMapping(path = "SearchForCards/{userName}/{deckName}/{searchString}")
    public List<CardDisplay> searchForCards(@PathVariable String userName,@PathVariable String deckName,@PathVariable String searchString) {
        deckBuilders.get(userName).searchForCards(deckName, searchString);
        return deckBuilders.get(userName).getCardsPossibleToAdd(deckName);
    }
    @PostMapping(path = "ClearSearch/{userName}/{deckName}")
    public List<CardDisplay> clearSearchString(@PathVariable String userName,@PathVariable String deckName) {
        deckBuilders.get(userName).searchForCards(deckName, "");
        return deckBuilders.get(userName).getCardsPossibleToAdd(deckName);
    }

    @GetMapping(path = "GetCardsInDeck/{userName}/{deckName}")
    public List<CardDisplay> GetCardsInDeck(@PathVariable String userName, @PathVariable String deckName){
        return deckBuilders.get(userName).getCurrentDeck(deckName);
    }

    @GetMapping(path = "GetDeckFraction/{userName}/{deckName}")
    public String getDeckFraction(@PathVariable String userName, @PathVariable String deckName) {
        return deckBuilders.get(userName).getDeckFraction(deckName);
    }

    @GetMapping(path = "GetDecksNames/{userName}")
    public List<String> GetDecksNames(@PathVariable String userName){return deckBuilders.get(userName).getDecksNames();}

    @PostMapping(path = "PutCardToDeck/{userName}/{deckName}")
    public String AddCardToDeck(@RequestBody CardDisplay cardDisplay, @PathVariable String userName, @PathVariable String deckName) {
        deckBuilders.get(userName).addCardToDeck(cardDisplay, deckName);
        DecksDatabase.saveDeck(userName,deckName, deckBuilders.get(userName).getDeckFraction(deckName),deckBuilders.get(userName).getCurrentDeck(deckName));
        return "";
    }

    @PostMapping(path = "PutCardFromDeckBack/{userName}/{deckName}")
    public void PutCardFromDeckBack(@RequestBody CardDisplay cardDisplay, @PathVariable String userName, @PathVariable String deckName){
        deckBuilders.get(userName).putCardFromDeckBack(cardDisplay, deckName);
        DecksDatabase.saveDeck(userName,deckName, deckBuilders.get(userName).getDeckFraction(deckName),deckBuilders.get(userName).getCurrentDeck(deckName));
    }

    @PostMapping(path = "CreateDeck/{userName}/{fraction}")
    public void CreateDeck(@RequestBody String deckName, @PathVariable String userName, @PathVariable String fraction) {
        deckBuilders.get(userName).createDeck(deckName, fraction);
        List<String> decks = deckBuilders.get(userName).getDecksNames();
        if(decks.contains(deckName)) {
            DecksDatabase.createDeck(userName,fraction,deckName);
        }
    }

    @GetMapping(path = "GetFractions")
    public List<String> getFractions() {
        return List.of(Consts.Fraction.humans, Consts.Fraction.monsters);
    }

    @PostMapping(path = "DeleteDeck/{userName}/{deckName}")
    public String DeleteDeck(@PathVariable String userName, @PathVariable String deckName) {
        DecksDatabase.deleteDeck(userName, deckName);
        String response = deckBuilders.get(userName).deleteCurrentDeck(deckName);
        return response;
    }
    @GetMapping(path = "ValidateDeck/{userName}/{deckName}")
    public boolean validateDeck(@PathVariable String deckName, @PathVariable String userName) {
        return deckBuilders.get(userName).isDeckValid(deckName);
    }

}
