package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Consts;
import com.example.demo.Duel.DataStructures.PlayerPlay;
import com.example.demo.Duel.Services.OnePlayerDuel;

public class CardEffect {
    private OnePlayerDuel player;
    private OnePlayerDuel enemy;
    private PlayerPlay playMade;

    public CardEffect(OnePlayerDuel player, OnePlayerDuel enemy, PlayerPlay playMade) {
        this.player = player;
        this.enemy = enemy;
        this.playMade = playMade;
    }

    public void boostRowBy(int amount){
        for(CardDisplay cardOnRow: player.getCardsOnBoardOnRow(playMade.getAffectedCardRowNum())){
            player.boostCard(cardOnRow, amount);
        }

    }

    public void woodTheHealerBoost(int boostAmount){
        for (int i = 0; i < Consts.rowsNumber; i++) {
            for(CardDisplay cardOnRow : player.getCardsOnBoardOnRow(i)){
                if(cardOnRow.getPoints() < 3){
                    player.boostCard(cardOnRow, boostAmount);
                }
            }
        }

    }

    public void invokeEffect(){
        CardDisplay p = playMade.getPlayedCard();
        if(p.equals(CardsFactory.booster)){
            player.boostCard(playMade.getAffectedCard(), 3);
        }
        else if(p.equals(CardsFactory.leader)){
            boostRowBy(2);
        }
        else if(p.equals(CardsFactory.woodTheHealer)){
            woodTheHealerBoost(2);
        }
        else if(p.equals(CardsFactory.fireball)){
            enemy.strikeCard(playMade.getAffectedCard(), 3);
        }

        player.placeCardOnBoard(playMade);
    }
}
