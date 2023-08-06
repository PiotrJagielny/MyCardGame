package com.example.demo.DeckBuilder;

import com.example.demo.Consts;
import com.example.demo.DeckBuilding.DeckBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.demo.TestsData.TestConsts.*;
import static org.junit.jupiter.api.Assertions.*;

class TestDeckBuilder_OneDeck {

    private DeckBuilder deckBuilder;
    @BeforeEach
    public void init() {
        deckBuilder = new DeckBuilder();
        deckBuilder.createDeck(firstDeck);
    }

    @Test
    public void testAddingCardToPlayerDeck() {
        int cardsToAdd_BeforeAddingToDeck = deckBuilder.getCardsPossibleToAdd(firstDeck).size();
        deckBuilder.addCardToDeck(deckBuilder.getCardsPossibleToAdd(firstDeck).get(0), firstDeck);
        int cardsToAdd_AfterAddingToDeck = deckBuilder.getCardsPossibleToAdd(firstDeck).size();

        assertEquals(1,deckBuilder.getCurrentDeck(firstDeck).size(), "Deck doens have 1 card after adding ");
        assertEquals(cardsToAdd_AfterAddingToDeck + 1, cardsToAdd_BeforeAddingToDeck, "Card is still in cardsPossibleToAdd after adding to deck ");
    }

    @Test
    public void testAddingBackCardFromDeck() {
        deckBuilder.addCardToDeck(deckBuilder.getCardsPossibleToAdd(firstDeck).get(0), firstDeck);
        deckBuilder.putCardFromDeckBack(deckBuilder.getCurrentDeck(firstDeck).get(0), firstDeck);
        assertTrue(deckBuilder.getCurrentDeck(firstDeck).isEmpty(), "Player deck is not empty after adding and removing card from deck");
    }

    @Test
    public void testAddingCardToFullDeck() {
        for(int i = 1; i <= Consts.MaxDeckSize + 1 ; ++i){
            deckBuilder.addCardToDeck( deckBuilder.getCardsPossibleToAdd(firstDeck).get(0), firstDeck );
        }
        assertEquals(Consts.MaxDeckSize, deckBuilder.getCurrentDeck(firstDeck).size(), "Can add cards after reaching max cards amount");
    }

    @Test
    public void testImpossibilityOfDeletingLastDeck(){

        List<String> decksNames = deckBuilder.getDecksNames();
        for (int i = 0; i < decksNames.size(); i++) {
            deckBuilder.deleteCurrentDeck(decksNames.get(i));
        }
        assertEquals(1, deckBuilder.getDecksNames().size());
    }

    @Test
    public void testPuttingCardBackFromEmptyDeck(){
        int initialAllCardsSize = deckBuilder.getCardsPossibleToAdd(firstDeck).size();
        deckBuilder.putCardFromDeckBack(null, firstDeck);
        int afterPuttingBackAllCardsSize = deckBuilder.getCardsPossibleToAdd(firstDeck).size();
        assertEquals(initialAllCardsSize, afterPuttingBackAllCardsSize);
    }


}