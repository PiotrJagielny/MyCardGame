package com.example.demo.DuelTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.Services.CardDuel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Consts.*;
import static org.junit.jupiter.api.Assertions.*;

class TestSpecificCards {

    @Test
    public void testLeaderCard(){
        List<CardDisplay> cardsDisplays = new ArrayList<>();
        cardsDisplays.add(new CardDisplay("Leader", 1));
        cardsDisplays.add(new CardDisplay("Viking", 4));

        CardDuel duel  = CardDuel.createDuel();
        duel.registerPlayerToDuel(secondPlayer);
        duel.registerPlayerToDuel(firstPlayer);
        duel.parseCardsFor(cardsDisplays , secondPlayer);
        duel.parseCardsFor(cardsDisplays , firstPlayer);
        duel.dealCards();

        duel.playCardAs(cardsDisplays.get(1), firstPlayer, firstRow);
        duel.playCardAs(cardsDisplays.get(1), secondPlayer, firstRow);
        duel.playCardAs(cardsDisplays.get(0), firstPlayer, firstRow);
        int leaderRowBoostAmount = 2;
        assertEquals(5 + leaderRowBoostAmount, duel.getBoardPointsOf(firstPlayer));
    }

    @Test
    public void testBooserCard(){
        List<CardDisplay> cardsDisplays = new ArrayList<>();
        cardsDisplays.add(new CardDisplay("Booster", 1));
        cardsDisplays.add(new CardDisplay("Viking", 4));

        CardDuel duel  = CardDuel.createDuel();
        duel.registerPlayerToDuel(secondPlayer);
        duel.registerPlayerToDuel(firstPlayer);
        duel.parseCardsFor(cardsDisplays , secondPlayer);
        duel.parseCardsFor(cardsDisplays , firstPlayer);
        duel.dealCards();

        duel.playCardAs(cardsDisplays.get(1), firstPlayer, firstRow);
        duel.playCardAs(cardsDisplays.get(1), secondPlayer, firstRow);
        duel.playCardAs(cardsDisplays.get(0), firstPlayer, firstRow, cardsDisplays.get(1));
        int singleCardBoostAmount = 3;
        CardDisplay boostedCard = duel.getCardsOnBoardDisplayOf(firstPlayer, firstRow).stream().filter(c -> c.getName().equals("Viking")).findFirst().orElse(null);
        assertEquals(boostedCard.getPoints(), 4 + singleCardBoostAmount);
    }

}