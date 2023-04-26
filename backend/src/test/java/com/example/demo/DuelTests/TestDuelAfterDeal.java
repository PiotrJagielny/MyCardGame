package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.CardsServices.CardsParser;
import com.example.demo.Consts;
import com.example.demo.Duel.DataStructures.PlayerPlay;
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


    @BeforeEach
    public void setUp(){
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(secondPlayer);
        duel.registerPlayerToDuel(firstPlayer);
        duel.parseCardsFor(CardsParser.getCardsDisplay( CardsFactory.createAllCards() ), firstPlayer);
        duel.parseCardsFor(CardsParser.getCardsDisplay( CardsFactory.createAllCards() ), secondPlayer);
        duel.dealCards();
    }

    public void playCard(CardDisplay playedCard, int onRow, String player){
        duel.playCardAs(new PlayerPlay(playedCard, onRow), player);
    }

    @Test
    public void beforePlayingCard_boardIsEmpty(){
        assertTrue(duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterPlayingCard_boardIsNotEmpty(){
        int cardsInHandBeforePlaying = duel.getCardsInHandDisplayOf(firstPlayer).size();
        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        assertFalse(duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).isEmpty());
        assertNotEquals(cardsInHandBeforePlaying, duel.getCardsInHandDisplayOf(firstPlayer).size());
    }

    @Test
    public void clearBoard_hasNoPoints(){
        assertEquals(0, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void onFirstPlayerTurn_player2CantPlayCard(){
        playCard(duel.getCardsInHandDisplayOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
        assertTrue(duel.getCardsOnBoardDisplayOf(secondPlayer, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterPlayingCardsWithPoints_theSamePointsAreOnBoard(){
        CardDisplay cardToPlay_first = duel.getCardsInHandDisplayOf(firstPlayer).get(0);
        CardDisplay cardToPlay_second = duel.getCardsInHandDisplayOf(firstPlayer).get(1);
        playCard(cardToPlay_first, Consts.firstRow, firstPlayer);
        playCard(duel.getCardsInHandDisplayOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
        playCard(cardToPlay_second, Consts.secondRow, firstPlayer);
        assertEquals(cardToPlay_first.getPoints() + cardToPlay_second.getPoints(), duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void afterPlayingCard_cantPlayAnother(){
        int expectedCardsOnBoard = 1;
        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        assertEquals(expectedCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size());

        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        assertEquals(expectedCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size());

        playCard(duel.getCardsInHandDisplayOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
        playCard(duel.getCardsInHandDisplayOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
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
        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        duel.endRoundFor(secondPlayer);
        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        int expectedNumberOfCardsOnBoard = 3;
        assertEquals(expectedNumberOfCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size());
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
        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.secondRow, firstPlayer);
        duel.endRoundFor(secondPlayer);
        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.thirdRow, firstPlayer);
        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        duel.endRoundFor(firstPlayer);
        for(int row = 0 ; row < Consts.rowsNumber ; ++row){
            assertTrue(duel.getCardsOnBoardDisplayOf(firstPlayer, row).isEmpty());
        }
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
        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        int cardsOnBoard_afterPlay = duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size();
        assertEquals(cardsOnBoard_afterPlay, cardsOnBoard_beforePlay);
    }

    public void playCardAsFirstPlayer_startNextRound(){
        playCard(duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        startNextRound_secondPlayerFirst();
    }

    public void startNextRound_secondPlayerFirst(){
        duel.endRoundFor(secondPlayer);
        duel.endRoundFor(firstPlayer);
    }

    @Test
    public void testCalculatingWonRounds(){
        CardDisplay morePointsCard = Collections.max(
                duel.getCardsInHandDisplayOf(firstPlayer), Comparator.comparingInt(CardDisplay::getPoints)
        );
        CardDisplay lessPointsCard = Collections.min(
                duel.getCardsInHandDisplayOf(firstPlayer), Comparator.comparingInt(CardDisplay::getPoints)
        );
        playCard(morePointsCard, Consts.firstRow, firstPlayer);
        playCard(lessPointsCard, Consts.firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);
        duel.endRoundFor(secondPlayer);

        assertEquals(1, duel.getWonRoundsOf(firstPlayer));
        assertEquals(0, duel.getWonRoundsOf(secondPlayer));
    }

    @Test
    public void afterTie_bothPlayersScoresPoint(){
        CardDisplay theSamePoints_firstPlayer = duel.getCardsInHandDisplayOf(firstPlayer).get(0);
        CardDisplay theSamePoints_secondPlayer = duel.getCardsInHandDisplayOf(secondPlayer).stream()
                .filter(c -> c.getPoints() == theSamePoints_firstPlayer.getPoints())
                .findFirst().orElse(null);
        playCard(theSamePoints_firstPlayer, Consts.firstRow, firstPlayer);
        playCard(theSamePoints_secondPlayer, Consts.firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);
        duel.endRoundFor(secondPlayer);

        assertEquals(1, duel.getWonRoundsOf(firstPlayer));
        assertEquals(1, duel.getWonRoundsOf(secondPlayer));
    }


}