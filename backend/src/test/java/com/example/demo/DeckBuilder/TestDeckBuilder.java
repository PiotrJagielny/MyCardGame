package com.example.demo.DeckBuilder;

import com.example.demo.Consts;
import com.example.demo.DeckBuilding.Services.DeckBuilder;
import com.example.demo.DeckBuilding.Services.DeckBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestDeckBuilder {

    private DeckBuilder deckBuilder;
    @BeforeEach
    public void Init() {
        deckBuilder = new DeckBuilderService();
    }

    @Test
    public void TestDeckBuilder() {

        assertNotEquals(null,deckBuilder, "New class failed to instaincate");
    }

    @Test
    public void TestAddingCardToPlayerDeck() {

        int PossibleCardsToAddBeforeAddingToDeck = deckBuilder.GetCardsPossibleToAdd().size();
        deckBuilder.AddCardToDeck(deckBuilder.GetCardsPossibleToAdd().get(0).getName());
        int PossibleCardsToAddAfterAddingToDeck = deckBuilder.GetCardsPossibleToAdd().size();

        assertEquals(1,deckBuilder.GetPlayerDeck().size(), "Deck doens have 1 card after adding ");
        assertEquals(PossibleCardsToAddAfterAddingToDeck + 1, PossibleCardsToAddBeforeAddingToDeck, "Card is still in cardsPossibleToAdd after adding to deck ");
    }

    @Test
    public void TestAddingBackCardFromDeck() {

        deckBuilder.AddCardToDeck(deckBuilder.GetCardsPossibleToAdd().get(0).getName());
        deckBuilder.PutCardFromDeckBack(deckBuilder.GetPlayerDeck().get(0).getName());

        assertTrue(deckBuilder.GetPlayerDeck().isEmpty(), "Player deck is not empty affter adding and removing card from deck");
    }

    @Test
    public void TestAddingCardToFullDeck() {

        for(int i = 1; i <= Consts.MaxDeckSize +1 ; ++i){
            deckBuilder.AddCardToDeck( deckBuilder.GetCardsPossibleToAdd().get(0).getName() );
        }
        String ResponseMessage = deckBuilder.AddCardToDeck( deckBuilder.GetCardsPossibleToAdd().get(0).getName() );
        assertEquals(Consts.MaxDeckSize, deckBuilder.GetPlayerDeck().size(), "Can add cards after reaching max cards amount");
        assertEquals(Consts.DeckFullMessage, ResponseMessage, "Can add cards after reaching max cards amount");
    }

    @Test
    public void TestDeckCreation(){
        
        deckBuilder.CreateDeck("FirstDeck");
        assertEquals(List.of("FirstDeck"), deckBuilder.GetDecksNames());
    }

}