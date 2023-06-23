package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Duel.PlayerPlay;
import com.example.demo.Duel.CardDuel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.demo.Consts.*;
import static org.junit.jupiter.api.Assertions.*;

class TestSpecificBehaviours {


    private CardDuel duel;


    public void playCard(CardDisplay playedCard, int onRow, CardDisplay cardThatGotEffect, String player){
        duel.playCardAs(new PlayerPlay(playedCard, onRow,cardThatGotEffect), player);
    }

    public void playCard(CardDisplay playedCard, int onRow, String player){
        duel.playCardAs(new PlayerPlay(playedCard, onRow), player);
    }
    public void playCard(CardDisplay playedCard, int onRow, int affectedRow, String player){
        duel.playCardAs(new PlayerPlay(playedCard, onRow, new CardDisplay(),affectedRow), player);
    }

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

        playCard(deck.get(1), firstRow, firstPlayer);
        playCard(deck.get(1), firstRow, secondPlayer);
        playCard(deck.get(0), firstRow, firstPlayer);
        int leaderRowBoostAmount = 2;
        int expectedPoints = CardsFactory.viking.getPoints() + leaderRowBoostAmount + CardsFactory.leader.getPoints();
        assertEquals(expectedPoints , duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void testBoostingSingleCard(){
        List<CardDisplay> deck = List.of(CardsFactory.booster, CardsFactory.viking);
        duel = createDuel(deck);

        playCard(deck.get(1), firstRow, firstPlayer);
        playCard(deck.get(1), firstRow, secondPlayer);
        playCard(deck.get(0), secondRow,deck.get(1), firstPlayer);
        int singleCardBoostAmount = 3;
        CardDisplay boostedCard = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).stream().filter(c -> c.getName().equals("Viking")).findFirst().orElse(null);
        assertEquals(CardsFactory.viking.getPoints() + singleCardBoostAmount, boostedCard.getPoints());
    }

    @Test
    public void testBoostingWholeBoard(){
        List<CardDisplay> deck = List.of(CardsFactory.healer, CardsFactory.paper, CardsFactory.minion, CardsFactory.viking);
        duel  = createDuel(deck);

        playCard(CardsFactory.paper, firstRow, firstPlayer);
        playCard(CardsFactory.paper, firstRow, secondPlayer);
        playCard(CardsFactory.minion, secondRow, firstPlayer);
        playCard(CardsFactory.minion, secondRow, secondPlayer);
        playCard(CardsFactory.viking, thirdRow, firstPlayer);
        playCard(CardsFactory.viking, thirdRow, secondPlayer);

        playCard(CardsFactory.healer, thirdRow, firstPlayer);

        int woodTheHealerBoostAmount = 2;
        int expectedMinionPoints = CardsFactory.minion.getPoints() + woodTheHealerBoostAmount;
        int expectedPaperPoints = CardsFactory.paper.getPoints() + woodTheHealerBoostAmount;
        int expectedVikingPoints = CardsFactory.viking.getPoints();
        int expectedBoardPoints = expectedMinionPoints + expectedPaperPoints + expectedVikingPoints + CardsFactory.healer.getPoints();

        assertEquals(expectedBoardPoints, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void testStrikingEnemyCard(){
        List<CardDisplay> deck = List.of(CardsFactory.archer, CardsFactory.viking);
        duel  = createDuel(deck);

        playCard(CardsFactory.viking, firstRow, firstPlayer);
        playCard(CardsFactory.archer, firstRow, CardsFactory.viking, secondPlayer);

        CardDisplay strikedVikingDisplay = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0);

        int fireballStrikeAmount = 3;
        assertEquals(CardsFactory.viking.getPoints() - fireballStrikeAmount,  strikedVikingDisplay.getPoints());
        assertTrue(duel.getCardsInHandDisplayOf(firstPlayer).contains(CardsFactory.archer));
        assertEquals(1, duel.getBoardPointsOf(secondPlayer));
    }

    @Test
    public void testUsingStrikingSpell() {
        List<CardDisplay> deck = List.of(CardsFactory.fireball, CardsFactory.viking);
        duel  = createDuel(deck);

        playCard(CardsFactory.viking, firstRow, firstPlayer);
        playCard(CardsFactory.fireball, thirdRow, CardsFactory.viking, secondPlayer);

        assertTrue(duel.getCardsOnBoardDisplayOf(secondPlayer, thirdRow).isEmpty());
        boolean isFireballInHand = duel.getCardsInHandDisplayOf(secondPlayer)
                .stream()
                .filter(c -> c.equals(CardsFactory.fireball))
                .findFirst()
                .orElse(null) == null;
        assertTrue(isFireballInHand);
        int actualVikingPoints_afterStrike = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0).getPoints();
        int expectedVikingPoints_afterStrike = CardsFactory.viking.getPoints() - CardsFactory.fireballStrikeAmount;
        assertEquals(expectedVikingPoints_afterStrike, actualVikingPoints_afterStrike);
    }

