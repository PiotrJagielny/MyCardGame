package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Consts;
import com.example.demo.Duel.PlayerPlay;
import com.example.demo.Duel.OnePlayerDuel;

import java.util.List;

public class CardEffects {
    private OnePlayerDuel player;
    private OnePlayerDuel enemy;
    private PlayerPlay playMade;

    public CardEffects(OnePlayerDuel player, OnePlayerDuel enemy, PlayerPlay playMade) {
        this.player = player;
        this.enemy = enemy;
        this.playMade = playMade;
    }
    public CardEffects(OnePlayerDuel player, OnePlayerDuel enemy) {
        this.player = player;
        this.enemy = enemy;
        this.playMade = null;
    }
    public void changePerspective(OnePlayerDuel newPlayer, OnePlayerDuel newEnemy) {
        player = newPlayer;
        enemy = newEnemy;
    }
    private void strikeCardBy(CardDisplay targetedCard, int strikeAmoutn) {
        if(targetedCard.getPoints() <= strikeAmoutn) {
            if(targetedCard.equals(CardsFactory.cow)) {
                enemy.spawnCard(CardsFactory.chort, enemy.getCardRow(targetedCard));
            }
        }
        enemy.strikeCard(targetedCard, strikeAmoutn);
    }

    public void invokeOnPlaceEffect() {
        CardDisplay p = playMade.getPlayedCard();
        if(p.equals(CardsFactory.booster)){
            player.boostCard(playMade.getTargetedCard(), CardsFactory.boosterBoostAmount);
        }
        else if(p.equals(CardsFactory.archer)){
            strikeCardBy(playMade.getTargetedCard(), CardsFactory.archerStrikeAmount);
        }
        else if(p.equals(CardsFactory.leader)){
            boostRowBy(CardsFactory.leaderBoostAmount);
        }
        else if(p.equals(CardsFactory.healer)){
            healerBoost(CardsFactory.healerBoostAmount);
        }
        else if(p.equals(CardsFactory.fireball)) {
            enemy.strikeCard(playMade.getTargetedCard(), CardsFactory.fireballStrikeAmount);
        }
        else if(p.equals(CardsFactory.conflagration)){
            burnAllMaxPointsCards();
        }
        else if(p.equals(CardsFactory.doubler)){
            int boostAmount = playMade.getTargetedCard().getPoints();
            player.boostCard(playMade.getTargetedCard(), boostAmount);
        }
        else if(p.equals(CardsFactory.rip)) {
            ripWholeRow();
        }
        else if(p.equals(CardsFactory.rain)) {
            enemy.setRowStatus(RowStatus.Rain, playMade.getAffectedRow());
            player.setRowStatus(RowStatus.Rain, playMade.getAffectedRow());
        }
        else if(p.equals(CardsFactory.clearSky)) {
            player.clearRowsStatus();
            enemy.clearRowsStatus();
        }

        player.placeCardOnBoard(playMade);

        if(p.equals(CardsFactory.sharpshooter)) {
            int targetPoints = playMade.getTargetedCard().getPoints();
            if(targetPoints <= CardsFactory.sharpshooterDamage) {
                player.boostCard(playMade.getPlayedCard(),CardsFactory.sharpshooterSelfBoost);
            }
            enemy.strikeCard(playMade.getTargetedCard(), CardsFactory.sharpshooterDamage);
        }
    }

    public void boostRowBy(int amount){
        for(CardDisplay cardOnRow: player.getCardsOnBoardOnRow(playMade.getPlayedCardRowNum())){
            player.boostCard(cardOnRow, amount);
        }
    }

    public void healerBoost(int boostAmount){
        for(CardDisplay card: player.getCardsOnBoard()){
            if(card.getPoints() <= CardsFactory.healerMaxCardPointsWithBoost){
                player.boostCard(card, boostAmount);
            }
        }
    }

    public void burnAllMaxPointsCards() {
        int playerMaxPoints = findMaxPoints(player.getCardsOnBoard());
        int enemyMaxPoints = findMaxPoints(enemy.getCardsOnBoard());
        int maxPoints = Math.max(playerMaxPoints, enemyMaxPoints);
        burnCardsWithMaxPoints(player, maxPoints);
        burnCardsWithMaxPoints(enemy, maxPoints);
    }

    private int findMaxPoints(List<CardDisplay> ofBoard){
        int maxPoints = 0;
        for (int j = 0; j < ofBoard.size(); j++) {
            if(ofBoard.get(j).getPoints() > maxPoints)
                maxPoints = ofBoard.get(j).getPoints();
        }
        return maxPoints;
    }

    private void burnCardsWithMaxPoints(OnePlayerDuel player, int maxPoints) {
        List<CardDisplay> cardsOnBoard = player.getCardsOnBoard();
        for (int j = 0; j < cardsOnBoard.size(); j++) {
            if(cardsOnBoard.get(j).getPoints() == maxPoints)
                player.burnCard(cardsOnBoard.get(j));
        }
    }

    private void ripWholeRow() {
        List<CardDisplay> row = enemy.getCardsOnBoardOnRow(playMade.getAffectedRow());
        for (int i = 0; i < row.size(); i++) {
            enemy.strikeCard(row.get(i), CardsFactory.ripRowDamageAmount);
        }
    }

    public void invokeOnTurnEndEffect() {
        List<CardDisplay> cardsOnBoard = player.getCardsOnBoard();
        for (var card : cardsOnBoard) {
            invokeSpecificCardTurnEffect(card);
        }
    }
    private void invokeSpecificCardTurnEffect(CardDisplay card) {
        if(card.equals(CardsFactory.longer)) {
            player.boostCard(card, CardsFactory.longerBoostAmount);
        }
    }

    public void invokeOnTurnStartEffect() {
        for (int i = 0; i < Consts.rowsNumber; i++) {
            String status = player.getRowStatusName(i);
            if(status.equals(RowStatus.Rain.toString())) {
                int maxPoints = 0;
                CardDisplay maxPointsCard = new CardDisplay();
                List<CardDisplay> cards = player.getCardsOnBoard();
                for (CardDisplay card : cards) {
                    if(card.getPoints() > maxPoints) {
                        maxPoints = card.getPoints();
                        maxPointsCard = card;
                    }
                }
                player.strikeCard(maxPointsCard, CardsFactory.rainStrikeAmount);
            }
        }
    }

}
