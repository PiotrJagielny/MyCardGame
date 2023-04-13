package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.demo.TestsData.TestConsts.*;

class TestDuelAfterDeal {
    CardDuel duel;
    List<CardDisplay> cardsDisplay;



    @BeforeEach
    public void setUp(){

        cardsDisplay = List.of(new CardDisplay("Knight",5),new CardDisplay( "Viking",7));
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(firstPlayer);
        duel.registerPlayerToDuel(secondPlayer);
        duel.parseCardsFor(cardsDisplay, firstPlayer);
        duel.parseCardsFor(cardsDisplay, secondPlayer);
        duel.dealCards();
        duel.setTurnTo(firstPlayer);
    }

    @Test
    public void beforePlayingCard_boardIsEmpty(){
        assertTrue(duel.getCardsOnBoardDisplayOf(firstPlayer).isEmpty());
    }

    @Test
    public void afterPlayingCard_boardIsNotEmpty(){
        int cardsInHandBeforePlaying = duel.getCardsInHandDisplayOf(firstPlayer).size();
        duel.playCardAs( duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer );
        assertFalse(duel.getCardsOnBoardDisplayOf(firstPlayer).isEmpty());
        assertNotEquals(cardsInHandBeforePlaying, duel.getCardsInHandDisplayOf(firstPlayer).size());
    }

    @Test
    public void clearBoard_hasNoPoints(){
        assertEquals(0, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void afterPlayingCard_boardHasSomePoints(){
        duel.playCardAs( duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer );
        assertNotEquals(0, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void afterSettingFirstPlayerTurn_player2CantPlayCard(){
        duel.playCardAs(duel.getCardsInHandDisplayOf(secondPlayer).get(0), secondPlayer);
        assertTrue(duel.getCardsOnBoardDisplayOf(secondPlayer).isEmpty());
    }

    @Test
    public void afterPlayingCardsWithPoints_theSamePointsAreOnBoard(){
        duel.dealCards();
        CardDisplay cardToPlay = duel.getCardsInHandDisplayOf(firstPlayer).get(0);
        duel.playCardAs(cardToPlay, firstPlayer);
        assertEquals(cardToPlay.getPoints(), duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void afterPlayingCard_turnSwitch(){
        duel.dealCards();

        int expectedCardsOnBoard = 1;
        duel.playCardAs( duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer );
        assertEquals(expectedCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer).size());

        duel.playCardAs( duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer );
        assertEquals(expectedCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer).size());

        duel.playCardAs( duel.getCardsInHandDisplayOf(secondPlayer).get(0), secondPlayer);
        duel.playCardAs( duel.getCardsInHandDisplayOf(secondPlayer).get(0), secondPlayer);
        assertEquals(expectedCardsOnBoard, duel.getCardsOnBoardDisplayOf(secondPlayer).size());

    }

}