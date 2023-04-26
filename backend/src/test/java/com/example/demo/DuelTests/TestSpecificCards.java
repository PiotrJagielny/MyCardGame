package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.DataStructures.PlayerPlay;
import com.example.demo.Duel.Services.CardDuel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.demo.Consts.*;
import static org.junit.jupiter.api.Assertions.*;

class TestSpecificCards {


    private CardDuel duel;

    private CardDisplay Booster;
    private CardDisplay Leader;
    private CardDisplay Viking;
    private CardDisplay WoodTheHealer;
    private CardDisplay Paper;
    private CardDisplay Minion;
    private CardDisplay Fireball;

    @BeforeEach
    public void setUp(){
        Booster = new CardDisplay("Booster", 1);
        Leader = new CardDisplay("Leader", 1);
        Viking = new CardDisplay("Viking", 4);
        WoodTheHealer = new CardDisplay("WoodTheHealer", 2);
        Paper = new CardDisplay("Paper", 1);
        Minion = new CardDisplay("Minion", 2);
        Fireball = new CardDisplay("Fireball", 0);
    }

    public void playCard(CardDisplay playedCard, int onRow, CardDisplay cardThatGotEffect, int affectedCardRow, String player){
        duel.playCardAs(new PlayerPlay(playedCard, onRow,cardThatGotEffect, affectedCardRow), player);
    }

    public void playCard(CardDisplay playedCard, int onRow, String player){
        duel.playCardAs(new PlayerPlay(playedCard, onRow), player);
    }

    public CardDuel getCreatedDuel(List<CardDisplay> deck){
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
        List<CardDisplay> deck = List.of(Leader, Viking);

        duel  = getCreatedDuel(deck);

        playCard(deck.get(1), firstRow, firstPlayer);
        playCard(deck.get(1), firstRow, secondPlayer);
        playCard(deck.get(0), firstRow, firstPlayer);
        int leaderRowBoostAmount = 2;
        assertEquals(5 + leaderRowBoostAmount, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void testBoostingSingleCard(){
        List<CardDisplay> deck = List.of(Booster, Viking);
        duel  = getCreatedDuel(deck);

        playCard(deck.get(1), firstRow, firstPlayer);
        playCard(deck.get(1), firstRow, secondPlayer);
        playCard(deck.get(0), secondRow,deck.get(1), firstRow, firstPlayer);
        int singleCardBoostAmount = 3;
        CardDisplay boostedCard = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).stream().filter(c -> c.getName().equals("Viking")).findFirst().orElse(null);
        assertEquals(boostedCard.getPoints(), 4 + singleCardBoostAmount);
    }

    @Test
    public void testBoostingWholeBoard(){
        List<CardDisplay> deck = List.of(WoodTheHealer, Paper, Minion, Viking);
        duel  = getCreatedDuel(deck);

        playCard(Paper, firstRow, firstPlayer);
        playCard(Paper, firstRow, secondPlayer);
        playCard(Minion, secondRow, firstPlayer);
        playCard(Minion, secondRow, secondPlayer);
        playCard(Viking, thirdRow, firstPlayer);
        playCard(Viking, thirdRow, secondPlayer);

        playCard(WoodTheHealer, thirdRow, firstPlayer);

        int woodTheHealerBoostAmount = 2;
        //woodTheHealer heals only cards with points less than 3
        int expectedMinionPoints = Minion.getPoints() + woodTheHealerBoostAmount;
        int expectedPaperPoints = Paper.getPoints() + woodTheHealerBoostAmount;
        int expectedVikingPoints = Viking.getPoints();
        int expectedBoardPoints = expectedMinionPoints + expectedPaperPoints + expectedVikingPoints + WoodTheHealer.getPoints();

        assertEquals(expectedBoardPoints, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void testStrikingEnemyCard(){
        List<CardDisplay> deck = List.of(Fireball, Viking);
        duel  = getCreatedDuel(deck);

        playCard(Viking, firstRow, firstPlayer);
        playCard(Fireball, firstRow, Viking, firstRow, secondPlayer);

        CardDisplay strikedVikingDisplay = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0);

        int fireballStrikeAmoint = 3;
        assertEquals(4 - fireballStrikeAmoint,  Viking.getPoints());
    }

}