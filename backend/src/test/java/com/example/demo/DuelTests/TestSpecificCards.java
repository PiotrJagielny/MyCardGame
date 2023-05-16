package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
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
        List<CardDisplay> deck = List.of(CardsFactory.leader, CardsFactory.viking);


        duel  = getCreatedDuel(deck);

        playCard(deck.get(1), firstRow, firstPlayer); 
        playCard(deck.get(1), firstRow, secondPlayer);
        playCard(deck.get(0), firstRow, firstPlayer);
        int leaderRowBoostAmount = 2;
        assertEquals(5 + leaderRowBoostAmount, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void testBoostingSingleCard(){
        List<CardDisplay> deck = List.of(CardsFactory.booster, CardsFactory.viking);
        duel  = getCreatedDuel(deck);

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
        duel  = getCreatedDuel(deck);

        playCard(CardsFactory.paper, firstRow, firstPlayer);
        playCard(CardsFactory.paper, firstRow, secondPlayer);
        playCard(CardsFactory.minion, secondRow, firstPlayer);
        playCard(CardsFactory.minion, secondRow, secondPlayer);
        playCard(CardsFactory.viking, thirdRow, firstPlayer);
        playCard(CardsFactory.viking, thirdRow, secondPlayer);

        playCard(CardsFactory.woodTheHealer, thirdRow, firstPlayer);

        int woodTheHealerBoostAmount = 2;
        //CardsFactory.woodTheHealer heals only cards with points less than 3
        int expectedminionPoints = CardsFactory.minion.getPoints() + woodTheHealerBoostAmount;
        int expectedpaperPoints = CardsFactory.paper.getPoints() + woodTheHealerBoostAmount;
        int expectedvikingPoints = CardsFactory.viking.getPoints();
        int expectedBoardPoints = expectedminionPoints + expectedpaperPoints + expectedvikingPoints + CardsFactory.woodTheHealer.getPoints();

        assertEquals(expectedBoardPoints, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void testStrikingEnemyCard(){
        List<CardDisplay> deck = List.of(CardsFactory.fireball, CardsFactory.viking);
        duel  = getCreatedDuel(deck);

        playCard(CardsFactory.viking, firstRow, firstPlayer);
        playCard(CardsFactory.fireball, firstRow, CardsFactory.viking, firstRow, secondPlayer);

        CardDisplay strikedvikingDisplay = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).get(0);

        int fireballStrikeAmount = 3;
        assertEquals(CardsFactory.viking.getPoints() - fireballStrikeAmount,  strikedvikingDisplay.getPoints());
        assertTrue(duel.getCardsInHandDisplayOf(firstPlayer).contains(CardsFactory.fireball));
        assertEquals(1, duel.getBoardPointsOf(secondPlayer));
    }

}