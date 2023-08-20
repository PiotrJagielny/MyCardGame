package com.example.demo.DeckBuilder;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Controllers.DeckBuilding.DeckBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.demo.TestsData.TestConsts.*;

class TestDeckBuilder_MultipleDecks {

    private DeckBuilder deckBuilder;
    @BeforeEach
    public void init() {
        deckBuilder = new DeckBuilder();
        deckBuilder.createDeck(firstDeck);
        deckBuilder.createDeck(secondDeck);
    }

    @Test
    public void testDeckCreation(){
        deckBuilder.createDeck("FirstDeck");
        assertTrue(deckBuilder.getDecksNames().contains("FirstDeck"));
    }

    @Test
    public void testAddingDeckWithExistingName(){

        int decksBeforeCreating = deckBuilder.getDecksNames().size();
        String singleName = "NewDeck";
        deckBuilder.createDeck(singleName);
        deckBuilder.createDeck(singleName);
        int decksAfterCreating = deckBuilder.getDecksNames().size();

        assertEquals(decksAfterCreating, decksBeforeCreating + 1, "there are two decks with same names");
    }

    @Test
    public void testAddingCardsToDifferentDecks(){
        deckBuilder.addCardToDeck(new CardDisplay("Thunder"), firstDeck);
        assertEquals(deckBuilder.getCurrentDeck(firstDeck).get(0).getName(), "Thunder");

        deckBuilder.addCardToDeck(new CardDisplay("Knight"), secondDeck);
        assertEquals(deckBuilder.getCurrentDeck(secondDeck).get(0).getName(), "Knight");
    }

    @Test
    public void testPuttingCardFromCorrectDeckBack(){
        deckBuilder.addCardToDeck( deckBuilder.getCardsPossibleToAdd(firstDeck).get(0),firstDeck);
        deckBuilder.addCardToDeck( deckBuilder.getCardsPossibleToAdd(secondDeck).get(0), secondDeck);
        deckBuilder.putCardFromDeckBack( deckBuilder.getCurrentDeck(secondDeck).get(0), secondDeck );

        assertEquals(0, deckBuilder.getCurrentDeck(secondDeck).size());
        assertEquals(1, deckBuilder.getCurrentDeck(firstDeck).size());

        deckBuilder.putCardFromDeckBack( deckBuilder.getCurrentDeck(firstDeck).get(0), firstDeck);
        assertEquals(0, deckBuilder.getCurrentDeck(firstDeck).size());
    }

    @Test
    public void testCardsPossibleToAddWhenSwitchingDecks(){
        int initialCardsPossibleToAdd = deckBuilder.getCardsPossibleToAdd(firstDeck).size();
        deckBuilder.addCardToDeck( deckBuilder.getCardsPossibleToAdd(firstDeck).get(0) , firstDeck);

        assertEquals(initialCardsPossibleToAdd, deckBuilder.getCardsPossibleToAdd(secondDeck).size());
    }

    @Test
    public void testDeckDelete(){

        int decksNumber = deckBuilder.getDecksNames().size();
        deckBuilder.deleteCurrentDeck(firstDeck);
        int decksNumberAfterDelete = deckBuilder.getDecksNames().size();

        assertEquals(1 + decksNumberAfterDelete, decksNumber, "Cant delete a deck");
    }

}