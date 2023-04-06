package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.NormalDuel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestDuelAfterDeal {
    CardDuel duel;
    List<CardDisplay> cardsDisplay;
    @BeforeEach
    public void setUp(){
        cardsDisplay = List.of(new CardDisplay("Knight"),new CardDisplay( "Viking"));
        duel = new NormalDuel();
        duel.parseCards(cardsDisplay);
        duel.dealCards();
    }

    @Test
    public void beforePlayingCard_boardIsEmpty(){
        assertTrue(duel.getCardsOnBoardDisplay().isEmpty());
    }

    @Test
    public void afterPlayingCard_boardIsNotEmpty(){
        int cardsInHandBeforePlaying = duel.getPlayerCardsInHandDisplay().size();
        duel.playCard( duel.getPlayerCardsInHandDisplay().get(0) );
        assertFalse(duel.getCardsOnBoardDisplay().isEmpty());
        assertNotEquals(cardsInHandBeforePlaying, duel.getPlayerCardsInHandDisplay().size());
    }

    @Test
    public void clearBoard_hasNoPoints(){
        assertEquals(0, duel.getBoardPoints());
    }

    @Test
    public void afterPlayingCard_boardHasSomePoints(){
        duel.playCard( duel.getPlayerCardsInHandDisplay().get(0) );
        assertNotEquals(0, duel.getBoardPoints());
    }

}