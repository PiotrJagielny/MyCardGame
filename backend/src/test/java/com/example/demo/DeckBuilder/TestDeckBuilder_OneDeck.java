package com.example.demo.DeckBuilder;

import com.example.demo.Consts;
import com.example.demo.DeckBuilding.Services.DeckBuilder;
import com.example.demo.DeckBuilding.Services.DeckBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestDeckBuilder_OneDeck {

    private DeckBuilder deckBuilder;
    @BeforeEach
    public void Init() {
        deckBuilder = new DeckBuilderService();
        deckBuilder.CreateDeck("First");
        deckBuilder.SelectDeck("First");
    }

    @Test
    public void TestAddingCardToPlayerDeck() {
        int CardsToAdd_BeforeAddingToDeck = deckBuilder.GetCardsPossibleToAdd().size();
        deckBuilder.AddCardToDeck(deckBuilder.GetCardsPossibleToAdd().get(0).getName());
        int CardsToAdd_AfterAddingToDeck = deckBuilder.GetCardsPossibleToAdd().size();

        assertEquals(1,deckBuilder.GetPlayerDeck().size(), "Deck doens have 1 card after adding ");
        assertEquals(CardsToAdd_AfterAddingToDeck + 1, CardsToAdd_BeforeAddingToDeck, "Card is still in cardsPossibleToAdd after adding to deck ");
    }

    @Test
    public void TestAddingBackCardFromDeck() {
        deckBuilder.AddCardToDeck(deckBuilder.GetCardsPossibleToAdd().get(0).getName());
        deckBuilder.PutCardFromDeckBack(deckBuilder.GetPlayerDeck().get(0).getName());
        assertTrue(deckBuilder.GetPlayerDeck().isEmpty(), "Player deck is not empty after adding and removing card from deck");
    }

    @Test
    public void TestAddingCardToFullDeck() {
        for(int i = 1; i <= Consts.MaxDeckSize + 1 ; ++i){
            deckBuilder.AddCardToDeck( deckBuilder.GetCardsPossibleToAdd().get(0).getName() );
        }
        assertEquals(Consts.MaxDeckSize, deckBuilder.GetPlayerDeck().size(), "Can add cards after reaching max cards amount");
    }

    @Test
    public void TestImpossiblityOfDeletingLastDeck(){

        //Making sure that i cant delete last deck
        deckBuilder.DeleteCurrentDeck();
        deckBuilder.DeleteCurrentDeck();
        deckBuilder.DeleteCurrentDeck();
        assertEquals(1, deckBuilder.GetDecksNames().size());
    }

}