package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.PlayerNumber;
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
        assertEquals(cardsDisplay.size() , duel.getCardsInDeckDisplayOf(firstPlayer).size());
        for(int i = 0 ; i < cardsDisplay.size() ; ++i){
            assertEquals(cardsDisplay.get(i).getName(), duel.getCardsInDeckDisplayOf(firstPlayer).get(i).getName());
        }
    }

    @Test
    public void beforeDealingCards_handIsEmpty(){
        assertTrue(duel.getCardsInHandDisplayOf(firstPlayer).isEmpty());
    }

    @Test
    public void afterDealingCards_handIsNotEmpty(){
        int cardsInDeck_beforeDeal = duel.getCardsInDeckDisplayOf(firstPlayer).size();
        duel.dealCards();
        int cardsInDeck_afterDeal = duel.getCardsInDeckDisplayOf(firstPlayer).size();

        assertNotEquals(cardsInDeck_beforeDeal, cardsInDeck_afterDeal);
        assertFalse(duel.getCardsInHandDisplayOf(firstPlayer).isEmpty());
    }

}