    @Test
    public void testCardsTargeting(){
        List<CardDisplay> deck = List.of(CardsFactory.viking, CardsFactory.paper, CardsFactory.minion, CardsFactory.warrior);
        duel = createDuel(deck);

        playCard(CardsFactory.viking, firstRow, firstPlayer);
        playCard(CardsFactory.warrior, firstRow, secondPlayer);

        playCard(CardsFactory.paper, secondRow, firstPlayer);
        playCard(CardsFactory.minion, secondRow, secondPlayer);

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
        playCard(CardsFactory.booster, secondRow, firstPlayer);
        assertTrue(duel.getPossibleTargetsOf(CardsFactory.booster, firstPlayer).isEmpty());
    }

    @Test
    public void cardWithPointsBelowOneIsRemoved() {
        List<CardDisplay> deck = List.of(CardsFactory.paper, CardsFactory.archer);
        duel = createDuel(deck);
        playCard(CardsFactory.paper, firstRow, firstPlayer);
        playCard(CardsFactory.archer, firstRow, CardsFactory.paper, secondPlayer);
        assertTrue(duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).isEmpty());
    }


    @Test
    public void testBurningAllMaxPointsCards(){
        List<CardDisplay> deck = List.of(CardsFactory.capitan, CardsFactory.conflagration);
        duel = createDuel(deck);
        playCard(CardsFactory.capitan, firstRow, firstPlayer);
        playCard(CardsFactory.capitan, firstRow, secondPlayer);
        playCard(CardsFactory.conflagration, secondRow, firstPlayer);

        int board = duel.getBoardPointsOf(firstPlayer);
        assertEquals(0, duel.getBoardPointsOf(firstPlayer));
        assertEquals(0, duel.getBoardPointsOf(secondPlayer));
    }

    @Test
    public void testDoublingCardPoints() {
        List<CardDisplay> deck = List.of(CardsFactory.doubler, CardsFactory.capitan, CardsFactory.viking);
        duel = createDuel(deck);

        playCard(CardsFactory.capitan, firstRow, firstPlayer);
        playCard(CardsFactory.capitan, firstRow, secondPlayer);
        playCard(CardsFactory.doubler, secondRow, CardsFactory.capitan, firstPlayer);

        int expectedPoints = CardsFactory.capitan.getPoints() * 2;
        int actualPoints = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0).getPoints();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testDealingDamageToWholeRow() {
        List<CardDisplay> deck = List.of(CardsFactory.rip, CardsFactory.capitan, CardsFactory.armageddon, CardsFactory.paper);
        duel = createDuel(deck);

        playCard(CardsFactory.capitan, firstRow, firstPlayer);
        playCard(CardsFactory.capitan, firstRow, secondPlayer);
        playCard(CardsFactory.armageddon, firstRow, firstPlayer);
        playCard(CardsFactory.armageddon, firstRow, secondPlayer);
        playCard(CardsFactory.paper, firstRow, firstPlayer);
        playCard(CardsFactory.paper, firstRow, secondPlayer);
        playCard(CardsFactory.rip,secondRow, firstRow ,firstPlayer );

        int firstUnitExpected = Math.max(CardsFactory.capitan.getPoints() - CardsFactory.ripRowDamageAmount, 0);
        int secondUnitExpected = Math.max(CardsFactory.armageddon.getPoints() - CardsFactory.ripRowDamageAmount, 0);
        int thirdUnitExpected = Math.max(CardsFactory.paper.getPoints() - CardsFactory.ripRowDamageAmount, 0);
        int expected = firstUnitExpected + secondUnitExpected + thirdUnitExpected;

        assertEquals(expected, duel.getBoardPointsOf(secondPlayer));
    }
    @Test
    public void testBoostingEveryTurn() {
        duel = createDuel(List.of(CardsFactory.longer, CardsFactory.paper, CardsFactory.viking));
        playCard(CardsFactory.longer, firstRow, firstPlayer);
        playCard(CardsFactory.paper, firstRow, secondPlayer);
        playCard(CardsFactory.paper, secondRow, firstPlayer);
        int expectedPoints= CardsFactory.longer.getPoints() + CardsFactory.longerBoostAmount ;
        int actualPoints = duel.getCardsOnBoardDisplayOf( firstPlayer, firstRow).get(0).getPoints();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testRainRowStatus() {
        duel = createDuel(List.of(CardsFactory.rain, CardsFactory.viking ));
        playCard(CardsFactory.viking, secondRow, firstPlayer);
        playCard(CardsFactory.rain, secondRow, secondRow, secondPlayer);
        playCard(CardsFactory.rain, secondRow, secondRow, firstPlayer);
        playCard(CardsFactory.viking, secondRow, secondPlayer);
        duel.endRoundFor(firstPlayer);
        int expected = CardsFactory.viking.getPoints() - 2*CardsFactory.rainStrikeAmount;
        int actual = duel.getBoardPointsOf(firstPlayer);
        assertEquals(expected, actual);
    }



}