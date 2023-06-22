package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Consts;
import com.example.demo.Duel.OnePlayerDuel;
import com.example.demo.Duel.PlayerPlay;

import java.util.List;

public class OnTurnEndEffect {
    private OnePlayerDuel player;
    public OnTurnEndEffect(OnePlayerDuel player) {
        this.player = player;
    }
    public void invokeTurnEndEffect() {

        List<List<CardDisplay>> wholeBoard = player.getWholeBoard();
        for (int i = 0; i < Consts.rowsNumber; i++) {
            List<CardDisplay> row = wholeBoard.get(i);
            for (var card : row) {
                if(card.equals(CardsFactory.longer)) {
                    player.boostCard(card, CardsFactory.longerBoostAmount);
                }
            }
        }
    }
}
