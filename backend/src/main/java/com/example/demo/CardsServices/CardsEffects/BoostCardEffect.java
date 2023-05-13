package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.Duel.Services.OnePlayerDuel;

public class BoostCardEffect implements CardEffect{

    private OnePlayerDuel playerAffected;

    private int boostAmount;

    public BoostCardEffect(OnePlayerDuel playerAffected, int boostAmount) {
        this.playerAffected = playerAffected;
        this.boostAmount = boostAmount;
    }

    @Override
    public void invokeEffect() {

    }
}
