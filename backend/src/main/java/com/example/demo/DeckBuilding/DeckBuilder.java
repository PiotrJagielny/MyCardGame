package com.example.demo.DeckBuilding;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Consts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DeckBuilder {
    private String selectedDeck;
    private List<Deck> playerDecks;


    public DeckBuilder() {
        playerDecks = new ArrayList<Deck>();

        playerDecks.add(new Deck(CardsFactory.createAllCards(),"Deck"));
        selectedDeck = "Deck";
        addCardToDeck(CardsFactory.paper);
        addCardToDeck(CardsFactory.armageddon);
        addCardToDeck(CardsFactory.viking);
    }


    public List<CardDisplay> getCardsPossibleToAdd() {
        return playerDecks.get( GetSelectedDeckIndex() ).getCardsPossibleToAdd();
    }


    public String addCardToDeck(CardDisplay cardDisplay) {
        return playerDecks.get( GetSelectedDeckIndex() ).addCard(cardDisplay);

    }


    public void putCardFromDeckBack(CardDisplay cardDisplay) {
        playerDecks.get(GetSelectedDeckIndex()).putCardFromDeckBack(cardDisplay);

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

    private Boolean isAbleToDeleteDeck(){return playerDecks.size() != 1;}

    private void deleteDeck(){
        playerDecks.removeIf(d -> d.getDeckName().equals(selectedDeck));
        selectedDeck = getDecksNames().get(0);
    }



    public List<String> getDecksNames() {
        List<String> result = new ArrayList<String>();

        for(int i = 0; i < playerDecks.size() ; ++i){
            result.add(playerDecks.get(i).getDeckName());
        }

        return result;
    }


    public List<CardDisplay> getPlayerDeck() {
        return playerDecks.get(GetSelectedDeckIndex()).getCardsInDeck();
    }


    public void createDeck(String deckName) {
        if(getDecksNames().contains(deckName) == false)
            playerDecks.add(new Deck(CardsFactory.createAllCards(), deckName));
    }


    public void selectDeck(String deckName) {
        selectedDeck = deckName;
    }

    private int GetSelectedDeckIndex() {
        int deckIndex = IntStream.range(0, playerDecks.size()).filter(i -> selectedDeck.equals(playerDecks.get(i).getDeckName())).findFirst().orElse(-1);
        return deckIndex;
    }

    public boolean isDeckValid(String deckName) {
        Deck deck = playerDecks.stream().filter(d -> d.getDeckName().equals(deckName)).findFirst().orElse(new Deck());
        return deck.getCardsInDeck().size() >= Consts.minDeckSize && deck.getCardsInDeck().size() <= Consts.maxDeckSize;
    }
}
