package com.example.demo.DeckBuilding;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Consts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class DeckBuilder {
    private Map<String, Deck> playerDecksBetter;

    public DeckBuilder() {

        playerDecksBetter = new HashMap<>();
        playerDecksBetter.put("Deck", new Deck(CardsFactory.createAllCards()));
    }


    public List<CardDisplay> getCardsPossibleToAdd(String deckName) {
        return playerDecksBetter.get( deckName ).getCardsPossibleToAdd();
    }

    public String addCardToDeck(CardDisplay card, String deckName) {
        return playerDecksBetter.get( deckName ).addCard(card);
    }


    public void putCardFromDeckBack(CardDisplay cardDisplay, String deckName) {
        playerDecksBetter.get(deckName).putCardFromDeckBack(cardDisplay);
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

    private Boolean isAbleToDeleteDeck(String deckName){return playerDecksBetter.size() != 1;}

    private void deleteDeck(String deckName) {
        playerDecksBetter.remove(deckName);
    }



    public List<String> getDecksNames() {
        List<String> result = new ArrayList<String>();
        for (Map.Entry<String, Deck> entry : playerDecksBetter.entrySet()) {
            result.add(entry.getKey());
        }
        return result;
    }


    public List<CardDisplay> getCurrentDeck(String deckName) {
        return playerDecksBetter.get(deckName).getCardsInDeck();
    }


    public void createDeck(String deckName) {
        if(getDecksNames().contains(deckName) == false){
            playerDecksBetter.put(deckName, new Deck(CardsFactory.createAllCards()));
        }
    }




    public boolean isDeckValid(String deckName) {
        Deck deck = playerDecksBetter.get(deckName);
        return deck.getCardsInDeck().size() >= Consts.minDeckSize && deck.getCardsInDeck().size() <= Consts.maxDeckSize;
    }
}
