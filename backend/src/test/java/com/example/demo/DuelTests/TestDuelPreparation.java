package com.example.demo.DuelTests;

import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.PlayerNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestDuelPreparation {

    CardDuel duel;

    @BeforeEach
    public void setUp(){
        duel = CardDuel.createDuel();
    }

    @Test
    public void afterCreatingDuel_noWinner() {
        assertFalse(duel.whoWon());
    }

    @Test
    public void afterCreatingDuel_twoPlayersDecksAreEmpty() {
        assertTrue(duel.getCardsInDeckDisplayOf(PlayerNumber.FirstPlayer).isEmpty());
    }


}