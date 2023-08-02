package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Duel.CardDuel;
import com.example.demo.TestsData.TestsUtils;
import org.junit.jupiter.api.Test;
import static com.example.demo.TestsData.TestsUtils.*;

import java.util.List;

import static com.example.demo.Consts.*;
import static org.junit.jupiter.api.Assertions.*;

class TestSpecificBehaviours {


    private CardDuel duel;

    public CardDuel createDuel(List<CardDisplay> deck){
        CardDuel result = CardDuel.createDuel();
        result.registerPlayerToDuel(secondPlayer);
        result.registerPlayerToDuel(firstPlayer);
        result.parseCardsFor(deck , secondPlayer);
        result.parseCardsFor(deck , firstPlayer);
        result.dealCards();
        return result;
    }

    @Test
    public void testBoostingSingleRow(){
        List<CardDisplay> deck = List.of(CardsFactory.leader, CardsFactory.viking);

        duel  = createDuel(deck);

        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.viking, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.viking, firstRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.leader, firstRow, firstPlayer);
        int expectedPoints = CardsFactory.viking.getPoints() + CardsFactory.leaderBoost + CardsFactory.leader.getPoints();
        assertEquals(expectedPoints , getBoardPointsOf(firstPlayer, duel));
    }

    @Test
    public void testBoostingSingleCard(){
        List<CardDisplay> deck = List.of(CardsFactory.booster, CardsFactory.viking);
        duel = createDuel(deck);

        TestsUtils.playCardWithoutTargeting(duel,CardsFactory.viking, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel,CardsFactory.viking, firstRow, secondPlayer);
        TestsUtils.playCardWithCardTargeting(duel, CardsFactory.booster, secondRow,CardsFactory.viking, firstPlayer);
        int singleCardBoostAmount = 3;
        CardDisplay boostedCard = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).stream().filter(c -> c.getName().equals("Viking")).findFirst().orElse(null);
        assertEquals(CardsFactory.viking.getPoints() + singleCardBoostAmount, boostedCard.getPoints());
    }

    @Test
    public void testBoostingWholeBoard(){
        List<CardDisplay> deck = List.of(CardsFactory.healer, CardsFactory.paper, CardsFactory.minion, CardsFactory.viking);
        duel  = createDuel(deck);

        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.paper, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.paper, firstRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.minion, secondRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.minion, secondRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.viking, thirdRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.viking, thirdRow, secondPlayer);

        TestsUtils.playCardWithoutTargeting(duel,CardsFactory.healer, thirdRow, firstPlayer);

        int expectedMinionPoints = CardsFactory.minion.getPoints() + CardsFactory.healerBoost;
        int expectedPaperPoints = CardsFactory.paper.getPoints() + CardsFactory.healerBoost;
        int expectedVikingPoints = CardsFactory.viking.getPoints();
        int expectedBoardPoints = expectedMinionPoints + expectedPaperPoints + expectedVikingPoints + CardsFactory.healer.getPoints();

        assertEquals(expectedBoardPoints, getBoardPointsOf(firstPlayer, duel));
    }

    @Test
    public void testStrikingEnemyCard(){
        List<CardDisplay> deck = List.of(CardsFactory.archer, CardsFactory.viking);
        duel  = createDuel(deck);

        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.viking, firstRow, firstPlayer);
        TestsUtils.playCardWithCardTargeting(duel, CardsFactory.archer, firstRow, CardsFactory.viking, secondPlayer);

        CardDisplay strikedVikingDisplay = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0);

        int fireballStrikeAmount = 3;
        assertEquals(CardsFactory.viking.getPoints() - fireballStrikeAmount,  strikedVikingDisplay.getPoints());
        assertTrue(duel.getCardsInHandDisplayOf(firstPlayer).contains(CardsFactory.archer));
        assertEquals(1, getBoardPointsOf(secondPlayer, duel));
    }

    @Test
    public void testUsingStrikingSpell() {
        List<CardDisplay> deck = List.of(CardsFactory.fireball, CardsFactory.viking);
        duel  = createDuel(deck);

        TestsUtils.playCardWithoutTargeting(duel,CardsFactory.viking, firstRow, firstPlayer);
        TestsUtils.playSpecialCardWithCardTargeting(duel, CardsFactory.fireball, CardsFactory.viking, secondPlayer);

        assertTrue(duel.getCardsOnBoardDisplayOf(secondPlayer, thirdRow).isEmpty());
        boolean isFireballInHand = duel.getCardsInHandDisplayOf(secondPlayer)
                .stream()
                .filter(c -> c.equals(CardsFactory.fireball))
                .findFirst()
                .orElse(null) == null;
        assertTrue(isFireballInHand);
        int actualVikingPoints_afterStrike = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0).getPoints();
        int expectedVikingPoints_afterStrike = CardsFactory.viking.getPoints() - CardsFactory.fireballDamage;
        assertEquals(expectedVikingPoints_afterStrike, actualVikingPoints_afterStrike);
    }

    @Test
    public void testCardsTargeting(){
        List<CardDisplay> deck = List.of(CardsFactory.viking, CardsFactory.paper, CardsFactory.minion, CardsFactory.warrior);
        duel = createDuel(deck);

        TestsUtils.playCardWithoutTargeting(duel,CardsFactory.viking, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel,CardsFactory.warrior, firstRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel,CardsFactory.paper, secondRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel,CardsFactory.minion, secondRow, secondPlayer);

        List<CardDisplay> possibleTargetsOfBooster = duel.getPossibleTargetsOf(CardsFactory.booster, firstPlayer);
        assertTrue(possibleTargetsOfBooster.contains(CardsFactory.viking));
        assertTrue(possibleTargetsOfBooster.contains(CardsFactory.paper));

        possibleTargetsOfBooster = duel.getPossibleTargetsOf(CardsFactory.booster, secondPlayer);
        assertTrue(possibleTargetsOfBooster.isEmpty());

        List<CardDisplay> possibleTargetsOfFireball = duel.getPossibleTargetsOf(CardsFactory.archer, firstPlayer);
        assertTrue(possibleTargetsOfFireball.contains(CardsFactory.warrior));
        assertTrue(possibleTargetsOfFireball.contains(CardsFactory.minion));
    }

    @Test
    public void testCardsTargeting_ifThereIsNoTarget(){
        List<CardDisplay> deck = List.of(CardsFactory.viking, CardsFactory.booster, CardsFactory.minion, CardsFactory.warrior);
        duel = createDuel(deck);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.booster, secondRow, firstPlayer);
        assertTrue(duel.getPossibleTargetsOf(CardsFactory.booster, firstPlayer).isEmpty());
    }

    @Test
    public void cardWithPointsBelowOneIsRemoved() {
        List<CardDisplay> deck = List.of(CardsFactory.paper, CardsFactory.archer);
        duel = createDuel(deck);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.paper, firstRow, firstPlayer);
        TestsUtils.playCardWithCardTargeting(duel, CardsFactory.archer, firstRow, CardsFactory.paper, secondPlayer);
        assertTrue(duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).isEmpty());
    }


    @Test
    public void testBurningAllMaxPointsCards(){
        List<CardDisplay> deck = List.of(CardsFactory.capitan, CardsFactory.conflagration);
        duel = createDuel(deck);
        TestsUtils.playCardWithoutTargeting(duel,CardsFactory.capitan, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel,CardsFactory.capitan, firstRow, secondPlayer);
        TestsUtils.playSpecialCardWithoutTargeting(duel, CardsFactory.conflagration, firstPlayer);

        int board = getBoardPointsOf(firstPlayer, duel);
        assertEquals(0, getBoardPointsOf(firstPlayer, duel));
        assertEquals(0, getBoardPointsOf(secondPlayer, duel));
    }

    @Test
    public void testDoublingCardPoints() {
        List<CardDisplay> deck = List.of(CardsFactory.doubler, CardsFactory.capitan, CardsFactory.viking);
        duel = createDuel(deck);

        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.capitan, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.capitan, firstRow, secondPlayer);
        TestsUtils.playCardWithCardTargeting(duel, CardsFactory.doubler, secondRow, CardsFactory.capitan, firstPlayer);

        int expectedPoints = CardsFactory.capitan.getPoints() * 2;
        int actualPoints = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0).getPoints();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testDealingDamageToWholeRow() {
        List<CardDisplay> deck = List.of(CardsFactory.rip, CardsFactory.capitan, CardsFactory.armageddon, CardsFactory.paper);
        duel = createDuel(deck);

        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.capitan, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.capitan, firstRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.armageddon, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.armageddon, firstRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.paper, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.paper, firstRow, secondPlayer);
        TestsUtils.playSpecialCardWithRowTargeting(duel, CardsFactory.rip, firstRow ,firstPlayer );

        int firstUnitExpected = Math.max(CardsFactory.capitan.getPoints() - CardsFactory.ripDamage, 0);
        int secondUnitExpected = Math.max(CardsFactory.armageddon.getPoints() - CardsFactory.ripDamage, 0);
        int thirdUnitExpected = Math.max(CardsFactory.paper.getPoints() - CardsFactory.ripDamage, 0);
        int expected = firstUnitExpected + secondUnitExpected + thirdUnitExpected;

        assertEquals(expected, getBoardPointsOf(secondPlayer, duel));
    }
    @Test
    public void testBoostingEveryTurn() {
        duel = createDuel(List.of(CardsFactory.longer, CardsFactory.paper, CardsFactory.viking));
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.longer, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.paper, firstRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.paper, secondRow, firstPlayer);
        int expectedPoints= CardsFactory.longer.getPoints() + 2*CardsFactory.longerBoost;
        int actualPoints = duel.getCardsOnBoardDisplayOf( firstPlayer, firstRow).get(0).getPoints();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testRainRowStatus() {
        duel = createDuel(List.of(CardsFactory.rain, CardsFactory.viking, CardsFactory.paper ));
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.viking, secondRow, firstPlayer);
        TestsUtils.playSpecialCardWithRowTargeting(duel, CardsFactory.rain, secondRow, secondPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.paper, secondRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.viking, secondRow, secondPlayer);
        duel.endRoundFor(firstPlayer);
        int expected = CardsFactory.viking.getPoints() - 2*CardsFactory.rainDamage + CardsFactory.paper.getPoints();
        int actual = getBoardPointsOf(firstPlayer, duel);
        assertEquals(expected, actual);
    }

    @Test
    public void testClearingAllRowsStatus() {
        duel = createDuel(List.of(CardsFactory.rain, CardsFactory.viking, CardsFactory.paper, CardsFactory.clearSky));
        TestsUtils.playSpecialCardWithRowTargeting(duel, CardsFactory.rain, firstRow, firstPlayer);
        TestsUtils.playSpecialCardWithoutTargeting(duel, CardsFactory.clearSky, secondPlayer);
        String expectedRowStatus = "";
        String actualRowStatus = duel.getRowStatusOf(secondPlayer, firstRow);
        assertEquals(expectedRowStatus, actualRowStatus);
    }
    @Test
    public void testPlyingCardFromDeck() {
        duel = createDuel(List.of(CardsFactory.priest,CardsFactory.paper, CardsFactory.capitan, CardsFactory.witch ,CardsFactory.viking, CardsFactory.warrior));
        CardDisplay nextCardInChain = TestsUtils.playCardWithCardTargeting(duel, CardsFactory.priest, firstRow, CardsFactory.viking, firstPlayer);

        int cardsInDeck = duel.getCardsInDeckDisplayOf(firstPlayer).size();
        CardDisplay vikingPlayed_noChainCard= TestsUtils.playCardWithoutTargeting(duel, nextCardInChain,firstRow, firstPlayer);
        int cardsInDeckAfterChainPlay = duel.getCardsInDeckDisplayOf(firstPlayer).size();

        int expectedPoints = CardsFactory.viking.getPoints() + CardsFactory.priest.getPoints();
        int actualPoints = duel.getRowPointsOf(firstPlayer, firstRow);
        assertEquals(new CardDisplay(), vikingPlayed_noChainCard);
        assertEquals(expectedPoints, actualPoints);
        assertEquals(cardsInDeck, cardsInDeckAfterChainPlay + 1);

    }

    @Test
    public void dealDamageToCard_boostIfItDies() {
        duel = createDuel(List.of(CardsFactory.sharpshooter, CardsFactory.paper, CardsFactory.capitan));

        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.capitan, firstRow, firstPlayer);
        TestsUtils.playCardWithCardTargeting(duel, CardsFactory.sharpshooter, firstRow, CardsFactory.capitan, secondPlayer);

        assertEquals(CardsFactory.sharpshooter.getPoints() ,duel.getRowPointsOf(secondPlayer, firstRow));

        duel = createDuel(List.of(CardsFactory.sharpshooter, CardsFactory.minion, CardsFactory.capitan));

        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.minion, firstRow, firstPlayer);
        TestsUtils.playCardWithCardTargeting(duel, CardsFactory.sharpshooter, firstRow, CardsFactory.minion, secondPlayer);

        int expected =CardsFactory.sharpshooter.getPoints() + CardsFactory.sharpshooterSelfBoost;
        assertEquals(expected,duel.getRowPointsOf(secondPlayer, firstRow));
    }

    @Test
    public void testSpawningNewUnitsOnDeath() {
        duel = createDuel(List.of(CardsFactory.cow, CardsFactory.archer, CardsFactory.viking));
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.cow, firstRow, firstPlayer);
        TestsUtils.playCardWithCardTargeting(duel, CardsFactory.archer, firstRow, CardsFactory.cow, secondPlayer);
        assertEquals(CardsFactory.chort, duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0));


        duel = createDuel(List.of(CardsFactory.cow, CardsFactory.archer, CardsFactory.viking));
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.cow, firstRow, firstPlayer);
        duel.endRoundFor(secondPlayer);
        duel.endRoundFor(firstPlayer);
        assertEquals(CardsFactory.chort, duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0));
    }

    @Test
    public void testDealingDamageEveryTwoTurns() {

        duel = createDuel(List.of(CardsFactory.trebuchet, CardsFactory.viking));
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.trebuchet, firstRow, firstPlayer);
        TestsUtils.playCardWithoutTargeting(duel, CardsFactory.viking, firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);

        int expectedPoints = CardsFactory.viking.getPoints() - CardsFactory.trebuchetDamage;
        assertEquals(expectedPoints, duel.getCardsOnBoardDisplayOf(secondPlayer, firstRow).get(0).getPoints());


    }



}