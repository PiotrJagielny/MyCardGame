package com.example.demo.DeckBuilder;

import com.example.demo.DeckBuilding.Services.DeckBuilder;
import com.example.demo.DeckBuilding.Services.DeckBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestDeckBuilder_MultipleDecks {

    private DeckBuilder deckBuilder;
    @BeforeEach
    public void init() {
        deckBuilder = new DeckBuilderService();
        deckBuilder.createDeck("First");
        deckBuilder.createDeck("Second");
        deckBuilder.selectDeck("First");
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
        deckBuilder.addCardToDeck("Thunder");
        assertEquals(deckBuilder.getPlayerDeck().get(0).getDisplay(), "Thunder");

        deckBuilder.selectDeck("Second");
        deckBuilder.addCardToDeck("Knight");
        assertEquals(deckBuilder.getPlayerDeck().get(0).getDisplay(), "Knight");
    }

    @Test
    public void testPuttingCardFromCorrectDeckBack(){
        deckBuilder.addCardToDeck( deckBuilder.getCardsPossibleToAdd().get(0).getDisplay());
        deckBuilder.selectDeck("Second");
        deckBuilder.addCardToDeck( deckBuilder.getCardsPossibleToAdd().get(0).getDisplay());
        deckBuilder.selectDeck("First");
        deckBuilder.putCardFromDeckBack( deckBuilder.getPlayerDeck().get(0).getDisplay() );

        assertEquals(0, deckBuilder.getPlayerDeck().size());
        deckBuilder.selectDeck("Second");
        assertEquals(1, deckBuilder.getPlayerDeck().size());

        deckBuilder.putCardFromDeckBack( deckBuilder.getPlayerDeck().get(0).getDisplay() );
        assertEquals(0, deckBuilder.getPlayerDeck().size());
    }

    @Test
    public void testCardsPossibleToAddWhenSwitchingDecks(){
        int initalCardsPossibleToAdd = deckBuilder.getCardsPossibleToAdd().size();
        deckBuilder.addCardToDeck( deckBuilder.getCardsPossibleToAdd().get(0).getDisplay() );
        deckBuilder.selectDeck("Second");

        assertEquals(initalCardsPossibleToAdd, deckBuilder.getCardsPossibleToAdd().size());
    }

    @Test
    public void testDeckDelete(){

        int decksNumber = deckBuilder.getDecksNames().size();
        deckBuilder.deleteCurrentDeck();
        int decksNumberAfterDelete = deckBuilder.getDecksNames().size();

        assertEquals(1 + decksNumberAfterDelete, decksNumber, "Cant delete a deck");
    }

    @Test
    public void testDeckSelectionAfterDeckDelete(){
        deckBuilder.selectDeck("Second");
        deckBuilder.deleteCurrentDeck();

        assertDoesNotThrow(() -> {
            deckBuilder.getPlayerDeck();
        });
    }

}