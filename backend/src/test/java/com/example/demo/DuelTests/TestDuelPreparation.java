package com.example.demo.DuelTests;

import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.NormalDuel;
import com.example.demo.Duel.Services.PlayerNumber;
import com.example.demo.TestsData.TestConsts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Not;

import static com.example.demo.TestsData.TestConsts.*;
import static org.junit.jupiter.api.Assertions.*;

class TestDuelPreparation {
    CardDuel duel;
    
    @BeforeEach
    public void setUp(){

        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(firstPlayer);
        duel.registerPlayerToDuel(secondPlayer);
    }

    @Test
    public void afterCreatingDuel_noWinner() {
        assertFalse(duel.whoWon());
    }

    @Test
    public void afterCreatingDuel_twoPlayersDecksAreEmpty() {
        assertTrue(duel.getCardsInDeckDisplayOf(firstPlayer).isEmpty());
    }


}