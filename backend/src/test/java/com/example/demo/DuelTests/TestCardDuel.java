package com.example.demo.DuelTests;

import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.NormalDuel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class TestCardDuel {

    @Test
    public void Nothing(){
        CardDuel duel = new NormalDuel();
        assertFalse(duel.whoWon());
    }

}