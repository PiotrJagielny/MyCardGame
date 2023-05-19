package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.Duel.DataStructures.PlayerPlay;
import com.example.demo.Duel.Services.OnePlayerDuel;

public class BoostCard implements CardEffect{

    private OnePlayerDuel affectedPlater;
    private PlayerPlay playMade;
    int boostAmount;

    public BoostCard(OnePlayerDuel affectedPlater, PlayerPlay playMade, int boostAmount) {
        this.affectedPlater = affectedPlater;
        this.playMade = playMade;
        this.boostAmount = boostAmount;
    }

    @Override
    public void invokeEffect() {
        affectedPlater.boostCard(playMade.getAffectedCard(), 3);
        affectedPlater.placeCardOnBoard(playMade);
    }
}
