package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.NormalDuel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestCardDuel {

    CardDuel duel;
    List<CardDisplay> cardsDisplay;
    @BeforeEach
    public void setUp(){
        cardsDisplay = List.of(new CardDisplay("Knight"),new CardDisplay( "Viking"));
        duel = new NormalDuel();
    }

    @Test
    public void duelCreation_noWinner() {
        assertFalse(duel.whoWon());
    }

    @Test
    public void emptyDisplaysAfterDuelCreation() {
        assertTrue(duel.getPlayerCardsInDeckDisplay().isEmpty());
    }

    @Test
    public void testCardsParse() {
        duel.parseCards(cardsDisplay);
        assertEquals(cardsDisplay.size() , duel.getPlayerCardsInDeckDisplay().size());
        for(int i = 0 ; i < cardsDisplay.size() ; ++i){
            assertEquals(cardsDisplay.get(i).getName(), duel.getPlayerCardsInDeckDisplay().get(i).getName());
        }
    }

    @Test
    public void testCardsInHandBeforeDeal(){
        duel.parseCards(cardsDisplay);
        assertTrue(duel.getPlayerCardsInHand().isEmpty());
    }

    @Test
    public void testCardsDeal(){
        int initialCardsInDeck = duel.getPlayerCardsInDeckDisplay().size();
        duel.parseCards(cardsDisplay);
        duel.DealCards();
        int cardsInDeckAfterDeal = duel.getPlayerCardsInDeckDisplay().size();

        assertNotEquals(initialCardsInDeck, cardsInDeckAfterDeal);
        assertFalse(duel.getPlayerCardsInHand().isEmpty());
    }

}