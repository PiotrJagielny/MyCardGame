package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Consts;
import com.example.demo.Duel.PlayerPlay;
import com.example.demo.Duel.OnePlayerDuel;

import java.util.List;

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
        for(CardDisplay cardOnRow: player.getCardsOnBoardOnRow(playMade.getPlayedCardRowNum())){
            player.boostCard(cardOnRow, amount);
        }
    }

    public void healerBoost(int boostAmount){
        for (int i = 0; i < Consts.rowsNumber; i++) {
            for(CardDisplay cardOnRow : player.getCardsOnBoardOnRow(i)){
                if(cardOnRow.getPoints() <= CardsFactory.healerMaxCardPointsWithBoost){
                    player.boostCard(cardOnRow, boostAmount);
                }
            }
        }

    }

    public void burnAllMaxPointsCards() {
        int playerMaxPoints = findMaxPoints(player.getWholeBoard());
        int enemyMaxPoints = findMaxPoints(enemy.getWholeBoard());
        int maxPoints = Math.max(playerMaxPoints, enemyMaxPoints);
        burnCardsWithMaxPoints(player, maxPoints);
        burnCardsWithMaxPoints(enemy, maxPoints);
    }

    private int findMaxPoints(List<List<CardDisplay>> ofBoard){
        int maxPoints = 0;
        for (int i = 0; i < Consts.rowsNumber; i++) {
            for (int j = 0; j < ofBoard.get(i).size(); j++) {
                if(ofBoard.get(i).get(j).getPoints() > maxPoints)
                    maxPoints = ofBoard.get(i).get(j).getPoints();
            }
        }
        return maxPoints;
    }

    private void burnCardsWithMaxPoints(OnePlayerDuel player, int maxPoints) {
        List<List<CardDisplay>> wholeBoard = player.getWholeBoard();
        for (int i = 0; i < Consts.rowsNumber; i++) {
            for (int j = 0; j < wholeBoard.get(i).size(); j++) {
                if(wholeBoard.get(i).get(j).getPoints() == maxPoints)
                    player.burnCard(wholeBoard.get(i).get(j), i);
            }
        }
    }

    private void ripWholeRow() {
        List<CardDisplay> row = enemy.getCardsOnBoardOnRow(playMade.getAffectedRow());
        for (int i = 0; i < row.size(); i++) {
            enemy.strikeCard(row.get(i), CardsFactory.ripRowDamageAmount);
        }
    }

    public void invokeEffect(){
        CardDisplay p = playMade.getPlayedCard();
        if(p.equals(CardsFactory.booster)){
            player.boostCard(playMade.getAffectedCard(), CardsFactory.boosterBoostAmount);
        }
        else if(p.equals(CardsFactory.archer)){
            enemy.strikeCard(playMade.getAffectedCard(), CardsFactory.archerStrikeAmount);
        }
        else if(p.equals(CardsFactory.leader)){
            boostRowBy(CardsFactory.leaderBoostAmount);
        }
        else if(p.equals(CardsFactory.healer)){
            healerBoost(CardsFactory.healerBoostAmount);
        }
        else if(p.equals(CardsFactory.fireball)) {
            enemy.strikeCard(playMade.getAffectedCard(), CardsFactory.fireballStrikeAmount);
        }
        else if(p.equals(CardsFactory.conflagration)){
            burnAllMaxPointsCards();
        }
        else if(p.equals(CardsFactory.doubler)){
            int boostAmount = playMade.getAffectedCard().getPoints();
            player.boostCard(playMade.getAffectedCard(), boostAmount);
        }
        else if(p.equals(CardsFactory.rip)) {
            ripWholeRow();
        }


        player.placeCardOnBoard(playMade);
    }


}
