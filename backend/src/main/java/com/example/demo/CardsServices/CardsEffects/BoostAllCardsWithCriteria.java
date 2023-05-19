package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Consts;
import com.example.demo.Duel.DataStructures.PlayerPlay;
import com.example.demo.Duel.Services.OnePlayerDuel;

public class BoostAllCardsWithCriteria implements CardEffect{

    private OnePlayerDuel playerAffected;

    private PlayerPlay playMade;
    private int boostAmount;


    public BoostAllCardsWithCriteria(OnePlayerDuel playerAffected, PlayerPlay playMade, int boostAmount) {
        this.playerAffected = playerAffected;
        this.playMade = playMade;
        this.boostAmount = boostAmount;
    }

    @Override
    public void invokeEffect() {
        for (int i = 0; i < Consts.rowsNumber; i++) {
            for(CardDisplay cardOnRow : playerAffected.getCardsOnBoardOnRow(i)){
                if(cardOnRow.getPoints() < 3){
                    playerAffected.boostCard(cardOnRow, 2);
                }
            }
        }
        playerAffected.placeCardOnBoard(playMade);
    }
}
