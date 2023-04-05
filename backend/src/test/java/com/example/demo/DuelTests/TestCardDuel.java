package com.example.demo.DuelTests;

import com.example.demo.Duel.Services.CardDuel;
import com.example.demo.Duel.Services.NormalDuel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestCardDuel {

    CardDuel duel;
    @BeforeEach
    public void setUp(){
        duel = new NormalDuel();
    }

    @Test
    public void duelCreation_noWinner() {
        assertFalse(duel.whoWon());
    }

    @Test
    public void emptyDisplaysAfterDuelCreation() {
        assertTrue(duel.getPlayerCardsDisplay().isEmpty());
    }

    @Test
    public void testCardsParse() {
        List<String> cardsDisplay = List.of("Knight", "Viking");
        duel.parseCards(cardsDisplay);
        assertEquals(cardsDisplay.size() , duel.getPlayerCardsDisplay().size());
        for(int i = 0 ; i < cardsDisplay.size() ; ++i){
            assertEquals(cardsDisplay.get(i), duel.getPlayerCardsDisplay().get(i));
        }
    }

}