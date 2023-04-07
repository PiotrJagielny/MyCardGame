package com.example.demo.DeckBuilding.Services;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Deck;
import com.example.demo.CardsServices.Factory.CardsFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class DeckBuilderService implements DeckBuilder {

    private String selectedDeck;
    private List<Deck> playerDecks;

    private CardsFactory factory;

    public DeckBuilderService() {
        playerDecks = new ArrayList<Deck>();
        factory = new CardsFactory();

        playerDecks.add(new Deck(factory.createAllCards(),"Deck"));
        selectedDeck = "Deck";
    }

    @Override
    public List<CardDisplay> getCardsPossibleToAdd() {
        return playerDecks.get( GetSelectedDeckIndex() ).getCardsPossibleToAdd();
    }

    @Override
    public String addCardToDeck(CardDisplay cardDisplay) {
        return playerDecks.get( GetSelectedDeckIndex() ).addCard(cardDisplay);

    }

    @Override
    public void putCardFromDeckBack(CardDisplay cardDisplay) {
        playerDecks.get(GetSelectedDeckIndex()).putCardFromDeckBack(cardDisplay);

    }

    @Override
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


    @Override
    public List<String> getDecksNames() {
        List<String> result = new ArrayList<String>();

        for(int i = 0; i < playerDecks.size() ; ++i){
            result.add(playerDecks.get(i).getDeckName());
        }

        return result;
    }

    @Override
    public List<CardDisplay> getPlayerDeck() {
        return playerDecks.get(GetSelectedDeckIndex()).getCardsInDeck();
    }

    @Override
    public void createDeck(String deckName) {
        if(getDecksNames().contains(deckName) == false)
            playerDecks.add(new Deck(factory.createAllCards(), deckName));
    }

    @Override
    public void selectDeck(String deckName) {
        selectedDeck = deckName;
    }

    private int GetSelectedDeckIndex() {
        int deckIndex = IntStream.range(0, playerDecks.size()).filter(i -> selectedDeck.equals(playerDecks.get(i).getDeckName())).findFirst().orElse(-1);
        return deckIndex;
    }

}
