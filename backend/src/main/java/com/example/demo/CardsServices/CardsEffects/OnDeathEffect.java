package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.CardTargetStrattegies.AllCardsInDeckTargetable;
import com.example.demo.Duel.OnePlayerDuel;
import com.example.demo.Duel.PlayerPlay;

public class OnDeathEffect {
    public static void invokeOnDeathEffect(OnePlayerDuel player, OnePlayerDuel enemy, PlayerPlay playMade) {
        CardDisplay p = playMade.getPlayedCard();

    }
}
