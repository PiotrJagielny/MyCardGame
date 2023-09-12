package com.example.demo.DuelTests;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Duel.ClientAPI.CardDuel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.Cards.CardsFactory.*;
import static com.example.demo.Consts.*;
import static com.example.demo.TestsData.TestsUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class TestSpecificBehaviours {


    private CardDuel duel;
    private List<CardDisplay> hand1 = new ArrayList<>();
    private List<CardDisplay> hand2 = new ArrayList<>();


    public void setHands() {
        hand1 = duel.getHandOf(player1);
        hand2 = duel.getHandOf(player2);
    }
    @Test
    public void testBoostingSingleRow(){
        duel  = createDuel(List.of(leader, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1,viking), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2,viking), firstRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1,leader), firstRow, player1);
        int expectedPoints = viking.getPoints() + leaderBoost + leader.getPoints();
        assertEquals(expectedPoints , getBoardPointsOf(player1, duel));
    }

    @Test
    public void testBoostingSingleCard(){
        duel = createDuel(List.of(booster, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, viking), firstRow, player2);
        playCardWithCardTargeting(duel, findByName(hand1, booster), secondRow, findByName(board(duel, player1),viking), player1);
        int singleCardBoostAmount = 3;
        CardDisplay boostedCard = findByName(duel.getRowOf(player1, firstRow), viking);
        assertEquals(viking.getPoints() + singleCardBoostAmount, boostedCard.getPoints());
    }

    @Test
    public void testBoostingWholeBoard(){
        duel  = createDuel(List.of(healer, paper, minion, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, paper), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, paper), firstRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1, minion), secondRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, minion), secondRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1, viking), thirdRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, viking), thirdRow, player2);

        playCardWithoutTargeting(duel, findByName(hand1, healer), thirdRow, player1);

        int expectedMinionPoints = minion.getPoints() + healerBoost;
        int expectedPaperPoints = paper.getPoints() + healerBoost;
        int expectedVikingPoints = viking.getPoints();
        int expectedBoardPoints = expectedMinionPoints + expectedPaperPoints + expectedVikingPoints + healer.getPoints();

        assertEquals(expectedBoardPoints, getBoardPointsOf(player1, duel));
    }

    @Test
    public void testStrikingEnemyCard(){
        duel  = createDuel(List.of(archer, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow,
                findByName(board(duel,player1), viking), player2);

        CardDisplay strikedVikingDisplay = duel.getRowOf(player1, firstRow).get(0);

        assertEquals(viking.getPoints() - archerDamage,  strikedVikingDisplay.getPoints());
        assertTrue(duel.getHandOf(player1).contains(archer));
        assertEquals(1, getBoardPointsOf(player2, duel));
    }

    @Test
    public void testUsingStrikingSpell() {
        duel  = createDuel(List.of(fireball, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, player1);
        playSpecialCardWithCardTargeting(duel, findByName(hand2,fireball), findByName(board(duel,player1), viking), player2);

        assertTrue(duel.getRowOf(player2, thirdRow).isEmpty());
        boolean isFireballInHand = duel.getHandOf(player2)
                .stream()
                .filter(c -> c.equals(fireball))
                .findFirst()
                .orElse(null) == null;
        assertTrue(isFireballInHand);
        int actualVikingPoints_afterStrike = duel.getRowOf(player1, firstRow).get(0).getPoints();
        int expectedVikingPoints_afterStrike = viking.getPoints() - fireballDamage;
        assertEquals(expectedVikingPoints_afterStrike, actualVikingPoints_afterStrike);
    }

    @Test
    public void testCardsTargeting(){
        duel = createDuel(List.of(viking, paper, minion, warrior));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, warrior), firstRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1, paper), secondRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, minion), secondRow, player2);

        List<CardDisplay> possibleTargetsOfBooster = duel.getPossibleTargetsOf(booster, player1);
        assertTrue(possibleTargetsOfBooster.contains(viking));
        assertTrue(possibleTargetsOfBooster.contains(paper));

        possibleTargetsOfBooster = duel.getPossibleTargetsOf(booster, player2);
        assertTrue(possibleTargetsOfBooster.isEmpty());

        List<CardDisplay> possibleTargetsOfFireball = duel.getPossibleTargetsOf(archer, player1);
        assertTrue(possibleTargetsOfFireball.contains(warrior));
        assertTrue(possibleTargetsOfFireball.contains(minion));
    }

    @Test
    public void cardWithPointsBelowOneIsRemoved() {
        duel = createDuel(List.of(paper, archer));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, paper), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow, findByName(board(duel,player1), paper), player2);
        assertTrue(duel.getRowOf(player1, firstRow).isEmpty());
    }


    @Test
    public void testBurningAllMaxPointsCards(){
        duel = createDuel(List.of(capitan, conflagration, giant));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, giant), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, capitan), firstRow, player2);
        playSpecialCardWithoutTargeting(duel, findByName(hand1,conflagration), player1);

        int board = getBoardPointsOf(player1, duel);
        assertEquals(giant.getPoints(), getBoardPointsOf(player1, duel));
        assertEquals(0, getBoardPointsOf(player2, duel));
    }

    @Test
    public void testDoublingCardPoints() {
        duel = createDuel(List.of(doubler, capitan, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, capitan), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, capitan), firstRow, player2);
        playCardWithCardTargeting(duel, findByName(hand1, doubler), secondRow, findByName(board(duel,player1),capitan), player1);

        int expectedPoints = capitan.getPoints() * 2;
        int actualPoints = duel.getRowOf(player1, firstRow).get(0).getPoints();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testDealingDamageToWholeRow() {
        duel = createDuel(List.of(rip, capitan, armageddon, paper));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, capitan), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, capitan), firstRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1, armageddon), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, armageddon), firstRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1, paper), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, paper), firstRow, player2);
        playSpecialCardWithRowTargeting(duel, findByName(hand1,rip), firstRow , player1);

        int firstUnitExpected = Math.max(capitan.getPoints() - ripDamage, 0);
        int secondUnitExpected = Math.max(armageddon.getPoints() - ripDamage, 0);
        int thirdUnitExpected = Math.max(paper.getPoints() - ripDamage, 0);
        int expected = firstUnitExpected + secondUnitExpected + thirdUnitExpected;

        assertEquals(expected, getBoardPointsOf(player2, duel));
    }

    @Test
    public void testBoostingEveryTurn() {
        duel = createDuel(List.of(longer, paper, viking));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, longer), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, paper), firstRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1, paper), secondRow, player1);
        int expectedPoints= longer.getPoints() + 2* longerBoost;
        int actualPoints = duel.getRowOf(player1, firstRow).get(0).getPoints();
        assertEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testRainRowStatus() {
        duel = createDuel(List.of(rain, viking, paper ));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, viking), firstRow, player2);
        playSpecialCardWithRowTargeting(duel, findByName(hand1, rain), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, paper), secondRow, player2);

        int expectedFirstPlayerPoints = viking.getPoints() - rainDamage ;
        assertEquals(expectedFirstPlayerPoints, duel.getRowPointsOf(player1, firstRow));


        int expectedSecondPlayerPoints = viking.getPoints() - rainDamage ;
        assertEquals(expectedSecondPlayerPoints, duel.getRowPointsOf(player2, firstRow));

        duel.endRoundFor(player2);
        assertEquals("", duel.getRowStatusOf(player1, secondRow));
    }

    @Test
    public void testClearingAllRowsStatus() {
        duel = createDuel(List.of(rain, viking, paper, clearSky));
        setHands();
        playSpecialCardWithRowTargeting(duel, findByName(hand1, rain), firstRow, player1);
        playSpecialCardWithoutTargeting(duel, findByName(hand2, clearSky), player2);
        String expectedRowStatus = "";
        String actualRowStatus = duel.getRowStatusOf(player2, firstRow);
        assertEquals(expectedRowStatus, actualRowStatus);
    }
    @Test
    public void testPlyingCardFromDeck() {
        duel = createDuel(List.of(priest, paper, capitan, hotdog,wildRoam, wildRoam, wildRoam, warrior, viking, warrior));
        setHands();
        CardDisplay vikingFromDeck = findByName(duel.getDeckOf(player1), viking);
        CardDisplay nextCardInChain = playCardWithCardTargeting(
                duel, findByName(hand1, priest) , firstRow,vikingFromDeck , player1);

        int cardsInDeck = duel.getDeckOf(player1).size();
        CardDisplay vikingPlayed_noChainCard= playCardWithoutTargeting(duel, nextCardInChain,firstRow, player1);
        int cardsInDeckAfterChainPlay = duel.getDeckOf(player1).size();

        int expectedPoints = viking.getPoints() + priest.getPoints();
        int actualPoints = duel.getRowPointsOf(player1, firstRow);
        assertEquals(new CardDisplay(), vikingPlayed_noChainCard);
        assertEquals(expectedPoints, actualPoints);
        assertEquals(cardsInDeck, cardsInDeckAfterChainPlay + 1);

    }

    @Test
    public void dealDamageToCard_boostIfItDies() {
        duel = createDuel(List.of(sharpshooter, paper, capitan));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, capitan), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, sharpshooter), firstRow,
                findByName(board(duel,player1), capitan), player2);

        assertEquals(sharpshooter.getPoints() ,duel.getRowPointsOf(player2, firstRow));

        duel = createDuel(List.of(sharpshooter, minion, capitan));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, minion), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, sharpshooter), firstRow,
                findByName(board(duel,player1),minion), player2);

        int expected = sharpshooter.getPoints() + sharpshooterSelfBoost;
        assertEquals(expected,duel.getRowPointsOf(player2, firstRow));
    }

    @Test
    public void testSpawningNewUnitsOnDeath() {
        //Spawn on strike
        duel = createDuel(List.of(cow, archer, viking));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, cow), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow,
                findByName(board(duel,player1),cow), player2);
        assertEquals(chort, duel.getRowOf(player1, firstRow).get(0));


        //Spawn on new round
        duel = createDuel(List.of(cow, archer, viking));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1,cow), firstRow, player1);
        duel.endRoundFor(player2);
        duel.endRoundFor(player1);
        assertEquals(chort, duel.getRowOf(player1, firstRow).get(0));

        //Spawn on burn
        duel = createDuel(List.of(cow, conflagration));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1,cow) , firstRow, player1);
        playSpecialCardWithoutTargeting(duel, findByName(hand2, conflagration), player2);
        CardDisplay chortSpawned = duel.getRowOf(player1, firstRow).get(0);
        assertEquals(chort, chortSpawned);
    }

    @Test
    public void testDealingDamageEveryTwoTurns() {

        duel = createDuel(List.of(trebuchet, viking));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, trebuchet), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, viking), firstRow, player2);
        duel.endRoundFor(player1);

        int expectedPoints = viking.getPoints() - trebuchetDamage;
        assertEquals(expectedPoints, duel.getRowOf(player2, firstRow).get(0).getPoints());

    }

    @Test
    public void testBoostingCardEveryTwoTurns() {
        duel = createDuel(List.of(goodPerson, viking));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1,goodPerson), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, viking), firstRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1,viking), firstRow, player1);

        int expectedPoints = viking.getPoints() + goodPersonBoost;
        int actualPoints = duel.getRowOf(player1, firstRow).stream().filter(c -> c.equals(viking))
                .findFirst().get().getPoints();
        assertEquals(expectedPoints,actualPoints);


    }

    @Test
    public void testBoostingByNumberOfCardsOnGraveyard() {
        duel = createDuel(List.of(gravedigger, archer, minion));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, minion), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow,
                findByName(board(duel, player1),minion), player2);
        playCardWithoutTargeting(duel, findByName(hand1, gravedigger), firstRow, player1);

        int cardsOnGraveyard = 1;
        int expectedPoints = gravedigger.getPoints() + cardsOnGraveyard;
        int actualPoints = duel.getRowOf(player1, firstRow)
                .stream()
                .filter(c-> c.equals(gravedigger) )
                .findFirst().get().getPoints();

        assertEquals(expectedPoints, actualPoints);

    }

    @Test
    public void testCardResurrectionFromGraveyard() {
        duel = createDuel(List.of(witch, archer, minion));
        setHands();
        playCardWithoutTargeting(duel, findByName(hand1, minion), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow,
                findByName(board(duel,player1), minion), player2);

        CardDisplay playChainCard = playCardWithCardTargeting(
                duel, findByName(hand1, witch), firstRow, duel.getGraveyardOf(player1).get(0), player1
        );
        assertEquals(minion, playChainCard);
        playCardWithoutTargeting(duel, playChainCard, firstRow, player1);

        CardDisplay playedChainCard = duel.getRowOf(player1, firstRow)
                .stream().filter(c->c.equals(playChainCard)).findFirst().orElse(new CardDisplay());

        assertEquals(playedChainCard, playChainCard);
    }
    @Test
    public void testCallingAllCardCopiesFromDeck() {
        duel = createDuel(List.of(wildRoam, wildRoam, viking, knight, capitan, archer,
                armageddon, wildRoam, wildRoam));
        setHands();

        int onePlayedFromHand = 1;
        int cardOccurences = onePlayedFromHand + duel.getDeckOf(player1)
                .stream()
                .filter(c -> c.equals(wildRoam))
                .collect(Collectors.toList()).size();
        playCardWithoutTargeting(duel, findByName(hand1, wildRoam), firstRow, player1);
        assertEquals(cardOccurences, duel.getRowOf(player1, firstRow).size());


        int cardsOnHandLeft = duel.getHandOf(player1).stream()
                .filter(c -> c.equals(wildRoam))
                .collect(Collectors.toList()).size();
        assertEquals(1, cardsOnHandLeft);

    }

    @Test
    public void testPlayingCopyOfCardOnBoardFromDeck() {
        duel = createDuel(List.of(supplier, sharpshooter, viking, capitan, warrior,
                minion, paper, badDog, sharpshooter));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1,viking), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, sharpshooter), firstRow,
                findByName(board(duel,player1), viking), player2);
        playCardWithCardTargeting(duel, findByName(hand1, sharpshooter), firstRow,
                findByName(board(duel, player2), sharpshooter), player1);

        CardDisplay loweredSharpShooter = duel.getRowOf(player2, firstRow).get(0);
        CardDisplay sharpshooterChain = playCardWithCardTargeting(duel, findByName(hand2, supplier), secondRow,loweredSharpShooter, player2);

        playCardWithCardTargeting(duel, sharpshooterChain, thirdRow, findByName(board(duel,player1), viking), player2);

        assertEquals(sharpshooter, duel.getRowOf(player2, thirdRow).get(0));
        assertEquals(sharpshooter.getPoints(), duel.getRowOf(player2, thirdRow).get(0).getPoints());

        int actualVikingPoints= duel.getRowOf(player1, firstRow).get(0).getPoints();
        int expectedVikingPoints = viking.getPoints() - 2* sharpshooterDamage;
        assertEquals(expectedVikingPoints, actualVikingPoints);
    }

    @Test
    public void testCallingCardFromDeckAfterTurns() {
        duel = createDuel(List.of(breaker, viking, minion, paper, warrior, armageddon, capitan, breaker));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, breaker), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, capitan), secondRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1, minion), secondRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, viking), secondRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1, paper), secondRow, player1);

        int expectedUnitsOnFirstRow = 2;
        assertEquals(expectedUnitsOnFirstRow, duel.getRowOf(player1, firstRow).size());

    }

    @Test
    public void testKillingAllMinPointsCards() {
        duel = createDuel(List.of(epidemic, minion, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1,minion), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2,minion), firstRow, player2);
        playCardWithoutTargeting(duel, findByName(hand1,viking), firstRow, player1);
        playSpecialCardWithoutTargeting(duel, findByName(hand2,epidemic), player2);

        int expectedCardsLeft = 1;
        int actualCardsLeft = duel.getRowOf(player1, firstRow).size() ;
        assertEquals(expectedCardsLeft, actualCardsLeft );
    }
    @Test
    public void testSpyThatDrawsCard() {
        duel = createDuel(List.of(spy, warrior, minion, paper, viking, capitan, wildRoam, wildRoam,archer, priest, witch));
        setHands();

        int initialHandSize = duel.getHandOf(player1).size();
        playCardWithoutTargeting(duel, findByName(hand1, spy), firstRow, player1);
        int handSizeAfterPlayingSpy= duel.getHandOf(player1).size();

        assertEquals(spy.getPoints(), duel.getRowPointsOf(player2, firstRow));
        assertEquals(0, duel.getRowPointsOf(player1, firstRow));
        assertEquals(initialHandSize, handSizeAfterPlayingSpy);

    }

    @Test
    public void testBurningOppositeRow() {
        duel = createDuel(List.of(blueFire, warrior, capitan, armageddon));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, warrior), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, blueFire), firstRow, player2);

        assertEquals(1,duel.getRowOf(player1, firstRow).size() );

        playCardWithoutTargeting(duel, findByName(hand1, capitan), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, armageddon), firstRow, player2);

        playCardWithoutTargeting(duel, findByName(hand1, armageddon), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, capitan), firstRow, player2);

        playCardWithoutTargeting(duel, findByName(hand1, blueFire), firstRow, player1);
        assertEquals(2, duel.getRowOf(player2, firstRow).size());


    }

    @Test
    public void testDamageDealtByNumberOfWeakenedCards() {
        duel = createDuel(List.of(axer, archer, viking));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, viking), firstRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, archer), firstRow, findByName(board(duel,player1), viking), player2);
        playCardWithoutTargeting(duel, findByName(hand1, archer), secondRow, player1);
        playCardWithCardTargeting(duel, findByName(hand2, axer), firstRow, findByName(board(duel,player1), viking), player2);

        int numberOfWeakenedCards = 1;
        int expectedPoints = viking.getPoints() - archerDamage - numberOfWeakenedCards;
        assertEquals(expectedPoints, duel.getRowPointsOf(player1, firstRow));
    }

    @Test
    public void testMakingCopyOfCardInDeck() {
        duel = createDuel(List.of(wildRoam, wildRoam, copier));
        setHands();

        playCardWithoutTargeting(duel, hand1.get(0), firstRow, player1);
        duel.endRoundFor(player2);
        playCardWithCardTargeting(duel, findByName(hand1, copier), secondRow, hand1.get(0), player1);

        //Are copies created in deck
        int expectedWildRoamsNumber = copierCopiesCount;
        int actualWildRoamsInDeck = duel.getDeckOf(player1).stream()
                .filter(c -> c.getName().equals(wildRoam.getName()))
                .collect(Collectors.toList()).size();
        assertEquals(expectedWildRoamsNumber, actualWildRoamsInDeck);


        //Will wild roam call this copies from deck
        playCardWithoutTargeting(duel, hand1.get(1), thirdRow, player1);
        assertEquals(3, duel.getRowOf(player1, thirdRow).size());


    }

    @Test
    public void testDecreasingBasePower() {
        duel = createDuel(List.of(mushrooms, armageddon, minion));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, armageddon), firstRow, player1);
        playSpecialCardWithCardTargeting(duel, findByName(hand2, mushrooms), findByName(board(duel,player1), armageddon), player2);

        CardDisplay damagedCard = duel.getRowOf(player1, firstRow).get(0);
        assertEquals(damagedCard.getBasePoints(), damagedCard.getPoints());

        int expectedBasePower = armageddon.getBasePoints() - mushroomsBaseDamage;
        assertEquals(expectedBasePower, damagedCard.getBasePoints());
    }

    @Test
    public void afterBasePowerEqualsZero_cardIsTotallyDestroyed() {
        duel = createDuel(List.of(mushrooms, minion));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, minion), firstRow, player1);
        playSpecialCardWithCardTargeting(duel, findByName(hand2, mushrooms), findByName(board(duel,player1), minion), player2);

        assertEquals(0, duel.getRowOf(player1, firstRow).size());
        assertEquals(0, duel.getGraveyardOf(player1).size());
    }

    @Test
    public void testIncreasingBasePower() {
        duel = createDuel(List.of(tastyMushroom, paper, conflagration));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, paper), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, paper), firstRow, player2);
        playSpecialCardWithCardTargeting(duel, findByName(hand1, tastyMushroom), findByName(board(duel, player1), paper), player1);

        int expectedBasePower = paper.getBasePoints() + tastyMushroomBaseIncrease;
        assertEquals(expectedBasePower , duel.getRowPointsOf(player1, firstRow));


        playSpecialCardWithoutTargeting(duel, findByName(hand2, conflagration), player2);
        assertEquals(0, duel.getRowOf(player1, firstRow).size());
        assertEquals(expectedBasePower, duel.getGraveyardOf(player1).get(0).getBasePoints());
    }

    @Test
    public void testTargetingGoldCard() {
        duel = createDuel(List.of(giant, archer, sharpshooter));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1,giant ), firstRow, player1);

        assertTrue(duel.getPossibleTargetsOf(archer, player2).isEmpty());
        assertFalse(duel.getPossibleTargetsOf(sharpshooter, player2).isEmpty());

    }

    @Test
    public void testLockingCard() {
        duel = createDuel(List.of(handcuffs, key, goodPerson, booster, capitan, warrior));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, goodPerson), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, goodPerson), firstRow, player2);
        playCardWithCardTargeting(duel,findByName(hand1, booster), firstRow, findByName(board(duel,player1),goodPerson), player1);
        playSpecialCardWithCardTargeting(duel,findByName(hand2, handcuffs), findByName(board(duel,player1), goodPerson), player2);
        playSpecialCardWithCardTargeting(duel,findByName(hand1, handcuffs), findByName(board(duel,player2), goodPerson), player1);
        duel.endRoundFor(player2);

        int pointsBeforeGoodPersonBoost = duel.getRowPointsOf(player1,firstRow) + capitan.getPoints();
        playCardWithoutTargeting(duel,findByName(hand1, capitan ), firstRow, player1);
        int pointsAfterGoodPersonBoost = duel.getRowPointsOf(player1,firstRow) ;

        assertEquals(pointsBeforeGoodPersonBoost , pointsAfterGoodPersonBoost);
    }
    @Test
    public void testUnlockingCard() {
        duel = createDuel(List.of(handcuffs, key, goodPerson, booster, capitan, warrior));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, goodPerson), firstRow, player1);
        playCardWithoutTargeting(duel, findByName(hand2, goodPerson), firstRow, player2);
        playCardWithCardTargeting(duel,findByName(hand1, booster), firstRow, findByName(board(duel,player1), goodPerson), player1);
        playSpecialCardWithCardTargeting(duel,findByName(hand2, handcuffs), findByName(board(duel,player1), goodPerson), player2);
        playSpecialCardWithCardTargeting(duel,findByName(hand1, key), findByName(board(duel,player1), goodPerson), player1);
        duel.endRoundFor(player2);

        int pointsBeforeGoodPersonBoost = duel.getRowPointsOf(player1,firstRow) + capitan.getPoints();
        playCardWithoutTargeting(duel,findByName(hand1, capitan), firstRow, player1);
        int pointsAfterGoodPersonBoost = duel.getRowPointsOf(player1,firstRow) ;

        assertEquals(pointsBeforeGoodPersonBoost +1, pointsAfterGoodPersonBoost);

    }


    @Test
    public void lockedCards_dontTriggerOnDeathEffects() {
        duel = createDuel(List.of(cow, handcuffs));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, cow), firstRow, player1);
        playSpecialCardWithCardTargeting(duel, findByName(hand2, handcuffs), findByName(board(duel,player1),cow), player2);
        assertTrue(duel.getRowOf(player1,firstRow).isEmpty());
    }

    @Test
    public void testEatingOwnCard() {
        duel = createDuel(List.of(cow, trex));
        setHands();

        playCardWithoutTargeting(duel, findByName(hand1, cow), firstRow, player1);
        duel.endRoundFor(player2);
        playCardWithCardTargeting(duel, findByName(hand1, trex), secondRow, findByName(board(duel,player1), cow), player1);

        assertEquals(chort.getPoints(), duel.getRowPointsOf(player1, firstRow));
        assertEquals(cow.getPoints() + trex.getPoints(), duel.getRowPointsOf(player1, secondRow));

    }



}