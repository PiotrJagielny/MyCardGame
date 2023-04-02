package com.example.demo.DeckBuilder;

import com.example.demo.DeckBuilding.Services.DeckBuilder;
import com.example.demo.DeckBuilding.Services.DeckBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestDeckBuilder_MultipleDecks {

    private DeckBuilder deckBuilder;
    @BeforeEach
    public void Init() {
        deckBuilder = new DeckBuilderService();
        deckBuilder.CreateDeck("First");
        deckBuilder.CreateDeck("Second");
        deckBuilder.SelectDeck("First");
    }

    @Test
    public void TestDeckCreation(){
        deckBuilder.CreateDeck("FirstDeck");
        assertTrue(deckBuilder.GetDecksNames().contains("FirstDeck"));
    }

    @Test
    public void TestAddingDeckWithExistingName(){

        int DecksBeforeCreating = deckBuilder.GetDecksNames().size();
        String singleName = "NewDeck";
        deckBuilder.CreateDeck(singleName);
        deckBuilder.CreateDeck(singleName);
        int DecksAfterCreating = deckBuilder.GetDecksNames().size();

        assertEquals(DecksAfterCreating, DecksBeforeCreating + 1, "there are two decks with same names");
    }

    @Test
    public void TestAddingCardsToDifferentDecks(){
        deckBuilder.AddCardToDeck("Thunder");
        assertEquals(deckBuilder.GetPlayerDeck().get(0).getName(), "Thunder");

        deckBuilder.SelectDeck("Second");
        deckBuilder.AddCardToDeck("Knight");
        assertEquals(deckBuilder.GetPlayerDeck().get(0).getName(), "Knight");
    }

    @Test
    public void TestPuttingCardFromCorrectDeckBack(){
        deckBuilder.AddCardToDeck( deckBuilder.GetCardsPossibleToAdd().get(0).getName());
        deckBuilder.SelectDeck("Second");
        deckBuilder.AddCardToDeck( deckBuilder.GetCardsPossibleToAdd().get(0).getName());
        deckBuilder.SelectDeck("First");
        deckBuilder.PutCardFromDeckBack( deckBuilder.GetPlayerDeck().get(0).getName() );

        assertEquals(0, deckBuilder.GetPlayerDeck().size());
        deckBuilder.SelectDeck("Second");
        assertEquals(1, deckBuilder.GetPlayerDeck().size());

        deckBuilder.PutCardFromDeckBack( deckBuilder.GetPlayerDeck().get(0).getName() );
        assertEquals(0, deckBuilder.GetPlayerDeck().size());
    }

    @Test
    public void TestCardsPossibleToAddWhenSwitchingDecks(){
        int InitalCardsPossibleToAdd = deckBuilder.GetCardsPossibleToAdd().size();
        deckBuilder.AddCardToDeck( deckBuilder.GetCardsPossibleToAdd().get(0).getName() );
        deckBuilder.SelectDeck("Second");

        assertEquals(InitalCardsPossibleToAdd, deckBuilder.GetCardsPossibleToAdd().size());
    }

    @Test
    public void TestDeckDelete(){

        int DecksNumber = deckBuilder.GetDecksNames().size();
        deckBuilder.DeleteCurrentDeck();
        int DecksNumberAfterDelete = deckBuilder.GetDecksNames().size();

        assertEquals(1 + DecksNumberAfterDelete, DecksNumber, "Cant delete a deck");
    }

    @Test
    public void TestDeckSelectionAfterDeckDelete(){
        deckBuilder.SelectDeck("Second");
        deckBuilder.DeleteCurrentDeck();

        assertDoesNotThrow(() -> {
            deckBuilder.GetPlayerDeck();
        });
    }

}