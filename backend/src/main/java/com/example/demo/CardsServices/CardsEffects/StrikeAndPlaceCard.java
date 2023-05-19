package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.Duel.DataStructures.PlayerPlay;
import com.example.demo.Duel.Services.OnePlayerDuel;

public class StrikeAndPlaceCard implements CardEffect{

    private OnePlayerDuel affectedPlayer;
    private OnePlayerDuel playedPlayer;
    private PlayerPlay playMade;
    private int strikeAmount;

    public StrikeAndPlaceCard(OnePlayerDuel affectedPlayer,OnePlayerDuel playedPlayer ,PlayerPlay playMade, int strikeAmount) {
        this.affectedPlayer = affectedPlayer;
        this.playMade = playMade;
        this.strikeAmount = strikeAmount;
        this.playedPlayer = playedPlayer;
    }

    @Override
    public void invokeEffect() {
        playedPlayer.placeCardOnBoard(playMade);
        affectedPlayer.strikeCard(playMade.getAffectedCard(), 3);
    }
}
