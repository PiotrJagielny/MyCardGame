package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.CardDuel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.CardsServices.Cards.CardsFactory.*;
import static com.example.demo.Consts.*;
import static com.example.demo.TestsData.TestsUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestSpecificBehaviours {


    private CardDuel duel;
    private List<CardDisplay> hand1 = new ArrayList<>();
    private List<CardDisplay> hand2 = new ArrayList<>();


    public void setHands() {
        hand1 = duel.getHandOf(firstPlayer);
        hand2 = duel.getHandOf(secondPlayer);
    }
    @Test
    public void testBoostingSingleRow(){
        duel  = createDuel(List.of(leader, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1,viking), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2,viking), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1,leader), firstRow, firstPlayer);
        int expectedPoints = viking.getPoints() + leaderBoost + leader.getPoints();
        assertEquals(expectedPoints , getBoardPointsOf(firstPlayer, duel));
    }

    @Test
    public void testBoostingSingleCard(){
        duel = createDuel(List.of(booster, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, viking), firstRow, secondPlayer);
        playCardWithCardTargeting(duel, findByName(hand1, booster), secondRow, findByName(hand1,viking), firstPlayer);
        int singleCardBoostAmount = 3;
        CardDisplay boostedCard = findByName(duel.getRowOf(firstPlayer, firstRow), viking);
        assertEquals(viking.getPoints() + singleCardBoostAmount, boostedCard.getPoints());
    }

    @Test
    public void testBoostingWholeBoard(){
        duel  = createDuel(List.of(healer, paper, minion, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, paper), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, paper), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, minion), secondRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, minion), secondRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, viking), thirdRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, viking), thirdRow, secondPlayer);

        playCardWithoutTargeting(duel, findByName(hand1, healer), thirdRow, firstPlayer);

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

        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow,
                findByName(hand1, viking), secondPlayer);

        CardDisplay strikedVikingDisplay = duel.getRowOf(firstPlayer, firstRow).get(0);

        assertEquals(viking.getPoints() - archerDamage,  strikedVikingDisplay.getPoints());
        assertTrue(duel.getHandOf(firstPlayer).contains(archer));
        assertEquals(1, getBoardPointsOf(secondPlayer, duel));
    }

    @Test
    public void testUsingStrikingSpell() {
        duel  = createDuel(List.of(fireball, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, firstPlayer);
        playSpecialCardWithCardTargeting(duel, findByName(hand2,fireball), findByName(hand1, viking), secondPlayer);

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

        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, warrior), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, paper), secondRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, minion), secondRow, secondPlayer);

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
        playCardWithoutTargeting(duel, findByName(hand1, booster), secondRow, firstPlayer);
        assertTrue(duel.getPossibleTargetsOf(booster, firstPlayer).isEmpty());
    }

    @Test
    public void cardWithPointsBelowOneIsRemoved() {
        duel = createDuel(List.of(paper, archer));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, paper), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow, findByName(hand1, paper), secondPlayer);
        assertTrue(duel.getRowOf(firstPlayer, firstRow).isEmpty());
    }


    @Test
    public void testBurningAllMaxPointsCards(){
        duel = createDuel(List.of(capitan, conflagration));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, capitan), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, capitan), firstRow, secondPlayer);
        playSpecialCardWithoutTargeting(duel, findByName(hand1,conflagration), firstPlayer);

        int board = getBoardPointsOf(firstPlayer, duel);
        assertEquals(0, getBoardPointsOf(firstPlayer, duel));
        assertEquals(0, getBoardPointsOf(secondPlayer, duel));
    }

    @Test
    public void testDoublingCardPoints() {
        duel = createDuel(List.of(doubler, capitan, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, capitan), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, capitan), firstRow, secondPlayer);
        playCardWithCardTargeting(duel, findByName(hand1, doubler), secondRow, findByName(hand1,capitan ), firstPlayer);

        int expectedPoints = capitan.getPoints() * 2;
        int actualPoints = duel.getRowOf(firstPlayer, firstRow).get(0).getPoints();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testDealingDamageToWholeRow() {
        duel = createDuel(List.of(rip, capitan, armageddon, paper));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, capitan), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, capitan), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, armageddon), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, armageddon), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, paper), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, paper), firstRow, secondPlayer);
        playSpecialCardWithRowTargeting(duel, findByName(hand1,rip), firstRow ,firstPlayer );

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
        playCardWithoutTargeting(duel, findByName(hand1, longer), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, paper), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, paper), secondRow, firstPlayer);
        int expectedPoints= longer.getPoints() + 2* longerBoost;
        int actualPoints = duel.getRowOf( firstPlayer, firstRow).get(0).getPoints();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testRainRowStatus() {
        duel = createDuel(List.of(rain, viking, paper ));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, viking), firstRow, secondPlayer);
        playSpecialCardWithRowTargeting(duel, findByName(hand1, rain), firstRow,firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, paper), secondRow, secondPlayer);

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
        playSpecialCardWithRowTargeting(duel, findByName(hand1, rain), firstRow, firstPlayer);
        playSpecialCardWithoutTargeting(duel, findByName(hand2, clearSky), secondPlayer);
        String expectedRowStatus = "";
        String actualRowStatus = duel.getRowStatusOf(secondPlayer, firstRow);
        assertEquals(expectedRowStatus, actualRowStatus);
    }
    @Test
    public void testPlyingCardFromDeck() {
        duel = createDuel(List.of(priest, paper, capitan, hotdog, viking, warrior));
        setHands();
        CardDisplay vikingFromDeck = findByName(duel.getDeckOf(firstPlayer), viking);
        CardDisplay nextCardInChain = playCardWithCardTargeting(
                duel, findByName(hand1, priest) , firstRow,vikingFromDeck , firstPlayer);

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

        playCardWithoutTargeting(duel, findByName(hand1, capitan), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findByName(hand2, sharpshooter), firstRow, findByName(hand1, capitan), secondPlayer);

        assertEquals(sharpshooter.getPoints() ,duel.getRowPointsOf(secondPlayer, firstRow));

        duel = createDuel(List.of(sharpshooter, minion, capitan));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, minion), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findByName(hand2, sharpshooter), firstRow, findByName(hand1,minion), secondPlayer);

        int expected = sharpshooter.getPoints() + sharpshooterSelfBoost;
        assertEquals(expected,duel.getRowPointsOf(secondPlayer, firstRow));
    }

    @Test
    public void testSpawningNewUnitsOnDeath() {
        //Spawn on strike
        duel = createDuel(List.of(cow, archer, viking));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, cow), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow, findByName(hand1,cow), secondPlayer);
        assertEquals(chort, duel.getRowOf(firstPlayer, firstRow).get(0));


        //Spawn on new round
        duel = createDuel(List.of(cow, archer, viking));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1,cow), firstRow, firstPlayer);
        duel.endRoundFor(secondPlayer);
        duel.endRoundFor(firstPlayer);
        assertEquals(chort, duel.getRowOf(firstPlayer, firstRow).get(0));

        //Spawn on burn
        duel = createDuel(List.of(cow, conflagration));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1,cow) , firstRow,firstPlayer);
        playSpecialCardWithoutTargeting(duel, findByName(hand2, conflagration), secondPlayer);
        CardDisplay chortSpawned = duel.getRowOf(firstPlayer, firstRow).get(0);
        assertEquals(chort, chortSpawned);
    }

    @Test
    public void testDealingDamageEveryTwoTurns() {

        duel = createDuel(List.of(trebuchet, viking));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, trebuchet), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, viking), firstRow, secondPlayer);
        duel.endRoundFor(firstPlayer);

        int expectedPoints = viking.getPoints() - trebuchetDamage;
        assertEquals(expectedPoints, duel.getRowOf(secondPlayer, firstRow).get(0).getPoints());

    }

    @Test
    public void testBoostingCardEveryTwoTurns() {
        duel = createDuel(List.of(goodPerson, viking));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1,goodPerson), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, viking), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1,viking), firstRow, firstPlayer);

        int expectedPoints = viking.getPoints() + goodPersonBoost;
        int actualPoints = duel.getRowOf(firstPlayer, firstRow).stream().filter(c -> c.equals(viking))
                .findFirst().get().getPoints();
        assertEquals(expectedPoints,actualPoints);


    }

    @Test
    public void testBoostingByNumberOfCardsOnGraveyard() {
        duel = createDuel(List.of(gravedigger, archer, minion));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, minion), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow, findByName(hand1,minion), secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, gravedigger), firstRow, firstPlayer);

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
        playCardWithoutTargeting(duel, findByName(hand1, minion), firstRow, firstPlayer);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow, findByName(hand1, minion), secondPlayer);

        CardDisplay playChainCard = playCardWithCardTargeting(
                duel, findByName(hand1, witch), firstRow, findByName(hand1, minion), firstPlayer
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
        playCardWithoutTargeting(duel, findByName(hand1, wildRoam), firstRow, firstPlayer);
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

        playCardWithoutTargeting(duel, findByName(hand1,viking), firstRow, firstPlayer);
        playCardWithCardTargeting(
                duel, findByName(hand2, sharpshooter), firstRow, findByName(hand1, viking), secondPlayer);
        playCardWithCardTargeting(
                duel, findByName(hand1, sharpshooter), firstRow, findByName(hand2, sharpshooter), firstPlayer);

        CardDisplay loweredSharpShooter = duel.getRowOf(secondPlayer, firstRow).get(0);
        CardDisplay sharpshooterChain = playCardWithCardTargeting(duel, findByName(hand2, supplier), secondRow,loweredSharpShooter, secondPlayer);

        playCardWithCardTargeting(duel, sharpshooterChain, thirdRow, findByName(hand1, viking), secondPlayer);

        assertEquals(sharpshooter, duel.getRowOf(secondPlayer, thirdRow).get(0));
        assertEquals(sharpshooter.getPoints(), duel.getRowOf(secondPlayer, thirdRow).get(0).getPoints());

        int actualVikingPoints= duel.getRowOf(firstPlayer, firstRow).get(0).getPoints();
        int expectedVikingPoints = viking.getPoints() - 2* sharpshooterDamage;
        assertEquals(expectedVikingPoints, actualVikingPoints);
    }

    @Test
    public void testCallingCardFromDeckAfterTurns() {
        duel = createDuel(List.of(breaker, viking, minion, paper, warrior, armageddon, capitan, breaker));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, breaker), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, capitan), secondRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, minion), secondRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, viking), secondRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1, paper), secondRow, firstPlayer);

        int expectedUnitsOnFirstRow = 2;
        assertEquals(expectedUnitsOnFirstRow, duel.getRowOf(firstPlayer, firstRow).size());

    }

    @Test
    public void testKillingAllMinPointsCards() {
        duel = createDuel(List.of(epidemic, minion, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1,minion), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2,minion), firstRow, secondPlayer);
        playCardWithoutTargeting(duel, findByName(hand1,viking), firstRow, firstPlayer);
        playSpecialCardWithoutTargeting(duel, findByName(hand2,epidemic), secondPlayer);

        int expectedCardsLeft = 1;
        int actualCardsLeft = duel.getRowOf(firstPlayer, firstRow).size() ;
        assertEquals(expectedCardsLeft, actualCardsLeft );
    }
    @Test
    public void testSpyThatDrawsCard() {
        duel = createDuel(List.of(spy, warrior, minion, paper, viking, capitan, wildRoam, wildRoam,archer, priest, witch));
        setHands();

        int initialHandSize = duel.getHandOf(firstPlayer).size();
        playCardWithoutTargeting(duel, findByName(hand1, spy), firstRow, firstPlayer);
        int handSizeAfterPlayingSpy= duel.getHandOf(firstPlayer).size();

        assertEquals(spy.getPoints(), duel.getRowPointsOf(secondPlayer, firstRow));
        assertEquals(0, duel.getRowPointsOf(firstPlayer, firstRow));
        assertEquals(initialHandSize, handSizeAfterPlayingSpy);

    }

    @Test
    public void testBurningOppositeRow() {
        duel = createDuel(List.of(blueFire, warrior, capitan, armageddon));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, warrior), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, blueFire), firstRow, secondPlayer);

        assertEquals(1,duel.getRowOf(firstPlayer, firstRow).size() );

        playCardWithoutTargeting(duel, findByName(hand1, capitan), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, armageddon), firstRow, secondPlayer);

        playCardWithoutTargeting(duel, findByName(hand1, armageddon), firstRow, firstPlayer);
        playCardWithoutTargeting(duel, findByName(hand2, capitan), firstRow, secondPlayer);

        playCardWithoutTargeting(duel, findByName(hand1, blueFire), firstRow, firstPlayer);
        assertEquals(2, duel.getRowOf(secondPlayer, firstRow).size());


    }

}