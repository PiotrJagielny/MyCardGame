package com.example.demo.DeckBuilding;

import com.example.demo.Cards.Card;
import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.CardsFactory;
import com.example.demo.Consts;

import java.util.*;

public class DeckBuilder {
    private Map<String, Deck> decks;

    public DeckBuilder() {
        decks = new HashMap<>();
    }


    public List<CardDisplay> getCardsPossibleToAdd(String deckName) {
        return decks.get( deckName ).getCardsPossibleToAdd();
    }
    public List<CardDisplay> getCurrentDeck(String deckName) {
        return decks.get(deckName).getCardsInDeck();
    }

    public String addCardToDeck(CardDisplay card, String deckName) {
        return decks.get( deckName ).addCard(card);
    }

    public void sortCardsPossibleToAddBy(String deckName, String criteria) {
        decks.get(deckName).sortCardsPossibleToAddBy(criteria);
    }


    public void putCardFromDeckBack(CardDisplay cardDisplay, String deckName) {
        decks.get(deckName).putCardFromDeckBack(cardDisplay);
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

    private Boolean isAbleToDeleteDeck(String deckName){return decks.size() != 1;}

    private void deleteDeck(String deckName) {
        decks.remove(deckName);
    }



    public List<String> getDecksNames() {
        List<String> result = new ArrayList<String>();
        for (Map.Entry<String, Deck> entry : decks.entrySet()) {
            result.add(entry.getKey());
        }
        return result;
    }




    public void createDeck(String deckName) {
        if(getDecksNames().contains(deckName) == false){
            decks.put(deckName, new Deck(CardsFactory.createAllCards()));
        }
    }




    public boolean isDeckValid(String deckName) {
        Deck deck = decks.get(deckName);
        return deck.getCardsInDeck().size() >= Consts.minDeckSize && deck.getCardsInDeck().size() <= Consts.maxDeckSize;
    }

    public void searchForCards(String deckName, String searchString) {
        decks.get(deckName).searchForCards(searchString);
    }
}
