package com.example.demo.DeckBuilder;

import com.example.demo.Consts;
import com.example.demo.DeckBuilding.DeckBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestDeckBuilder_OneDeck {

    private DeckBuilder deckBuilder;
    @BeforeEach
    public void init() {
        deckBuilder = new DeckBuilder();
        deckBuilder.createDeck("First");
        deckBuilder.selectDeck("First");
    }

    @Test
    public void testAddingCardToPlayerDeck() {
        int cardsToAdd_BeforeAddingToDeck = deckBuilder.getCardsPossibleToAdd().size();
        deckBuilder.addCardToDeck(deckBuilder.getCardsPossibleToAdd().get(0));
        int cardsToAdd_AfterAddingToDeck = deckBuilder.getCardsPossibleToAdd().size();

        assertEquals(1,deckBuilder.getCurrentDeck().size(), "Deck doens have 1 card after adding ");
        assertEquals(cardsToAdd_AfterAddingToDeck + 1, cardsToAdd_BeforeAddingToDeck, "Card is still in cardsPossibleToAdd after adding to deck ");
    }

    @Test
    public void testAddingBackCardFromDeck() {
        deckBuilder.addCardToDeck(deckBuilder.getCardsPossibleToAdd().get(0));
        deckBuilder.putCardFromDeckBack(deckBuilder.getCurrentDeck().get(0));
        assertTrue(deckBuilder.getCurrentDeck().isEmpty(), "Player deck is not empty after adding and removing card from deck");
    }

    @Test
    public void testAddingCardToFullDeck() {
        for(int i = 1; i <= Consts.MaxDeckSize + 1 ; ++i){
            deckBuilder.addCardToDeck( deckBuilder.getCardsPossibleToAdd().get(0) );
        }
        assertEquals(Consts.MaxDeckSize, deckBuilder.getCurrentDeck().size(), "Can add cards after reaching max cards amount");
    }

    @Test
    public void testImpossibilityOfDeletingLastDeck(){

        deckBuilder.deleteCurrentDeck();
        assertEquals(1, deckBuilder.getDecksNames().size());
    }

    @Test
    public void testPuttingCardBackFromEmptyDeck(){
        int initialAllCardsSize = deckBuilder.getCardsPossibleToAdd().size();
        deckBuilder.putCardFromDeckBack(null);
        int afterPuttingBackAllCardsSize = deckBuilder.getCardsPossibleToAdd().size();
        assertEquals(initialAllCardsSize, afterPuttingBackAllCardsSize);
    }


}