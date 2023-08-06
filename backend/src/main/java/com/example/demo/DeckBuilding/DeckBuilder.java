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
    private String selectedDeck;
    private List<Deck> playerDecks;
    private Map<String, Deck> playerDecksBetter;


    public DeckBuilder() {
        playerDecks = new ArrayList<Deck>();

        playerDecks.add(new Deck(CardsFactory.createAllCards(),"Deck"));
        playerDecksBetter = new HashMap<>();
        playerDecksBetter.put("Deck", new Deck(CardsFactory.createAllCards()));
        selectedDeck = "Deck";
    }


    public List<CardDisplay> getCardsPossibleToAdd() {
        return playerDecks.get( GetSelectedDeckIndex() ).getCardsPossibleToAdd();
    }


    public String addCardToDeck(CardDisplay cardDisplay) {
        return playerDecks.get( GetSelectedDeckIndex() ).addCard(cardDisplay);

    }
    public String addCardToDeck(CardDisplay card, String deckName) {
        return playerDecksBetter.get( deckName ).addCard(card);
    }


    public void putCardFromDeckBack(CardDisplay cardDisplay) {
        playerDecks.get(GetSelectedDeckIndex()).putCardFromDeckBack(cardDisplay);
    }
    public void putCardFromDeckBack(CardDisplay cardDisplay, String deckName) {
        playerDecksBetter.get(deckName).putCardFromDeckBack(cardDisplay);
    }


    public String deleteCurrentDeck() {
        String responseMessage = "";
        if(isAbleToDeleteDeck() == false){
            responseMessage = "Cannot delete last deck";
            return responseMessage;
        }
        deleteDeck();
        return responseMessage;
    }
    public String deleteCurrentDeck(String deckName) {
        String responseMessage = "";
        if(isAbleToDeleteDeck() == false){
            responseMessage = "Cannot delete last deck";
            return responseMessage;
        }
        deleteDeck(deckName);
        return responseMessage;
    }

    private Boolean isAbleToDeleteDeck(){return playerDecks.size() != 1;}

    private void deleteDeck(){
        playerDecks.removeIf(d -> d.getDeckName().equals(selectedDeck));
        selectedDeck = getDecksNames().get(0);
    }
    private void deleteDeck(String deckName) {
        playerDecks.removeIf(d -> d.getDeckName().equals(deckName));
        selectedDeck = getDecksNames().get(0);
    }



    public List<String> getDecksNames() {
        List<String> result = new ArrayList<String>();

        for(int i = 0; i < playerDecks.size() ; ++i){
            result.add(playerDecks.get(i).getDeckName());
        }

        return result;
    }


    public List<CardDisplay> getCurrentDeck() {
        return playerDecks.get(GetSelectedDeckIndex()).getCardsInDeck();
    }


    public void createDeck(String deckName) {
        if(getDecksNames().contains(deckName) == false){
            playerDecks.add(new Deck(CardsFactory.createAllCards(), deckName));
            playerDecksBetter.put(deckName, new Deck(CardsFactory.createAllCards()));
        }
    }


    public void selectDeck(String deckName) {
        selectedDeck = deckName;
    }
    public String getCurrentDeckName() {return selectedDeck;}

    private int GetSelectedDeckIndex() {
        int deckIndex = IntStream.range(0, playerDecks.size()).filter(i -> selectedDeck.equals(playerDecks.get(i).getDeckName())).findFirst().orElse(-1);
        return deckIndex;
    }

    public boolean isDeckValid(String deckName) {
        Deck deck = playerDecks.stream().filter(d -> d.getDeckName().equals(deckName)).findFirst().orElse(new Deck());
        return deck.getCardsInDeck().size() >= Consts.minDeckSize && deck.getCardsInDeck().size() <= Consts.maxDeckSize;
    }
}
