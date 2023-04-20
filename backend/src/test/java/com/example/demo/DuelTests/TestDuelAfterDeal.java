package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Consts;
import com.example.demo.Duel.Services.CardDuel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.demo.TestsData.TestConsts.*;

class TestDuelAfterDeal {
    CardDuel duel;
    List<CardDisplay> cardsDisplay;


    @BeforeEach
    public void setUp(){

        cardsDisplay = List.of(new CardDisplay("Knight",5),new CardDisplay( "Viking",7), new CardDisplay("which", 6));
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(secondPlayer);
        duel.registerPlayerToDuel(firstPlayer);
        duel.parseCardsFor(cardsDisplay, firstPlayer);
        duel.parseCardsFor(cardsDisplay, secondPlayer);
        duel.dealCards();
    }

    @Test
    public void beforePlayingCard_boardIsEmpty(){
        assertTrue(duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterPlayingCard_boardIsNotEmpty(){
        int cardsInHandBeforePlaying = duel.getCardsInHandDisplayOf(firstPlayer).size();
        duel.playCardAs( duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer, Consts.firstRow );
        assertFalse(duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).isEmpty());
        assertNotEquals(cardsInHandBeforePlaying, duel.getCardsInHandDisplayOf(firstPlayer).size());
    }

    @Test
    public void clearBoard_hasNoPoints(){
        assertEquals(0, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void onFirstPlayerTurn_player2CantPlayCard(){
        duel.playCardAs(duel.getCardsInHandDisplayOf(secondPlayer).get(0), secondPlayer, Consts.firstRow);
        assertTrue(duel.getCardsOnBoardDisplayOf(secondPlayer, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterPlayingCardsWithPoints_theSamePointsAreOnBoard(){
        duel.dealCards();
        CardDisplay cardToPlay_first = duel.getCardsInHandDisplayOf(firstPlayer).get(0);
        CardDisplay cardToPlay_second = duel.getCardsInHandDisplayOf(firstPlayer).get(1);
        duel.playCardAs(cardToPlay_first, firstPlayer, Consts.firstRow);
        duel.playCardAs(duel.getCardsInHandDisplayOf(secondPlayer).get(0), secondPlayer, Consts.firstRow);
        duel.playCardAs(cardToPlay_second, firstPlayer, Consts.secondRow);
        assertEquals(cardToPlay_first.getPoints() + cardToPlay_second.getPoints(), duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void afterPlayingCard_cantPlayAnother(){
        duel.dealCards();

        int expectedCardsOnBoard = 1;
        duel.playCardAs( duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer, Consts.firstRow );
        assertEquals(expectedCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size());

        duel.playCardAs( duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer, Consts.firstRow );
        assertEquals(expectedCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size());

        duel.playCardAs( duel.getCardsInHandDisplayOf(secondPlayer).get(0), secondPlayer, Consts.firstRow);
        duel.playCardAs( duel.getCardsInHandDisplayOf(secondPlayer).get(0), secondPlayer, Consts.firstRow);
        assertEquals(expectedCardsOnBoard, duel.getCardsOnBoardDisplayOf(secondPlayer, Consts.firstRow).size());
    }

    @Test
    public void playerCanEndRound(){
        duel.endRoundFor(firstPlayer);
        assertTrue(duel.didEndRound(firstPlayer));
    }

    @Test
    public void atStartOfGame_nobodyEndedRound(){
        assertFalse(duel.didEndRound(firstPlayer));
        assertFalse(duel.didEndRound(secondPlayer));
    }

    @Test
    public void afterOnePlayerEndsRound_turnsDontSwitch() {
        duel.dealCards();
        duel.dealCards();
        duel.playCardAs(duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer, Consts.firstRow);
        duel.endRoundFor(secondPlayer);
        duel.playCardAs(duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer, Consts.firstRow);
        duel.playCardAs(duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer, Consts.firstRow);
        int expectedNumberOfCardsOnBoard = 3;
        assertEquals(expectedNumberOfCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size());
    }

    @Test
    public void afterEndingRound_turnsSwitch(){
        duel.dealCards();
        duel.playCardAs(duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer, Consts.firstRow);
        duel.endRoundFor(secondPlayer);
        duel.playCardAs(duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer, Consts.firstRow);
        assertEquals(2, duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size());
    }

    @Test
    public void afterTwoPlayerEndRound_newRoundStarts(){
        startNextRound_secondPlayerFirst();
        assertFalse(duel.didEndRound(firstPlayer));
        assertFalse(duel.didEndRound(secondPlayer));
    }

    @Test
    public void afterEndingFirstRound_newCardsAreDealt() {
        int cardsInHandBeforeNewTurn = duel.getCardsInHandDisplayOf(firstPlayer).size();
        startNextRound_secondPlayerFirst();
        int cardsInHandAfterNewTurn = duel.getCardsInHandDisplayOf(firstPlayer).size();
        assertNotEquals(cardsInHandAfterNewTurn, cardsInHandBeforeNewTurn);
    }

    @Test
    public void atTheGameStart_noOneWonRound(){
        assertEquals(0, duel.getWonRoundsOf(firstPlayer));
        assertEquals(0, duel.getWonRoundsOf(secondPlayer));
    }

    @Test
    public void afterWinningTurn_onePlayerScoresPoints(){
        playCardAsFirstPlayer_startNextRound();
        assertEquals(1, duel.getWonRoundsOf(firstPlayer));
        assertEquals(0, duel.getWonRoundsOf(secondPlayer));
    }

    @Test
    public void afterNewRound_boardIsEmpty(){
        playCardAsFirstPlayer_startNextRound();
        assertTrue(duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterTwoRoundsWon_playerWonGame(){
        playCardAsFirstPlayer_startNextRound();
        playCardAsFirstPlayer_startNextRound();
        assertTrue(duel.didWon(firstPlayer));
    }

    @Test
    public void afterWonGame_cantPlayCard(){
        playCardAsFirstPlayer_startNextRound();
        playCardAsFirstPlayer_startNextRound();
        int cardsOnBoard_beforePlay = duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size();
        duel.playCardAs( duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer , Consts.firstRow);
        int cardsOnBoard_afterPlay = duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size();
        assertEquals(cardsOnBoard_afterPlay, cardsOnBoard_beforePlay);
    }

    public void playCardAsFirstPlayer_startNextRound(){
        duel.playCardAs(duel.getCardsInHandDisplayOf(firstPlayer).get(0), firstPlayer, Consts.firstRow);
        startNextRound_secondPlayerFirst();
    }

    public void startNextRound_secondPlayerFirst(){
        duel.endRoundFor(secondPlayer);
        duel.endRoundFor(firstPlayer);
    }

    @Test
    public void testCalculatingWonRounds(){
        duel.dealCards();
        CardDisplay morePointsCard = Collections.max(
                duel.getCardsInHandDisplayOf(firstPlayer), Comparator.comparingInt(CardDisplay::getPoints)
        );
        CardDisplay lessPointsCard = Collections.min(
                duel.getCardsInHandDisplayOf(firstPlayer), Comparator.comparingInt(CardDisplay::getPoints)
        );
        duel.playCardAs(morePointsCard, firstPlayer, Consts.firstRow);
        duel.playCardAs(lessPointsCard, secondPlayer, Consts.firstRow);
        duel.endRoundFor(firstPlayer);
        duel.endRoundFor(secondPlayer);

        assertEquals(1, duel.getWonRoundsOf(firstPlayer));
        assertEquals(0, duel.getWonRoundsOf(secondPlayer));
    }


}