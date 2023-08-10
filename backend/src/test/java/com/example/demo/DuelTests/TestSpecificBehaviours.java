package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Duel.CardDuel;
import com.example.demo.TestsData.TestsUtils;
import org.junit.jupiter.api.Test;

import static com.example.demo.CardsServices.Cards.CardsFactory.*;
import static com.example.demo.TestsData.TestsUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.Consts.*;
import static org.junit.jupiter.api.Assertions.*;

class TestSpecificBehaviours {


    private CardDuel duel;
    private List<CardDisplay> hand11 = new ArrayList<>();
    private List<CardDisplay> hand12 = new ArrayList<>();


    public void setHands() {
        hand11 = duel.getHandOf(firstPlayer);
        hand12 = duel.getHandOf(secondPlayer);
    }
    @Test
    public void testBoostingSingleRow(){
        duel  = createDuel(List.of(leader, viking));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11,viking), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12,viking), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand11,leader), firstRow, firstPlayer);
        int expectedPoints = viking.getPoints() + leaderBoost + leader.getPoints();
        assertEquals(expectedPoints , getBoardPointsOf(firstPlayer, duel));
    }

    @Test
    public void testBoostingSingleCard(){
        duel = createDuel(List.of(booster, viking));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11, viking), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, viking), firstRow, secondPlayer);
        playCardWithCardTargeting(duel, findCardByName(hand11, booster), secondRow, findCardByName(hand11,viking), firstPlayer);
        int singleCardBoostAmount = 3;
        CardDisplay boostedCard = findCardByName(duel.getRowOf(firstPlayer, firstRow), viking);
        assertEquals(viking.getPoints() + singleCardBoostAmount, boostedCard.getPoints());
    }

    @Test
    public void testBoostingWholeBoard(){
        duel  = createDuel(List.of(healer, paper, minion, viking));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11, paper), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, paper), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand11, minion), secondRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, minion), secondRow, secondPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand11, viking), thirdRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, viking), thirdRow, secondPlayer);

        playCardWithoutTargeting(duel, findCardByName(hand11, healer), thirdRow, firstPlayer);

        int expectedMinionPoints = minion.getPoints() + healerBoost;
        int expectedPaperPoints = paper.getPoints() + healerBoost;
        int expectedVikingPoints = viking.getPoints();
        int expectedBoardPoints = expectedMinionPoints + expectedPaperPoints + expectedVikingPoints + healer.getPoints();

        assertEquals(expectedBoardPoints, getBoardPointsOf(firstPlayer, duel));
    }

    @Test
    public void testStrikingEnemyCard(){
        duel  = createDuel(List.of(archer, viking));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11, viking), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findCardByName(hand12, archer), firstRow,
                findCardByName(hand11, viking), secondPlayer);

        CardDisplay strikedVikingDisplay = duel.getRowOf(firstPlayer, firstRow).get(0);

        int fireballStrikeAmount = 3;
        assertEquals(viking.getPoints() - fireballStrikeAmount,  strikedVikingDisplay.getPoints());
        assertTrue(duel.getHandOf(firstPlayer).contains(archer));
        assertEquals(1, getBoardPointsOf(secondPlayer, duel));
    }

    @Test
    public void testUsingStrikingSpell() {
        duel  = createDuel(List.of(fireball, viking));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11, viking), firstRow, firstPlayer);
        playSpecialCardWithCardTargeting(duel, findCardByName(hand12,fireball), findCardByName(hand11, viking), secondPlayer);

        assertTrue(duel.getRowOf(secondPlayer, thirdRow).isEmpty());
        boolean isFireballInHand = duel.getHandOf(secondPlayer)
                .stream()
                .filter(c -> c.equals(fireball))
                .findFirst()
                .orElse(null) == null;
        assertTrue(isFireballInHand);
        int actualVikingPoints_afterStrike = duel.getRowOf(firstPlayer, firstRow).get(0).getPoints();
        int expectedVikingPoints_afterStrike = viking.getPoints() - fireballDamage;
        assertEquals(expectedVikingPoints_afterStrike, actualVikingPoints_afterStrike);
    }

    @Test
    public void testCardsTargeting(){
        duel = createDuel(List.of(viking, paper, minion, warrior));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11, viking), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, warrior), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand11, paper), secondRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, minion), secondRow, secondPlayer);

        List<CardDisplay> possibleTargetsOfBooster = duel.getPossibleTargetsOf(booster, firstPlayer);
        assertTrue(possibleTargetsOfBooster.contains(viking));
        assertTrue(possibleTargetsOfBooster.contains(paper));

        possibleTargetsOfBooster = duel.getPossibleTargetsOf(booster, secondPlayer);
        assertTrue(possibleTargetsOfBooster.isEmpty());

        List<CardDisplay> possibleTargetsOfFireball = duel.getPossibleTargetsOf(archer, firstPlayer);
        assertTrue(possibleTargetsOfFireball.contains(warrior));
        assertTrue(possibleTargetsOfFireball.contains(minion));
    }

    @Test
    public void testCardsTargeting_ifThereIsNoTarget(){
        duel = createDuel(List.of(viking, booster, minion, warrior));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11, booster), secondRow, firstPlayer);
        assertTrue(duel.getPossibleTargetsOf(booster, firstPlayer).isEmpty());
    }

    @Test
    public void cardWithPointsBelowOneIsRemoved() {
        duel = createDuel(List.of(paper, archer));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11, paper), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findCardByName(hand12, archer), firstRow, findCardByName(hand11, paper), secondPlayer);
        assertTrue(duel.getRowOf(firstPlayer, firstRow).isEmpty());
    }


    @Test
    public void testBurningAllMaxPointsCards(){
        duel = createDuel(List.of(capitan, conflagration));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11, capitan), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, capitan), firstRow, secondPlayer);
        playSpecialCardWithoutTargeting(duel, findCardByName(hand11,conflagration), firstPlayer);

        int board = getBoardPointsOf(firstPlayer, duel);
        assertEquals(0, getBoardPointsOf(firstPlayer, duel));
        assertEquals(0, getBoardPointsOf(secondPlayer, duel));
    }

    @Test
    public void testDoublingCardPoints() {
        duel = createDuel(List.of(doubler, capitan, viking));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11, capitan), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, capitan), firstRow, secondPlayer);
        playCardWithCardTargeting(duel, findCardByName(hand11, doubler), secondRow, findCardByName(hand11,capitan ), firstPlayer);

        int expectedPoints = capitan.getPoints() * 2;
        int actualPoints = duel.getRowOf(firstPlayer, firstRow).get(0).getPoints();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testDealingDamageToWholeRow() {
        duel = createDuel(List.of(rip, capitan, armageddon, paper));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11, capitan), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, capitan), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand11, armageddon), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, armageddon), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand11, paper), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, paper), firstRow, secondPlayer);
        playSpecialCardWithRowTargeting(duel, findCardByName(hand11,rip), firstRow ,firstPlayer );

        int firstUnitExpected = Math.max(capitan.getPoints() - ripDamage, 0);
        int secondUnitExpected = Math.max(armageddon.getPoints() - ripDamage, 0);
        int thirdUnitExpected = Math.max(paper.getPoints() - ripDamage, 0);
        int expected = firstUnitExpected + secondUnitExpected + thirdUnitExpected;

        assertEquals(expected, getBoardPointsOf(secondPlayer, duel));
    }
    @Test
    public void testBoostingEveryTurn() {
        duel = createDuel(List.of(longer, paper, viking));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11, longer), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, paper), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand11, paper), secondRow, firstPlayer);
        int expectedPoints= longer.getPoints() + 2* longerBoost;
        int actualPoints = duel.getRowOf( firstPlayer, firstRow).get(0).getPoints();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testRainRowStatus() {
        duel = createDuel(List.of(rain, viking, paper ));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11, viking), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, viking), firstRow, secondPlayer);
        playSpecialCardWithRowTargeting(duel, findCardByName(hand11, rain), firstRow,firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, paper), secondRow, secondPlayer);

        int expectedFirstPlayerPoints = viking.getPoints() - rainDamage ;
        assertEquals(expectedFirstPlayerPoints, duel.getRowPointsOf(firstPlayer, firstRow));


        int expectedSecondPlayerPoints = viking.getPoints() - rainDamage ;
        assertEquals(expectedSecondPlayerPoints, duel.getRowPointsOf(secondPlayer, firstRow));

        duel.endRoundFor(secondPlayer);
        assertEquals("", duel.getRowStatusOf(firstPlayer, secondRow));
    }

    @Test
    public void testClearingAllRowsStatus() {
        duel = createDuel(List.of(rain, viking, paper, clearSky));
        setHands();
        playSpecialCardWithRowTargeting(duel, findCardByName(hand11, rain), firstRow, firstPlayer);
        playSpecialCardWithoutTargeting(duel, findCardByName(hand12, clearSky), secondPlayer);
        String expectedRowStatus = "";
        String actualRowStatus = duel.getRowStatusOf(secondPlayer, firstRow);
        assertEquals(expectedRowStatus, actualRowStatus);
    }
    @Test
    public void testPlyingCardFromDeck() {
        duel = createDuel(List.of(priest, paper, capitan, hotdog, viking, warrior));
        setHands();
        CardDisplay vikingFromDeck = findCardByName(duel.getDeckOf(firstPlayer), viking);
        CardDisplay nextCardInChain = playCardWithCardTargeting(
                duel,findCardByName(hand11, priest) , firstRow,vikingFromDeck , firstPlayer);

        int cardsInDeck = duel.getDeckOf(firstPlayer).size();
        CardDisplay vikingPlayed_noChainCard= playCardWithoutTargeting(duel, nextCardInChain,firstRow, firstPlayer);
        int cardsInDeckAfterChainPlay = duel.getDeckOf(firstPlayer).size();

        int expectedPoints = viking.getPoints() + priest.getPoints();
        int actualPoints = duel.getRowPointsOf(firstPlayer, firstRow);
        assertEquals(new CardDisplay(), vikingPlayed_noChainCard);
        assertEquals(expectedPoints, actualPoints);
        assertEquals(cardsInDeck, cardsInDeckAfterChainPlay + 1);

    }

    @Test
    public void dealDamageToCard_boostIfItDies() {
        duel = createDuel(List.of(sharpshooter, paper, capitan));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11, capitan), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findCardByName(hand12, sharpshooter), firstRow, findCardByName(hand11, capitan), secondPlayer);

        assertEquals(sharpshooter.getPoints() ,duel.getRowPointsOf(secondPlayer, firstRow));

        duel = createDuel(List.of(sharpshooter, minion, capitan));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11, minion), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findCardByName(hand12, sharpshooter), firstRow, findCardByName(hand11,minion), secondPlayer);

        int expected = sharpshooter.getPoints() + sharpshooterSelfBoost;
        assertEquals(expected,duel.getRowPointsOf(secondPlayer, firstRow));
    }

    @Test
    public void testSpawningNewUnitsOnDeath() {
        //Spawn on strike
        duel = createDuel(List.of(cow, archer, viking));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11, cow), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findCardByName(hand12, archer), firstRow, findCardByName(hand11,cow), secondPlayer);
        assertEquals(chort, duel.getRowOf(firstPlayer, firstRow).get(0));


        //Spawn on new round
        duel = createDuel(List.of(cow, archer, viking));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11,cow), firstRow, firstPlayer);
        duel.endRoundFor(secondPlayer);
        duel.endRoundFor(firstPlayer);
        assertEquals(chort, duel.getRowOf(firstPlayer, firstRow).get(0));

        //Spawn on burn
        duel = createDuel(List.of(cow, conflagration));
        setHands();
        playCardWithoutTargeting(duel,findCardByName(hand11,cow) , firstRow,firstPlayer);
        playSpecialCardWithoutTargeting(duel, findCardByName(hand12, conflagration), secondPlayer);
        CardDisplay chortSpawned = duel.getRowOf(firstPlayer, firstRow).get(0);
        assertEquals(chort, chortSpawned);
    }

    @Test
    public void testDealingDamageEveryTwoTurns() {

        duel = createDuel(List.of(trebuchet, viking));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11, trebuchet), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, viking), firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);

        int expectedPoints = viking.getPoints() - trebuchetDamage;
        assertEquals(expectedPoints, duel.getRowOf(secondPlayer, firstRow).get(0).getPoints());

    }

    @Test
    public void testBoostingCardEveryTwoTurns() {
        duel = createDuel(List.of(goodPerson, viking));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11,goodPerson), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand12, viking), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand11,viking), firstRow, firstPlayer);

        int expectedPoints = viking.getPoints() + goodPersonBoost;
        int actualPoints = duel.getRowOf(firstPlayer, firstRow).stream().filter(c -> c.equals(viking))
                .findFirst().get().getPoints();
        assertEquals(expectedPoints,actualPoints);


    }

    @Test
    public void testBoostingByNumberOfCardsOnGraveyard() {
        duel = createDuel(List.of(gravedigger, archer, minion));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11, minion), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findCardByName(hand12, archer), firstRow, findCardByName(hand11,minion), secondPlayer);
        playCardWithoutTargeting(duel, findCardByName(hand11, gravedigger), firstRow, firstPlayer);

        int cardsOnGraveyard = 1;
        int expectedPoints = gravedigger.getPoints() + cardsOnGraveyard;
        int actualPoints = duel.getRowOf(firstPlayer, firstRow)
                .stream()
                .filter(c-> c.equals(gravedigger) )
                .findFirst().get().getPoints();

        assertEquals(expectedPoints, actualPoints);

    }

    @Test
    public void testCardResurrectionFromGraveyard() {
        duel = createDuel(List.of(witch, archer, minion));
        setHands();
        playCardWithoutTargeting(duel, findCardByName(hand11, minion), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findCardByName(hand12, archer), firstRow, findCardByName(hand11, minion), secondPlayer);

        CardDisplay playChainCard = playCardWithCardTargeting(
                duel, findCardByName(hand11, witch), firstRow, findCardByName(hand11, minion), firstPlayer
        );
        assertEquals(minion, playChainCard);
        playCardWithoutTargeting(duel, playChainCard, firstRow, firstPlayer);

        CardDisplay playedChainCard = duel.getRowOf(firstPlayer, firstRow)
                .stream().filter(c->c.equals(playChainCard)).findFirst().orElse(new CardDisplay());

        assertEquals(playedChainCard, playChainCard);
    }
    @Test
    public void testCallingAllCardCopiesFromDeck() {
        duel = createDuel(List.of(wildRoam, wildRoam, viking, knight, capitan, archer,
                armageddon, wildRoam));
        setHands();

        int onePlayedFromHand = 1;
        int cardOccurences = onePlayedFromHand + duel.getDeckOf(firstPlayer)
                .stream()
                .filter(c -> c.equals(wildRoam))
                .collect(Collectors.toList()).size();
        playCardWithoutTargeting(duel, findCardByName(hand11, wildRoam), firstRow, firstPlayer);
        assertEquals(cardOccurences, duel.getRowOf(firstPlayer, firstRow).size());


        int cardsOnHandLeft = duel.getHandOf(firstPlayer).stream()
                .filter(c -> c.equals(wildRoam))
                .collect(Collectors.toList()).size();
        assertEquals(1, cardsOnHandLeft);

    }

    @Test
    public void testPlayingCopyOfCardOnBoardFromDeck() {
        duel = createDuel(List.of(supplier, sharpshooter, viking, capitan, warrior,
                minion, paper, thunder, sharpshooter));
        setHands();

        playCardWithoutTargeting(duel, findCardByName(hand11,viking), firstRow, firstPlayer);
        playCardWithCardTargeting(
                duel, findCardByName(hand12, sharpshooter), firstRow, findCardByName(hand11, viking), secondPlayer);
        playCardWithCardTargeting(
                duel, findCardByName(hand11, sharpshooter), firstRow, findCardByName(hand12, sharpshooter), firstPlayer);

        CardDisplay loweredSharpShooter = duel.getRowOf(secondPlayer, firstRow).get(0);
        CardDisplay sharpshooterChain = playCardWithCardTargeting(duel, findCardByName(hand12, supplier), secondRow,loweredSharpShooter, secondPlayer);

        playCardWithCardTargeting(duel, sharpshooterChain, thirdRow, findCardByName(hand11, viking), secondPlayer);

        assertEquals(sharpshooter, duel.getRowOf(secondPlayer, thirdRow).get(0));
        assertEquals(sharpshooter.getPoints(), duel.getRowOf(secondPlayer, thirdRow).get(0).getPoints());

        int actualVikingPoints= duel.getRowOf(firstPlayer, firstRow).get(0).getPoints();
        int expectedVikingPoints = viking.getPoints() - 2* sharpshooterDamage;
        assertEquals(expectedVikingPoints, actualVikingPoints);
    }




}