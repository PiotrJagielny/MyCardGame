package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
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
        duel = CardDuel.createDuel();
        duel.parseCards_forPlayer1(cardsDisplay);
        duel.parseCards_forPlayer2(cardsDisplay);
        duel.dealCards();
    }

    @Test
    public void beforePlayingCard_boardIsEmpty(){
        assertTrue(duel.getCardsOnBoardDisplay_player1().isEmpty());
    }

    @Test
    public void afterPlayingCard_boardIsNotEmpty(){
        int cardsInHandBeforePlaying = duel.getCardsInHandDisplay_player1().size();
        duel.playCard_asPlayer1( duel.getCardsInHandDisplay_player1().get(0) );
        assertFalse(duel.getCardsOnBoardDisplay_player1().isEmpty());
        assertNotEquals(cardsInHandBeforePlaying, duel.getCardsInHandDisplay_player1().size());
    }

    @Test
    public void clearBoard_hasNoPoints(){
        assertEquals(0, duel.getBoardPoints_player1());
    }

    @Test
    public void afterPlayingCard_boardHasSomePoints(){
        duel.playCard_asPlayer1( duel.getCardsInHandDisplay_player1().get(0) );
        assertNotEquals(0, duel.getBoardPoints_player1());
    }

}