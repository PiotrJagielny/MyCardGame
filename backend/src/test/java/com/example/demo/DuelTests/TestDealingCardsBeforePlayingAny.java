package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.NormalDuel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TestDealingCardsBeforePlayingAny {

    CardDuel duel;
    List<CardDisplay> cardsDisplay;
    @BeforeEach
    public void setUp(){
        cardsDisplay = List.of(new CardDisplay("Knight"),new CardDisplay( "Viking"));
        duel = new NormalDuel();
        duel.parseCards(cardsDisplay);
    }

    @Test
    public void afterAddingCardsDisplaysToParse_deckIsFilled() {
        assertEquals(cardsDisplay.size() , duel.getPlayerCardsInDeckDisplay().size());
        for(int i = 0 ; i < cardsDisplay.size() ; ++i){
            assertEquals(cardsDisplay.get(i).getName(), duel.getPlayerCardsInDeckDisplay().get(i).getName());
        }
    }

    @Test
    public void beforeDealingCards_handIsEmpty(){
        assertTrue(duel.getPlayerCardsInHandDisplay().isEmpty());
    }

    @Test
    public void afterDealingCards_handIsNotEmpty(){
        int cardsInDeck_beforeDeal = duel.getPlayerCardsInDeckDisplay().size();
        duel.dealCards();
        int cardsInDeck_afterDeal = duel.getPlayerCardsInDeckDisplay().size();

        assertNotEquals(cardsInDeck_beforeDeal, cardsInDeck_afterDeal);
        assertFalse(duel.getPlayerCardsInHandDisplay().isEmpty());
    }

}