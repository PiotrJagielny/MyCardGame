package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.CardsServices.CardsParser;
import com.example.demo.Consts;
import com.example.demo.Duel.PlayerPlay;
import com.example.demo.Duel.CardDuel;
import com.example.demo.TestsData.TestConsts;
import com.example.demo.TestsData.TestsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.demo.TestsData.TestConsts.*;
import static com.example.demo.TestsData.TestsUtils.*;

class TestDuelAfterDeal {
    CardDuel duel;


    @BeforeEach
    public void setUp(){
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(secondPlayer);
        duel.registerPlayerToDuel(firstPlayer);
        List<CardDisplay> allCards = CardsParser.getCardsDisplay(CardsFactory.createAllCards());
        List<CardDisplay> noEffectCards = allCards.stream()
                .filter(c -> !c.getCardInfo().equals(""))
                .collect(Collectors.toList());
        duel.parseCardsFor( noEffectCards , firstPlayer);
        duel.parseCardsFor( noEffectCards , secondPlayer);
        duel.dealCards();
    }

    @Test
    public void beforePlayingCard_boardIsEmpty(){
        assertTrue(duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterPlayingCard_boardIsNotEmpty(){
        int cardsInHandBeforePlaying = duel.getCardsInHandDisplayOf(firstPlayer).size();
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        assertFalse(duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).isEmpty());
        assertNotEquals(cardsInHandBeforePlaying, duel.getCardsInHandDisplayOf(firstPlayer).size());
    }

    @Test
    public void clearBoard_hasNoPoints(){
        assertEquals(0, getBoardPointsOf(firstPlayer, duel));
    }

    @Test
    public void onFirstPlayerTurn_player2CantPlayCard(){
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
        assertTrue(duel.getCardsOnBoardDisplayOf(secondPlayer, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterPlayingCardsWithPoints_theSamePointsAreOnBoard(){
        CardDisplay cardToPlay_first = duel.getCardsInHandDisplayOf(firstPlayer).get(0);
        CardDisplay cardToPlay_second = duel.getCardsInHandDisplayOf(firstPlayer).get(1);
        TestsUtils.playCardWithoutTargeting(duel, cardToPlay_first, Consts.firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, cardToPlay_second, Consts.secondRow, firstPlayer);
        assertEquals(cardToPlay_first.getPoints() + cardToPlay_second.getPoints(), getBoardPointsOf(firstPlayer, duel));
    }

    @Test
    public void afterPlayingCard_cantPlayAnother(){
        int expectedCardsOnBoard = 1;
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        assertEquals(expectedCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size());

        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        assertEquals(expectedCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size());

        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
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
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        duel.endRoundFor(secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        int expectedNumberOfCardsOnBoard = 3;
        assertEquals(expectedNumberOfCardsOnBoard, duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size());
    }

    @Test
    public void afterTwoPlayerEndRound_newRoundStarts(){
        startNextRound_firstPlayerFirst();
        assertFalse(duel.didEndRound(firstPlayer));
        assertFalse(duel.didEndRound(secondPlayer));
    }

    @Test
    public void afterEndingFirstRound_newCardsAreDealt() {
        int cardsInHandBeforeNewTurn = duel.getCardsInHandDisplayOf(firstPlayer).size();
        startNextRound_firstPlayerFirst();
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
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.secondRow, firstPlayer);
        duel.endRoundFor(secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.thirdRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
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
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        int cardsOnBoard_afterPlay = duel.getCardsOnBoardDisplayOf(firstPlayer, Consts.firstRow).size();
        assertEquals(cardsOnBoard_afterPlay, cardsOnBoard_beforePlay);
    }

    public void playCardAsFirstPlayer_startNextRound(){
        TestsUtils.playCardWithoutTargeting(duel, duel.getCardsInHandDisplayOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        startNextRound_secondPlayerFirst();
    }

    public void startNextRound_secondPlayerFirst(){
        duel.endRoundFor(secondPlayer);
        duel.endRoundFor(firstPlayer);
    }
    public void startNextRound_firstPlayerFirst(){
        duel.endRoundFor(firstPlayer);
        duel.endRoundFor(secondPlayer);
    }

    @Test
    public void testCalculatingWonRounds(){
        CardDisplay morePointsCard = Collections.max(
                duel.getCardsInHandDisplayOf(firstPlayer), Comparator.comparingInt(CardDisplay::getPoints)
        );
        CardDisplay lessPointsCard = Collections.min(
                duel.getCardsInHandDisplayOf(secondPlayer), Comparator.comparingInt(CardDisplay::getPoints)
        );
        TestsUtils.playCardWithoutTargeting(duel, morePointsCard, Consts.firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, lessPointsCard, Consts.firstRow, secondPlayer);
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
        TestsUtils.playCardWithoutTargeting(duel, theSamePoints_firstPlayer, Consts.firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, theSamePoints_secondPlayer, Consts.firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);
        duel.endRoundFor(secondPlayer);

        assertEquals(1, duel.getWonRoundsOf(firstPlayer));
        assertEquals(1, duel.getWonRoundsOf(secondPlayer));
    }

    @Test
    public void afterCardDies_goesToGraveyard() {
        duel = TestsUtils.createDuel(List.of(CardsFactory.minion, CardsFactory.archer));
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.minion, firstRow, firstPlayer);
        TestsUtils.playCardWithCardTargeting(duel, CardsFactory.archer, firstRow, CardsFactory.minion, secondPlayer);
        assertEquals(1, duel.getPlayerGraveyard(firstPlayer).size());

    }
    @Test
    public void afterCardIsBurned_goesToGraveyard() {
        duel = TestsUtils.createDuel(List.of(CardsFactory.knight, CardsFactory.conflagration));
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.knight, firstRow, firstPlayer);
        TestsUtils.playSpecialCardWithoutTargeting(duel, CardsFactory.conflagration, secondPlayer);
        assertEquals(1, duel.getPlayerGraveyard(firstPlayer).size());
    }
    @Test
    public void afterNewRound_everyCardGoesToGraveyard() {
        duel = TestsUtils.createDuel(List.of(CardsFactory.viking, CardsFactory.knight));
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.viking, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.viking, firstRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.knight, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.knight, firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);
        duel.endRoundFor(secondPlayer);

        assertTrue(duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).isEmpty());
        assertTrue(duel.getCardsOnBoardDisplayOf(secondPlayer, firstRow).isEmpty());

        assertEquals(2,duel.getPlayerGraveyard(firstPlayer).size());
        assertEquals(2,duel.getPlayerGraveyard(secondPlayer).size());
    }


}