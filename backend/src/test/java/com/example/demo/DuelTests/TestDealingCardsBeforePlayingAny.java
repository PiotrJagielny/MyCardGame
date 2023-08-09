package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.CardDuel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.demo.TestsData.TestConsts.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TestDealingCardsBeforePlayingAny {

    CardDuel duel;
    List<CardDisplay> cardsDisplay;


    @BeforeEach
    public void setUp(){

        cardsDisplay = List.of(new CardDisplay("Knight"),new CardDisplay( "Viking"));
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(firstPlayer);
        duel.registerPlayerToDuel(secondPlayer);
        duel.parseCardsFor(cardsDisplay, firstPlayer);
        duel.parseCardsFor(cardsDisplay, secondPlayer);
    }

    @Test
    public void afterAddingCardsDisplaysToParse_deckIsFilled() {
        assertEquals(cardsDisplay.size() , duel.getDeckOf(firstPlayer).size());
        for(int i = 0 ; i < cardsDisplay.size() ; ++i){
            assertEquals(cardsDisplay.get(i).getName(), duel.getDeckOf(firstPlayer).get(i).getName());
        }
    }

    @Test
    public void beforeDealingCards_handIsEmpty(){
        assertTrue(duel.getHandOf(firstPlayer).isEmpty());
    }

    @Test
    public void afterDealingCards_handIsNotEmpty(){
        int cardsInDeck_beforeDeal = duel.getDeckOf(firstPlayer).size();
        duel.dealCards();
        int cardsInDeck_afterDeal = duel.getDeckOf(firstPlayer).size();

        assertNotEquals(cardsInDeck_beforeDeal, cardsInDeck_afterDeal);
        assertFalse(duel.getHandOf(firstPlayer).isEmpty());
    }

}