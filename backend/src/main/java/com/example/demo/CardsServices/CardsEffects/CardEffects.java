package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Consts;
import com.example.demo.Duel.PlayerPlay;
import com.example.demo.Duel.OnePlayerDuel;
import com.example.demo.Duel.RowStatus;

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
            onDeathEffect(targetedCard,enemy.getCardRow(targetedCard) ,enemy);
        }
        enemy.strikeCard(targetedCard, strikeAmoutn);
    }
    private void burnCard(CardDisplay cardToBurn , OnePlayerDuel ofPlayer) {
        onDeathEffect(cardToBurn,ofPlayer.getCardRow(cardToBurn) ,ofPlayer);
        ofPlayer.burnCard(cardToBurn);
    }
    private void onDeathEffect(CardDisplay cardDied, int cardRow, OnePlayerDuel ofPlayer) {
        if(cardDied.equals(CardsFactory.cow)) {
            ofPlayer.spawnCard(CardsFactory.chort, cardRow);
        }
    }

    public void invokeOnPlaceEffect() {
        CardDisplay p = playMade.getPlayedCard();
        player.placeCardOnBoard(playMade);
        if(p.equals(CardsFactory.booster)){
            player.boostCard(playMade.getTargetedCard(), CardsFactory.boosterBoost);
        }
        else if(p.equals(CardsFactory.archer)){
            strikeCardBy(playMade.getTargetedCard(), CardsFactory.archerDamage);
        }
        else if(p.equals(CardsFactory.leader)){
            boostRowBy(CardsFactory.leaderBoost);
        }
        else if(p.equals(CardsFactory.healer)){
            healerBoost(CardsFactory.healerBoost);
        }
        else if(p.equals(CardsFactory.fireball)) {
            strikeCardBy(playMade.getTargetedCard(), CardsFactory.fireballDamage);
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
        else if(p.equals(CardsFactory.sharpshooter)) {
            int targetPoints = playMade.getTargetedCard().getPoints();
            if(targetPoints <= CardsFactory.sharpshooterDamage) {
                player.boostCard(playMade.getPlayedCard(),CardsFactory.sharpshooterSelfBoost);
            }
            strikeCardBy(playMade.getTargetedCard(), CardsFactory.sharpshooterDamage);
        }
        else if(p.equals(CardsFactory.gravedigger)) {
            int cardsOnGraveyard = player.getGraveyard().size();
            player.boostCard(playMade.getPlayedCard(), cardsOnGraveyard);
        }
        else if(p.equals(CardsFactory.wildRoam)) {

            List<CardDisplay> deck = player.getCardsInDeck();
            for (CardDisplay card : deck) {
                if(card.equals(CardsFactory.wildRoam)) {
                    player.placeCardOnBoard(new PlayerPlay(card, playMade.getPlayedCardRowNum()));
                }
            }

        }
        else if(p.equals(CardsFactory.epidemic)) {
            burnAllMinPointsCards();

        }

    }

    private void boostRowBy(int amount){
        for(CardDisplay card: player.getCardsOnBoardOnRow(playMade.getPlayedCardRowNum())){
            if(card.equals(CardsFactory.leader) == false)
                player.boostCard(card, amount);
        }
    }

    private void healerBoost(int boostAmount){
        for(CardDisplay card: player.getCardsOnBoard()){
            if(card.equals(CardsFactory.healer) == false) {
                if(card.getPoints() <= CardsFactory.healerMaxCardPointsWithBoost){
                    player.boostCard(card, boostAmount);
                }
            }
        }
    }

    private void burnAllMaxPointsCards() {
        int playerMaxPoints = findMaxPoints(player.getCardsOnBoard());
        int enemyMaxPoints = findMaxPoints(enemy.getCardsOnBoard());
        int maxPoints = Math.max(playerMaxPoints, enemyMaxPoints);
        burnCardsWithSpecifiedPoints(player, maxPoints);
        burnCardsWithSpecifiedPoints(enemy, maxPoints);
    }

    private int findMaxPoints(List<CardDisplay> ofBoard){
        int maxPoints = 0;
        for (int j = 0; j < ofBoard.size(); j++) {
            if(ofBoard.get(j).getPoints() > maxPoints)
                maxPoints = ofBoard.get(j).getPoints();
        }
        return maxPoints;
    }

    private void burnCardsWithSpecifiedPoints(OnePlayerDuel ofPlayer, int points) {
        List<CardDisplay> cardsOnBoard = ofPlayer.getCardsOnBoard();
        for (int j = 0; j < cardsOnBoard.size(); j++) {
            if(cardsOnBoard.get(j).getPoints() == points) {
                burnCard(cardsOnBoard.get(j), ofPlayer);
            }
        }
    }
    private void burnAllMinPointsCards() {
        int minPoints = Math.min(
                findMinPoints(player.getCardsOnBoard()), findMinPoints(enemy.getCardsOnBoard())
        );
        burnCardsWithSpecifiedPoints(player, minPoints);
        burnCardsWithSpecifiedPoints(enemy, minPoints);
    }
    private int findMinPoints(List<CardDisplay> ofBoard){
        int minPoints = Integer.MAX_VALUE;
        for (int i = 0; i < ofBoard.size(); i++) {
            if(ofBoard.get(i).getPoints() < minPoints)
                minPoints = ofBoard.get(i).getPoints();
        }
        return minPoints;
    }

    private void ripWholeRow() {
        List<CardDisplay> row = enemy.getCardsOnBoardOnRow(playMade.getAffectedRow());
        for (int i = 0; i < row.size(); i++) {
            strikeCardBy(row.get(i), CardsFactory.ripDamage);
        }
    }

    public void invokeOnTurnEndEffect() {
        List<CardDisplay> cardsOnBoard = player.getCardsOnBoard();
        for (var card : cardsOnBoard) {
            int cardTimer = player.decrementAndGetTimer(card);

            if(cardTimer != CardsFactory.noTimer)
                invokeSpecificTurnEndCardEffect(card, cardTimer);
        }
    }
    private void invokeSpecificTurnEndCardEffect(CardDisplay card, int cardTimer) {
        if(cardTimer == 0) {
           if(card.equals(CardsFactory.trebuchet)) {
                CardDisplay cardToStrike = enemy.getRandomCardFromBoardWithout(CardsFactory.trebuchet);
                strikeCardBy(cardToStrike, CardsFactory.trebuchetDamage);
           }
           else if(card.equals(CardsFactory.goodPerson)) {
                CardDisplay cardToStrike = player.getRandomCardFromBoardWithout(CardsFactory.goodPerson);
                player.boostCard(cardToStrike, CardsFactory.goodPersonBoost);
           }
           else if(card.equals(CardsFactory.longer)) {
               player.boostCard(card, CardsFactory.longerBoost);
           }
        }
    }



    public void invokeOnTurnStartEffect() {
        List<CardDisplay> cardsOnBoard = player.getCardsOnBoard();
        for (var card : cardsOnBoard) {
            int cardTimer = player.decrementAndGetTimer(card);
            if(cardTimer != CardsFactory.noTimer)
                invokeSpecificTurnStartCardEffect(card);
        }

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
                player.strikeCard(maxPointsCard, CardsFactory.rainDamage);
            }
        }
    }

    private void invokeSpecificTurnStartCardEffect(CardDisplay card) {

        int cardTimer = player.decrementAndGetTimer(card);

        if(cardTimer == 0) {
            if(card.equals(CardsFactory.breaker)) {
                CardDisplay anotherBreaker = player.getCardsInDeck().stream().filter(c -> c.equals(CardsFactory.breaker)).findFirst().orElse(new CardDisplay());
                player.placeCardOnBoard(new PlayerPlay(anotherBreaker, player.getCardRow(card)));
            }
        }
    }

    public CardDisplay getPlayChainCard() {
        if(playMade.getPlayedCard().equals(CardsFactory.priest)){
            return playMade.getTargetedCard();
        }
        else if(playMade.getPlayedCard().equals(CardsFactory.witch)) {
            return playMade.getTargetedCard();
        }
        else if(playMade.getPlayedCard().equals(CardsFactory.supplier)) {
            CardDisplay cardFromDeckToChain= player.getCardsInDeck().stream().filter(c -> c.getName().equals(playMade.getTargetedCard().getName())).findFirst().orElse(null);
            if(cardFromDeckToChain== null) {
                return new CardDisplay();
            }
            return cardFromDeckToChain;
        }
        return new CardDisplay();
    }


}
