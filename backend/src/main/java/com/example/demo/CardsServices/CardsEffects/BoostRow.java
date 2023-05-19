package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.DataStructures.PlayerPlay;
import com.example.demo.Duel.Services.OnePlayerDuel;

public class BoostRow implements CardEffect{

    private OnePlayerDuel affectedPlayer;
    private PlayerPlay playMade;
    private int boostAmount;

    public BoostRow(OnePlayerDuel affectedPlayer, PlayerPlay playMade, int boostAmount) {
        this.affectedPlayer = affectedPlayer;
        this.playMade = playMade;
        this.boostAmount = boostAmount;
    }

    @Override
    public void invokeEffect() {
        for(CardDisplay cardOnRow: affectedPlayer.getCardsOnBoardOnRow(playMade.getAffectedCardRowNum())){
            affectedPlayer.boostCard(cardOnRow, 2);
        }
        affectedPlayer.placeCardOnBoard(playMade);
    }
}
