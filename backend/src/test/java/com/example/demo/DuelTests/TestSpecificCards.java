package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Duel.DataStructures.PlayerPlay;
import com.example.demo.Duel.Services.CardDuel;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.demo.Consts.*;
import static org.junit.jupiter.api.Assertions.*;

class TestSpecificCards {


    private CardDuel duel;


    public void playCard(CardDisplay playedCard, int onRow, CardDisplay cardThatGotEffect, int affectedCardRow, String player){
        duel.playCardAs(new PlayerPlay(playedCard, onRow,cardThatGotEffect, affectedCardRow), player);
    }

    public void playCard(CardDisplay playedCard, int onRow, String player){
        duel.playCardAs(new PlayerPlay(playedCard, onRow), player);
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
        playCard(deck.get(0), secondRow,deck.get(1), firstRow, firstPlayer);
        int singleCardBoostAmount = 3;
        CardDisplay boostedCard = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).stream().filter(c -> c.getName().equals("Viking")).findFirst().orElse(null);
        assertEquals(CardsFactory.viking.getPoints() + singleCardBoostAmount, boostedCard.getPoints());
    }

    @Test
    public void testBoostingWholeBoard(){
        List<CardDisplay> deck = List.of(CardsFactory.woodTheHealer, CardsFactory.paper, CardsFactory.minion, CardsFactory.viking);
        duel  = createDuel(deck);

        playCard(CardsFactory.paper, firstRow, firstPlayer);
        playCard(CardsFactory.paper, firstRow, secondPlayer);
        playCard(CardsFactory.minion, secondRow, firstPlayer);
        playCard(CardsFactory.minion, secondRow, secondPlayer);
        playCard(CardsFactory.viking, thirdRow, firstPlayer);
        playCard(CardsFactory.viking, thirdRow, secondPlayer);

        playCard(CardsFactory.woodTheHealer, thirdRow, firstPlayer);

        int woodTheHealerBoostAmount = 2;
        int expectedMinionPoints = CardsFactory.minion.getPoints() + woodTheHealerBoostAmount;
        int expectedPaperPoints = CardsFactory.paper.getPoints() + woodTheHealerBoostAmount;
        int expectedVikingPoints = CardsFactory.viking.getPoints();
        int expectedBoardPoints = expectedMinionPoints + expectedPaperPoints + expectedVikingPoints + CardsFactory.woodTheHealer.getPoints();

        assertEquals(expectedBoardPoints, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void testStrikingEnemyCard(){
        List<CardDisplay> deck = List.of(CardsFactory.fireball, CardsFactory.viking);
        duel  = createDuel(deck);

        playCard(CardsFactory.viking, firstRow, firstPlayer);
        playCard(CardsFactory.fireball, firstRow, CardsFactory.viking, firstRow, secondPlayer);

        CardDisplay strikedVikingDisplay = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0);

        int fireballStrikeAmount = 3;
        assertEquals(CardsFactory.viking.getPoints() - fireballStrikeAmount,  strikedVikingDisplay.getPoints());
        assertTrue(duel.getCardsInHandDisplayOf(firstPlayer).contains(CardsFactory.fireball));
        assertEquals(1, duel.getBoardPointsOf(secondPlayer));
    }

    @Test
    public void testBoosterPossibleBoostTargets(){
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

        List<CardDisplay> possibleTargetsOfFireball = duel.getPossibleTargetsOf(CardsFactory.fireball, firstPlayer);
        assertTrue(possibleTargetsOfFireball.contains(CardsFactory.warrior));
        assertTrue(possibleTargetsOfFireball.contains(CardsFactory.minion));
    }

}