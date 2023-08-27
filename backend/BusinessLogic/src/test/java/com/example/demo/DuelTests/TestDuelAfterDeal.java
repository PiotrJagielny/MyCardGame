package com.example.demo.DuelTests;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.CardsFactory;
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
        hand1 = duel.getHandOf(firstPlayer);
        hand2 = duel.getHandOf(secondPlayer);
    }

    @BeforeEach
    public void setUp(){
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(secondPlayer);
        duel.registerPlayerToDuel(firstPlayer);
        List<CardDisplay> allCards = getCardsDisplay(createAllCards());
        List<CardDisplay> noEffectCards = allCards.stream()
                .filter(c -> c.getCardInfo().equals(""))
                .collect(Collectors.toList());
        duel.parseCardsFor( noEffectCards , firstPlayer);
        duel.parseCardsFor( noEffectCards , secondPlayer);
        duel.dealCards();
    }

    @Test
    public void beforePlayingCard_boardIsEmpty(){
        assertTrue(duel.getRowOf(firstPlayer, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterPlayingCard_boardIsNotEmpty(){
        int cardsInHandBeforePlaying = duel.getHandOf(firstPlayer).size();
        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        assertFalse(duel.getRowOf(firstPlayer, Consts.firstRow).isEmpty());
        assertNotEquals(cardsInHandBeforePlaying, duel.getHandOf(firstPlayer).size());
    }

    @Test
    public void clearBoard_hasNoPoints(){
        assertEquals(0, getBoardPointsOf(firstPlayer, duel));
    }

    @Test
    public void onFirstPlayerTurn_player2CantPlayCard(){
        playCardWithoutTargeting(duel, duel.getHandOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
        assertTrue(duel.getRowOf(secondPlayer, Consts.firstRow).isEmpty());
    }

    @Test
    public void afterPlayingCardsWithPoints_theSamePointsAreOnBoard(){
        CardDisplay cardToPlay_first = duel.getHandOf(firstPlayer).get(0);
        CardDisplay cardToPlay_second = duel.getHandOf(firstPlayer).get(1);
        playCardWithoutTargeting(duel, cardToPlay_first, Consts.firstRow, firstPlayer);
        playCardWithoutTargeting(duel, duel.getHandOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
        playCardWithoutTargeting(duel, cardToPlay_second, Consts.secondRow, firstPlayer);
        assertEquals(cardToPlay_first.getPoints() + cardToPlay_second.getPoints(), getBoardPointsOf(firstPlayer, duel));
    }

    @Test
    public void afterPlayingCard_cantPlayAnother(){
        int expectedCardsOnBoard = 1;
        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        assertEquals(expectedCardsOnBoard, duel.getRowOf(firstPlayer, Consts.firstRow).size());

        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        assertEquals(expectedCardsOnBoard, duel.getRowOf(firstPlayer, Consts.firstRow).size());

        playCardWithoutTargeting(duel, duel.getHandOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
        playCardWithoutTargeting(duel, duel.getHandOf(secondPlayer).get(0), Consts.firstRow, secondPlayer);
        assertEquals(expectedCardsOnBoard, duel.getRowOf(secondPlayer, Consts.firstRow).size());
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
        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        duel.endRoundFor(secondPlayer);
        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        int expectedNumberOfCardsOnBoard = 3;
        assertEquals(expectedNumberOfCardsOnBoard, duel.getRowOf(firstPlayer, Consts.firstRow).size());
    }

    @Test
    public void afterTwoPlayerEndRound_newRoundStarts(){
        startNextRound_firstPlayerFirst();
        assertFalse(duel.didEndRound(firstPlayer));
        assertFalse(duel.didEndRound(secondPlayer));
    }

    @Test
    public void afterEndingFirstRound_newCardsAreDealt() {
        int cardsInHandBeforeNewTurn = duel.getHandOf(firstPlayer).size();
        startNextRound_firstPlayerFirst();
        int cardsInHandAfterNewTurn = duel.getHandOf(firstPlayer).size();
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
        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.secondRow, firstPlayer);
        duel.endRoundFor(secondPlayer);
        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.thirdRow, firstPlayer);
        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        duel.endRoundFor(firstPlayer);
        for(int row = 0 ; row < Consts.rowsNumber ; ++row){
            assertTrue(duel.getRowOf(firstPlayer, row).isEmpty());
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
        int cardsOnBoard_beforePlay = duel.getRowOf(firstPlayer, Consts.firstRow).size();
        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
        int cardsOnBoard_afterPlay = duel.getRowOf(firstPlayer, Consts.firstRow).size();
        assertEquals(cardsOnBoard_afterPlay, cardsOnBoard_beforePlay);
    }

    public void playCardAsFirstPlayer_startNextRound(){
        playCardWithoutTargeting(duel, duel.getHandOf(firstPlayer).get(0), Consts.firstRow, firstPlayer);
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
                duel.getHandOf(firstPlayer), Comparator.comparingInt(CardDisplay::getPoints)
        );
        CardDisplay lessPointsCard = Collections.min(
                duel.getHandOf(secondPlayer), Comparator.comparingInt(CardDisplay::getPoints)
        );
        playCardWithoutTargeting(duel, morePointsCard, Consts.firstRow, firstPlayer);
        playCardWithoutTargeting(duel, lessPointsCard, Consts.firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);
        duel.endRoundFor(secondPlayer);

        assertEquals(1, duel.getWonRoundsOf(firstPlayer));
        assertEquals(0, duel.getWonRoundsOf(secondPlayer));
    }

    @Test
    public void afterTie_bothPlayersScoresPoint(){
        CardDisplay theSamePoints_firstPlayer = duel.getHandOf(firstPlayer).get(0);
        CardDisplay theSamePoints_secondPlayer = duel.getHandOf(secondPlayer).stream()
                .filter(c -> c.getPoints() == theSamePoints_firstPlayer.getPoints())
                .findFirst().orElse(null);
        playCardWithoutTargeting(duel, theSamePoints_firstPlayer, Consts.firstRow, firstPlayer);
        playCardWithoutTargeting(duel, theSamePoints_secondPlayer, Consts.firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);
        duel.endRoundFor(secondPlayer);

        assertEquals(1, duel.getWonRoundsOf(firstPlayer));
        assertEquals(1, duel.getWonRoundsOf(secondPlayer));
    }

    @Test
    public void afterCardDies_goesToGraveyard() {
        duel = createDuel(List.of(minion, archer));
        List<CardDisplay> hand1 = duel.getHandOf(firstPlayer);
        List<CardDisplay> hand2= duel.getHandOf(secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, minion), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow, findByName(hand1,minion ), secondPlayer);
        assertEquals(1, duel.getGraveyardOf(firstPlayer).size());

    }
    @Test
    public void afterCardIsBurned_goesToGraveyard() {
        duel = createDuel(List.of(knight, conflagration));
        List<CardDisplay> hand1 = duel.getHandOf(firstPlayer);
        List<CardDisplay> hand2= duel.getHandOf(secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, knight), firstRow, firstPlayer);
        playSpecialCardWithoutTargeting(duel, findByName(hand2, conflagration), secondPlayer);
        assertEquals(1, duel.getGraveyardOf(firstPlayer).size());
    }
    @Test
    public void afterNewRound_everyCardGoesToGraveyard() {
        duel = createDuel(List.of(viking, knight));

        List<CardDisplay> hand1 = duel.getHandOf(firstPlayer);
        List<CardDisplay> hand2= duel.getHandOf(secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, viking), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, knight), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, knight), firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);
        duel.endRoundFor(secondPlayer);

        assertTrue(duel.getRowOf(firstPlayer, firstRow).isEmpty());
        assertTrue(duel.getRowOf(secondPlayer, firstRow).isEmpty());

        assertEquals(2,duel.getGraveyardOf(firstPlayer).size());
        assertEquals(2,duel.getGraveyardOf(secondPlayer).size());
    }

    @Test
    public void testPlayingTwoSameCards() {
        duel = createDuel(List.of(viking, viking));
        List<CardDisplay> handCards_firstPlayer = duel.getHandOf(firstPlayer);
        List<CardDisplay> handCards_secondPlayer = duel.getHandOf(secondPlayer);
        playCardWithoutTargeting(duel,handCards_firstPlayer.get(0), firstRow, firstPlayer);
        playCardWithoutTargeting(duel,handCards_secondPlayer.get(0), firstRow, secondPlayer);
        playCardWithoutTargeting(duel,handCards_firstPlayer.get(1), firstRow, firstPlayer);
        playCardWithoutTargeting(duel,handCards_secondPlayer.get(1), firstRow, secondPlayer);
        List<CardDisplay> rowCards = duel.getRowOf(firstPlayer, firstRow);
        assertNotEquals(rowCards.get(0).getId(), rowCards.get(1).getId());
    }

    @Test
    public void testDealingDamageToOneOfTwoSameCards() {
        duel = createDuel(List.of(viking, viking, archer));
        List<CardDisplay> handCards_firstPlayer = duel.getHandOf(firstPlayer);
        List<CardDisplay> handCards_secondPlayer = duel.getHandOf(secondPlayer);
        playCardWithoutTargeting(duel, handCards_firstPlayer.get(0), firstRow, firstPlayer );
        playCardWithoutTargeting(duel, handCards_secondPlayer.get(0), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, handCards_firstPlayer.get(1), firstRow, firstPlayer );

        List<CardDisplay> enemyBoard = duel.getRowOf(firstPlayer, firstRow);
        playCardWithCardTargeting(duel, handCards_secondPlayer.get(2), firstRow,enemyBoard.get(1), secondPlayer);
        enemyBoard = duel.getRowOf(firstPlayer, firstRow);
        assertEquals(viking.getPoints() - archerDamage, enemyBoard.get(1).getPoints());
        assertEquals(viking.getPoints(), enemyBoard.get(0).getPoints());
    }

    @Test
    public void afterCardKill_itsPowerResetsOnGraveyard() {
        duel = createDuel(List.of(archer, armageddon, fireball));
        List<CardDisplay> hand1 = duel.getHandOf(firstPlayer);
        List<CardDisplay> hand2 = duel.getHandOf(secondPlayer);

        playCardWithoutTargeting(duel, findByName(hand1, armageddon), firstRow, firstPlayer);
        playSpecialCardWithCardTargeting(duel, findByName(hand2, fireball), findByName(hand1, armageddon), secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, archer), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow, findByName(hand1, armageddon), secondPlayer);

        assertEquals(armageddon.getPoints(), duel.getGraveyardOf(firstPlayer).get(0).getPoints());
    }

    @Test
    public void testEndRoundEffectsAfterPass() {
        duel = createDuel(List.of(viking, minion, trebuchet, warrior));
        setHands();



        playCardWithoutTargeting(duel , findByName(hand1, trebuchet), firstRow, firstPlayer);
        playCardWithoutTargeting(duel , findByName(hand2, viking), firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer); //firstTrebuchetDamage
        playCardWithoutTargeting(duel , findByName(hand2, minion), firstRow, secondPlayer);
        playCardWithoutTargeting(duel , findByName(hand2, warrior), firstRow, secondPlayer);
        int expectedPoints =viking.getPoints() + minion.getPoints() + warrior.getPoints() - 2*trebuchetDamage;
        assertEquals(expectedPoints, duel.getRowPointsOf(secondPlayer,firstRow));

    }
    @Test
    public void testStartRoundEffectsAfterPass() {
        duel = createDuel(List.of(viking, minion, breaker, warrior, capitan, armageddon, paper, knight, thunder, breaker));
        setHands();

        playCardWithoutTargeting(duel , findByName(hand1, breaker), firstRow, firstPlayer);
        playCardWithoutTargeting(duel , findByName(hand2, breaker), firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);
        playCardWithoutTargeting(duel , findByName(hand2, warrior), secondRow, secondPlayer);
        playCardWithoutTargeting(duel , findByName(hand2, minion), secondRow, secondPlayer);
        assertEquals(2, duel.getRowOf(firstPlayer, firstRow).size());
        assertEquals(2, duel.getRowOf(secondPlayer, firstRow).size());


    }


}