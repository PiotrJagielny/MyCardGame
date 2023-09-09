package com.example.demo.DeckBuilding;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.CardsFactory;
import com.example.demo.Consts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeckBuilder {
    private Map<String, Deck> playerDecks;

    public DeckBuilder() {

        playerDecks = new HashMap<>();
        playerDecks.put("Deck", new Deck(CardsFactory.createAllCards()));
    }


    public List<CardDisplay> getCardsPossibleToAdd(String deckName) {
        try {
            return playerDecks.get( deckName ).getCardsPossibleToAdd();
        }
        catch(Exception e) {
            System.out.println("deck name is: " +deckName);
            return List.of();
        }
    }
    public List<CardDisplay> getCurrentDeck(String deckName) {
        try {
            return playerDecks.get(deckName).getCardsInDeck();
        }
        catch(Exception e) {
            System.out.println("There was no such deck as: " + deckName);
            return List.of();
        }
    }

    public String addCardToDeck(CardDisplay card, String deckName) {
        return playerDecks.get( deckName ).addCard(card);
    }


    public void putCardFromDeckBack(CardDisplay cardDisplay, String deckName) {
        playerDecks.get(deckName).putCardFromDeckBack(cardDisplay);
    }


    public String deleteCurrentDeck(String deckName) {
        String responseMessage = "";
        if(isAbleToDeleteDeck(deckName) == false){
            responseMessage = "Cannot delete last deck";
            return responseMessage;
        }
        deleteDeck(deckName);
        return responseMessage;
    }

    private Boolean isAbleToDeleteDeck(String deckName){return playerDecks.size() != 1;}

    private void deleteDeck(String deckName) {
        playerDecks.remove(deckName);
    }



    public List<String> getDecksNames() {
        List<String> result = new ArrayList<String>();
        for (Map.Entry<String, Deck> entry : playerDecks.entrySet()) {
            result.add(entry.getKey());
        }
        return result;
    }




    public void createDeck(String deckName) {
        if(getDecksNames().contains(deckName) == false){
            playerDecks.put(deckName, new Deck(CardsFactory.createAllCards()));
        }
    }




    public boolean isDeckValid(String deckName) {
        Deck deck = playerDecks.get(deckName);
        return deck.getCardsInDeck().size() >= Consts.minDeckSize && deck.getCardsInDeck().size() <= Consts.maxDeckSize;
    }
}
