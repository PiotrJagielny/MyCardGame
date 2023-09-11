package com.example.demo.DuelTests;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Consts;
import com.example.demo.Duel.ClientAPI.CardDuel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.Cards.CardsFactory.*;
import static com.example.demo.TestsData.TestConsts.*;
import static com.example.demo.TestsData.TestsUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class TestDuelAfterDeal {
    CardDuel duel;

    private List<CardDisplay> hand1 = new ArrayList<>();
    private List<CardDisplay> hand2 = new ArrayList<>();


    public void setHands() {
        hand1.clear();
        hand2.clear();
        hand1 = duel.getHandOf(player1);
        hand2 = duel.getHandOf(player2);
    }

    @BeforeEach
    public void setUp(){
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(player2);
        duel.registerPlayerToDuel(player1);
        List<CardDisplay> allCards = getCardsDisplay(createAllCards());
        List<CardDisplay> noEffectCards = allCards.stream()
                .filter(c -> c.getCardInfo().equals(""))
                .collect(Collectors.toList());
        duel.parseCardsFor( noEffectCards , player1);
        duel.parseCardsFor( noEffectCards , player2);
        duel.dealCards();
    }

    @Test
    public void beforePlayingCard_boardIsEmpty(){
        assertTrue(duel.getRowOf(player1, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterPlayingCard_boardIsNotEmpty(){
        int cardsInHandBeforePlaying = duel.getHandOf(player1).size();
        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.firstRow, player1);
        assertFalse(duel.getRowOf(player1, Consts.firstRow).isEmpty());
        assertNotEquals(cardsInHandBeforePlaying, duel.getHandOf(player1).size());
    }

    @Test
    public void clearBoard_hasNoPoints(){
        assertEquals(0, getBoardPointsOf(player1, duel));
    }

    @Test
    public void onFirstPlayerTurn_player2CantPlayCard(){
        playCardWithoutTargeting(duel, duel.getHandOf(player2).get(0), Consts.firstRow, player2);
        assertTrue(duel.getRowOf(player2, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterPlayingCardsWithPoints_theSamePointsAreOnBoard(){
        CardDisplay cardToPlay_first = duel.getHandOf(player1).get(0);
        CardDisplay cardToPlay_second = duel.getHandOf(player1).get(1);
        playCardWithoutTargeting(duel, cardToPlay_first, Consts.firstRow, player1);
        playCardWithoutTargeting(duel, duel.getHandOf(player2).get(0), Consts.firstRow, player2);
        playCardWithoutTargeting(duel, cardToPlay_second, Consts.secondRow, player1);
        assertEquals(cardToPlay_first.getPoints() + cardToPlay_second.getPoints(), getBoardPointsOf(player1, duel));
    }

    @Test
    public void afterPlayingCard_cantPlayAnother(){
        int expectedCardsOnBoard = 1;
        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.firstRow, player1);
        assertEquals(expectedCardsOnBoard, duel.getRowOf(player1, Consts.firstRow).size());

        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.firstRow, player1);
        assertEquals(expectedCardsOnBoard, duel.getRowOf(player1, Consts.firstRow).size());

        playCardWithoutTargeting(duel, duel.getHandOf(player2).get(0), Consts.firstRow, player2);
        playCardWithoutTargeting(duel, duel.getHandOf(player2).get(0), Consts.firstRow, player2);
        assertEquals(expectedCardsOnBoard, duel.getRowOf(player2, Consts.firstRow).size());
    }

    @Test
    public void playerCanEndRound(){
        duel.endRoundFor(player1);
        assertTrue(duel.didEndRound(player1));
    }

    @Test
    public void atStartOfGame_nobodyEndedRound(){
        assertFalse(duel.didEndRound(player1));
        assertFalse(duel.didEndRound(player2));
    }

    @Test
    public void afterOnePlayerEndsRound_turnsDontSwitch() {
        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.firstRow, player1);
        duel.endRoundFor(player2);
        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.firstRow, player1);
        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.firstRow, player1);
        int expectedNumberOfCardsOnBoard = 3;
        assertEquals(expectedNumberOfCardsOnBoard, duel.getRowOf(player1, Consts.firstRow).size());
    }

    @Test
    public void afterTwoPlayerEndRound_newRoundStarts(){
        startNextRound_firstPlayerFirst();
        assertFalse(duel.didEndRound(player1));
        assertFalse(duel.didEndRound(player2));
    }

    @Test
    public void afterEndingFirstRound_newCardsAreDealt() {
        int cardsInHandBeforeNewTurn = duel.getHandOf(player1).size();
        startNextRound_firstPlayerFirst();
        int cardsInHandAfterNewTurn = duel.getHandOf(player1).size();
        assertNotEquals(cardsInHandAfterNewTurn, cardsInHandBeforeNewTurn);
    }

    @Test
    public void atTheGameStart_noOneWonRound(){
        assertEquals(0, duel.getWonRoundsOf(player1));
        assertEquals(0, duel.getWonRoundsOf(player2));
    }

    @Test
    public void afterWinningTurn_onePlayerScoresPoints(){
        playCardAsFirstPlayer_startNextRound();
        assertEquals(1, duel.getWonRoundsOf(player1));
        assertEquals(0, duel.getWonRoundsOf(player2));
    }

    @Test
    public void afterNewRound_boardIsEmpty(){
        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.secondRow, player1);
        duel.endRoundFor(player2);
        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.thirdRow, player1);
        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.firstRow, player1);
        duel.endRoundFor(player1);
        for(int row = 0 ; row < Consts.rowsNumber ; ++row){
            assertTrue(duel.getRowOf(player1, row).isEmpty());
        }
    }

    @Test
    public void afterTwoRoundsWon_playerWonGame(){
        playCardAsFirstPlayer_startNextRound();
        playCardAsFirstPlayer_startNextRound();
        assertTrue(duel.didWon(player1));
    }

    @Test
    public void afterWonGame_cantPlayCard(){
        playCardAsFirstPlayer_startNextRound();
        playCardAsFirstPlayer_startNextRound();
        int cardsOnBoard_beforePlay = duel.getRowOf(player1, Consts.firstRow).size();
        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.firstRow, player1);
        int cardsOnBoard_afterPlay = duel.getRowOf(player1, Consts.firstRow).size();
        assertEquals(cardsOnBoard_afterPlay, cardsOnBoard_beforePlay);
    }

    public void playCardAsFirstPlayer_startNextRound(){
        playCardWithoutTargeting(duel, duel.getHandOf(player1).get(0), Consts.firstRow, player1);
        startNextRound_secondPlayerFirst();
    }

    public void startNextRound_secondPlayerFirst(){
        duel.endRoundFor(player2);
        duel.endRoundFor(player1);
    }
    public void startNextRound_firstPlayerFirst(){
        duel.endRoundFor(player1);
        duel.endRoundFor(player2);
    }

    @Test
    public void testCalculatingWonRounds(){
        CardDisplay morePointsCard = Collections.max(
                duel.getHandOf(player1), Comparator.comparingInt(CardDisplay::getPoints)
        );
        CardDisplay lessPointsCard = Collections.min(
                duel.getHandOf(player2), Comparator.comparingInt(CardDisplay::getPoints)
        );
        playCardWithoutTargeting(duel, morePointsCard, Consts.firstRow, player1);
        playCardWithoutTargeting(duel, lessPointsCard, Consts.firstRow, player2);
        duel.endRoundFor(player1);
        duel.endRoundFor(player2);

        assertEquals(1, duel.getWonRoundsOf(player1));
        assertEquals(0, duel.getWonRoundsOf(player2));
    }

    @Test
    public void afterTie_bothPlayersScoresPoint(){
        CardDisplay theSamePoints_firstPlayer = duel.getHandOf(player1).get(0);
        CardDisplay theSamePoints_secondPlayer = duel.getHandOf(player2).stream()
                .filter(c -> c.getPoints() == theSamePoints_firstPlayer.getPoints())
                .findFirst().orElse(null);
        playCardWithoutTargeting(duel, theSamePoints_firstPlayer, Consts.firstRow, player1);
        playCardWithoutTargeting(duel, theSamePoints_secondPlayer, Consts.firstRow, player2);
        duel.endRoundFor(player1);
        duel.endRoundFor(player2);

        assertEquals(1, duel.getWonRoundsOf(player1));
        assertEquals(1, duel.getWonRoundsOf(player2));
    }

    @Test
    public void afterCardDies_goesToGraveyard() {
        duel = createDuel(List.of(minion, archer));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, minion), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow, findByName(board(duel,player1), minion), player2);
        assertEquals(1, duel.getGraveyardOf(player1).size());

    }
    @Test
    public void afterCardIsBurned_goesToGraveyard() {
        duel = createDuel(List.of(knight, conflagration));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, knight), firstRow, player1);
        playSpecialCardWithoutTargeting(duel, findByName(hand2, conflagration), player2);
        assertEquals(1, duel.getGraveyardOf(player1).size());
    }
    @Test
    public void afterNewRound_everyCardGoesToGraveyard() {
        duel = createDuel(List.of(viking, knight));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, viking), firstRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1, knight), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, knight), firstRow, player2);
        duel.endRoundFor(player1);
        duel.endRoundFor(player2);

        assertTrue(duel.getRowOf(player1, firstRow).isEmpty());
        assertTrue(duel.getRowOf(player2, firstRow).isEmpty());

        assertEquals(2,duel.getGraveyardOf(player1).size());
        assertEquals(2,duel.getGraveyardOf(player2).size());
    }

    @Test
    public void testPlayingTwoSameCards() {
        duel = createDuel(List.of(viking, viking));
        setHands();

        playCardWithoutTargeting(duel,hand1.get(0), firstRow, player1);
        playCardWithoutTargeting(duel,hand2.get(0), firstRow, player2);
        playCardWithoutTargeting(duel,hand1.get(1), firstRow, player1);
        playCardWithoutTargeting(duel,hand2.get(1), firstRow, player2);
        List<CardDisplay> rowCards = duel.getRowOf(player1, firstRow);
        assertNotEquals(rowCards.get(0).getId(), rowCards.get(1).getId());
    }

    @Test
    public void testDealingDamageToOneOfTwoSameCards() {
        duel = createDuel(List.of(viking, viking, archer));
        setHands();


        playCardWithoutTargeting(duel, hand1.get(0), firstRow, player1);
        playCardWithoutTargeting(duel, hand2.get(0), firstRow, player2);
        playCardWithoutTargeting(duel, hand1.get(1), firstRow, player1);

        List<CardDisplay> enemyBoard = duel.getRowOf(player1, firstRow);
        playCardWithCardTargeting(duel, hand2.get(2), firstRow,enemyBoard.get(1), player2);
        enemyBoard = duel.getRowOf(player1, firstRow);
        assertEquals(viking.getPoints() - archerDamage, enemyBoard.get(1).getPoints());
        assertEquals(viking.getPoints(), enemyBoard.get(0).getPoints());
    }

    @Test
    public void afterCardKill_itsPowerResetsOnGraveyard() {
        duel = createDuel(List.of(archer, armageddon, fireball));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, armageddon), firstRow, player1);
        playSpecialCardWithCardTargeting(duel, findByName(hand2, fireball), findByName(board(duel, player1), armageddon), player2);
        playCardWithoutTargeting(duel, findByName(hand1, archer), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow, findByName(board(duel,player1), armageddon), player2);

        assertEquals(armageddon.getPoints(), duel.getGraveyardOf(player1).get(0).getPoints());
    }

    @Test
    public void testEndRoundEffectsAfterPass() {
        duel = createDuel(List.of(viking, minion, trebuchet, warrior));
        setHands();



        playCardWithoutTargeting(duel , findByName(hand1, trebuchet), firstRow, player1);
        playCardWithoutTargeting(duel , findByName(hand2, viking), firstRow, player2);
        duel.endRoundFor(player1); //firstTrebuchetDamage
        playCardWithoutTargeting(duel , findByName(hand2, minion), firstRow, player2);
        playCardWithoutTargeting(duel , findByName(hand2, warrior), firstRow, player2);
        int expectedPoints =viking.getPoints() + minion.getPoints() + warrior.getPoints() - 2*trebuchetDamage;
        assertEquals(expectedPoints, duel.getRowPointsOf(player2,firstRow));

    }
    @Test
    public void testStartRoundEffectsAfterPass() {
        duel = createDuel(List.of(viking, minion, breaker, warrior, capitan, armageddon, paper, knight, thunder, breaker));
        setHands();

        playCardWithoutTargeting(duel , findByName(hand1, breaker), firstRow, player1);
        playCardWithoutTargeting(duel , findByName(hand2, breaker), firstRow, player2);
        duel.endRoundFor(player1);
        playCardWithoutTargeting(duel , findByName(hand2, warrior), secondRow, player2);
        playCardWithoutTargeting(duel , findByName(hand2, minion), secondRow, player2);
        assertEquals(2, duel.getRowOf(player1, firstRow).size());
        assertEquals(2, duel.getRowOf(player2, firstRow).size());

    }

    @Test
    public void testPuttingBasePowerCardToDeck() {
        duel = createDuel(List.of(ginger, warrior, copier));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, warrior), firstRow, player1);
        playCardWithRowTargeting(duel, findByName(hand2, ginger), secondRow, firstRow, player2);
        playCardWithCardTargeting(duel, findByName(hand1, copier), secondRow, duel.getRowOf(player1,firstRow).get(0), player1);

        int expectedPointsSum = copierCopiesCount * warrior.getPoints();
        int actualPointsSum = duel.getDeckOf(player1)
                .stream().mapToInt(c -> c.getPoints()).sum();
        assertEquals(expectedPointsSum, actualPointsSum);
    }


}