package com.example.demo.DeckBuilder;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.CardsFactory;
import com.example.demo.Consts;
import com.example.demo.DeckBuilding.DeckBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.demo.TestsData.TestConsts.*;
import static org.junit.jupiter.api.Assertions.*;

class TestDeckBuilder {

    private DeckBuilder deckBuilder;
    @BeforeEach
    public void init() {
        deckBuilder = new DeckBuilder();
        deckBuilder.createDeck(firstDeck, Consts.Fraction.humans);
        deckBuilder.createDeck(secondDeck, Consts.Fraction.humans);
    }

    @Test
    public void testDeckCreation(){
        deckBuilder.createDeck("FirstDeck", Consts.Fraction.humans);
        assertTrue(deckBuilder.getDecksNames().contains("FirstDeck"));
    }

    @Test
    public void testAddingDeckWithExistingName(){

        int decksBeforeCreating = deckBuilder.getDecksNames().size();
        String singleName = "NewDeck";
        deckBuilder.createDeck(singleName, Consts.Fraction.humans);
        deckBuilder.createDeck(singleName, Consts.Fraction.humans);
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

        decksNumber = deckBuilder.getDecksNames().size();
        deckBuilder.deleteCurrentDeck(secondDeck);
        decksNumberAfterDelete = deckBuilder.getDecksNames().size();
        assertEquals(decksNumber,decksNumberAfterDelete);

    }

    @Test
    public void testAddingCardToFullDeck() {
        for(int i = 1; i <= Consts.maxDeckSize+ 1 ; ++i){
            deckBuilder.addCardToDeck( deckBuilder.getCardsPossibleToAdd(firstDeck).get(0), firstDeck );
        }
        assertEquals(Consts.maxDeckSize, deckBuilder.getCurrentDeck(firstDeck).size(), "Can add cards after reaching max cards amount");
    }

    @Test
    public void testDifferentFractionDecks() {
        deckBuilder.createDeck("nowy", Consts.Fraction.monsters);
        CardDisplay humansCard = CardsFactory.createAllCards().stream()
                .map(c -> c.getDisplay())
                .filter(c -> c.getFraction().equals(Consts.Fraction.humans))
                .findFirst().orElse(new CardDisplay());

        CardDisplay monsterCard = CardsFactory.createAllCards().stream()
                .map(c -> c.getDisplay())
                .filter(c -> c.getFraction().equals(Consts.Fraction.monsters))
                .findFirst().orElse(new CardDisplay());

        CardDisplay neutralCard= CardsFactory.createAllCards().stream()
                .map(c -> c.getDisplay())
                .filter(c -> c.getFraction().equals(Consts.Fraction.neutral))
                .findFirst().orElse(new CardDisplay());

        assertFalse(deckBuilder.getCardsPossibleToAdd("nowy").contains(humansCard));
        assertTrue(deckBuilder.getCardsPossibleToAdd("nowy").contains(monsterCard));
        assertTrue(deckBuilder.getCardsPossibleToAdd("nowy").contains(neutralCard));

    }

}