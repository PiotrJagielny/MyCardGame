package com.example.demo.DuelTests;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Duel.ClientAPI.CardDuel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.demo.TestsData.TestConsts.*;
import static org.junit.jupiter.api.Assertions.*;

class TestDealingCardsBeforePlayingAny {

    CardDuel duel;
    List<CardDisplay> cardsDisplay;


    @BeforeEach
    public void setUp(){

        cardsDisplay = List.of(new CardDisplay("Knight"),new CardDisplay( "Viking"));
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(player1);
        duel.registerPlayerToDuel(player2);
        duel.parseCardsFor(cardsDisplay, player1);
        duel.parseCardsFor(cardsDisplay, player2);
    }

    @Test
    public void afterAddingCardsDisplaysToParse_deckIsFilled() {
        assertEquals(cardsDisplay.size() , duel.getDeckOf(player1).size());
        for(int i = 0 ; i < cardsDisplay.size() ; ++i){
            assertEquals(cardsDisplay.get(i).getName(), duel.getDeckOf(player1).get(i).getName());
        }
    }

    @Test
    public void beforeDealingCards_handIsEmpty(){
        assertTrue(duel.getHandOf(player1).isEmpty());
    }

    @Test
    public void afterDealingCards_handIsNotEmpty(){
        int cardsInDeck_beforeDeal = duel.getDeckOf(player1).size();
        duel.dealCards();
        int cardsInDeck_afterDeal = duel.getDeckOf(player1).size();

        assertNotEquals(cardsInDeck_beforeDeal, cardsInDeck_afterDeal);
        assertFalse(duel.getHandOf(player1).isEmpty());
    }

}