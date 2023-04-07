package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
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
        duel = CardDuel.createDuel();
        duel.parseCards_forPlayer1(cardsDisplay);
        duel.parseCards_forPlayer2(cardsDisplay);
    }

    @Test
    public void afterAddingCardsDisplaysToParse_deckIsFilled() {
        assertEquals(cardsDisplay.size() , duel.getCardsInDeckDisplay_player1().size());
        for(int i = 0 ; i < cardsDisplay.size() ; ++i){
            assertEquals(cardsDisplay.get(i).getName(), duel.getCardsInDeckDisplay_player1().get(i).getName());
        }
    }

    @Test
    public void beforeDealingCards_handIsEmpty(){
        assertTrue(duel.getCardsInHandDisplay_player1().isEmpty());
    }

    @Test
    public void afterDealingCards_handIsNotEmpty(){
        int cardsInDeck_beforeDeal = duel.getCardsInDeckDisplay_player1().size();
        duel.dealCards();
        int cardsInDeck_afterDeal = duel.getCardsInDeckDisplay_player1().size();

        assertNotEquals(cardsInDeck_beforeDeal, cardsInDeck_afterDeal);
        assertFalse(duel.getCardsInHandDisplay_player1().isEmpty());
    }

}