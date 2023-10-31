package com.example.demo;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Duel.ClientAPI.CardDuel;

import java.util.List;
import java.util.Random;

public final class Utils {

    private Utils() {}

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min +1) + min;
    }

    public CardDisplay getMirrorCardFromHand(CardDuel duel, String username, CardDisplay cardToSearch) {
        String enemy = duel.getOpponentOf(username);
        List<CardDisplay> enemyHand = duel.getHandOf(enemy);
        return enemyHand.stream().filter(c -> c.getName().equals(cardToSearch.getName())).findFirst().orElse(new CardDisplay());
    }
    

}
