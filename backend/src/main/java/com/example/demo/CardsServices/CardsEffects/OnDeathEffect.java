package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Duel.OnePlayerDuel;
import com.example.demo.Duel.PlayerPlay;

public class OnDeathEffect {
    private OnePlayerDuel player;
    private OnePlayerDuel enemy;

    public OnDeathEffect(OnePlayerDuel player, OnePlayerDuel enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    public void invokeOnDeathEffect(CardDisplay cardDied, int onRow) {
        if(cardDied.equals(CardsFactory.cow)) {
            player.spawnCard(CardsFactory.chort, onRow);
        }

    }
    public void strikeCardBy(CardDisplay cardToStrike, int strikeAmount) {
        if(cardToStrike.getPoints() <= strikeAmount) {
            int onPlayerRow = player.getCardRow(cardToStrike);
            int onEnemyRow = enemy.getCardRow(cardToStrike);
            if(onEnemyRow != -1) {

            }
            else {

            }
            invokeOnDeathEffect(cardToStrike, player.getCardRow(cardToStrike));
        }

    }
}
