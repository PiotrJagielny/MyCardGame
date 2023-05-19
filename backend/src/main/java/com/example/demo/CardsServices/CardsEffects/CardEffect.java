package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.Duel.DataStructures.PlayerPlay;
import com.example.demo.Duel.Services.OnePlayerDuel;

public interface CardEffect {
    void invokeEffect(OnePlayerDuel player, OnePlayerDuel enemy, PlayerPlay playMade);
}
