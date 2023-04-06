package com.example.demo.DuelTests;

import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.NormalDuel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestDuelPreparation {

    CardDuel duel;

    @BeforeEach
    public void setUp(){
        duel = new NormalDuel();
    }

    @Test
    public void afterCreatingDuel_noWinner() {
        assertFalse(duel.whoWon());
    }

    @Test
    public void afterCreatingDuel_DeckIsEmpty() {
        assertTrue(duel.getPlayerCardsInDeckDisplay().isEmpty());
    }

}