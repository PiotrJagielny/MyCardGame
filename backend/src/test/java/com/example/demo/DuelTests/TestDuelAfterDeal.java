package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.PlayerNumber;
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
        duel.parseCardsFor(cardsDisplay, PlayerNumber.FirstPlayer);
        duel.parseCardsFor(cardsDisplay, PlayerNumber.SecondPlayer);
        duel.dealCards();
    }

    @Test
    public void beforePlayingCard_boardIsEmpty(){
        assertTrue(duel.getCardsOnBoardDisplayOf(PlayerNumber.FirstPlayer).isEmpty());
    }

    @Test
    public void afterPlayingCard_boardIsNotEmpty(){
        int cardsInHandBeforePlaying = duel.getCardsInHandDisplayOf(PlayerNumber.FirstPlayer).size();
        duel.playCardAs( duel.getCardsInHandDisplayOf(PlayerNumber.FirstPlayer).get(0), PlayerNumber.FirstPlayer );
        assertFalse(duel.getCardsOnBoardDisplayOf(PlayerNumber.FirstPlayer).isEmpty());
        assertNotEquals(cardsInHandBeforePlaying, duel.getCardsInHandDisplayOf(PlayerNumber.FirstPlayer).size());
    }

    @Test
    public void clearBoard_hasNoPoints(){
        assertEquals(0, duel.getBoardPointsOf(PlayerNumber.FirstPlayer));
    }

    @Test
    public void afterPlayingCard_boardHasSomePoints(){
        duel.playCardAs( duel.getCardsInHandDisplayOf(PlayerNumber.FirstPlayer).get(0), PlayerNumber.FirstPlayer );
        assertNotEquals(0, duel.getBoardPointsOf(PlayerNumber.FirstPlayer));
    }

    @Test
    public void afterSettingFirstPlayerTurn_player2CantPlayCard(){
        duel.setTurnTo(PlayerNumber.FirstPlayer);
        duel.playCardAs(duel.getCardsInHandDisplayOf(PlayerNumber.SecondPlayer).get(0), PlayerNumber.SecondPlayer);
        assertTrue(duel.getCardsOnBoardDisplayOf(PlayerNumber.SecondPlayer).isEmpty());
    }

}