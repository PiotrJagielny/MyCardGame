package com.example.demo.Duel;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.CardsFactory;
import com.example.demo.Consts;
import com.example.demo.Duel.Rows.RowStatus;
import com.example.demo.Utils;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

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

        placeCardOnBoard();

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
            int maxPoints= Math.max(
                    findPointsByCriteria(player.getCardsOnBoard(), 0,(element,result) -> element > result ),
                    findPointsByCriteria(enemy.getCardsOnBoard(), 0,(element,result) -> element > result )
            );
            burnCardsWithSpecifiedPoints(
                    List.of(player,enemy),
                    List.of(Consts.firstRow, Consts.secondRow, Consts.thirdRow),
                    maxPoints
            );
        }
        else if(p.equals(CardsFactory.blueFire)) {
            if(CardsFactory.blueFireThreshold <= enemy.getRowPoints(playMade.playedOnRow())) {
                int r = playMade.playedOnRow();
                burnCardsWithSpecifiedPoints(
                        List.of(enemy),
                        List.of(playMade.playedOnRow()),
                        findPointsByCriteria(enemy.getRow(r), 0,(element, result) -> element > result)
                );
            }
        }
        else if(p.equals(CardsFactory.doubler)){
            int boostAmount = playMade.getTargetedCard().getPoints();
            player.boostCard(playMade.getTargetedCard(), boostAmount);
        }
        else if(p.equals(CardsFactory.rip)) {
            dealDamageToWholeRow(CardsFactory.ripDamage);
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

            List<CardDisplay> deck = player.getDeck();
            for (CardDisplay card : deck) {
                if(card.equals(CardsFactory.wildRoam)) {
                    player.placeCardOnBoard(new PlayerPlay(card, playMade.playedOnRow()));
                }
            }

        }
        else if(p.equals(CardsFactory.epidemic)) {
            int minPoints = Math.min(
                    findPointsByCriteria(player.getCardsOnBoard(), Integer.MAX_VALUE,(element,result) -> element < result ),
                    findPointsByCriteria(enemy.getCardsOnBoard(), Integer.MAX_VALUE,(element,result) -> element < result )
            );
            burnCardsWithSpecifiedPoints(
                    List.of(player,enemy),
                    List.of(Consts.firstRow, Consts.secondRow, Consts.thirdRow),
                    minPoints
            );
        }
        else if(p.equals(CardsFactory.ginger)) {
            dealDamageToWholeRow(CardsFactory.gingerDamage);
        }
        else if(p.equals(CardsFactory.spy)){
            player.dealCards(1);
        }
        else if(p.equals(CardsFactory.axer)) {
            int weakenedCards = enemy.getCardsOnBoard().stream()
                    .filter(c -> c.getPoints() < c.getBasePoints())
                    .collect(Collectors.toList()).size();

            strikeCardBy(playMade.getTargetedCard(), weakenedCards);
        }
        else if(p.equals(CardsFactory.copier)) {
            int cardsInDeck = player.getDeck().size();
            for (int i = 0; i < CardsFactory.copierCopiesCount; i++) {
                int randomPlace = 0;
                if(cardsInDeck != 0) {
                    randomPlace = Utils.getRandomNumber(0, cardsInDeck - 1);
                }
                player.insertCardIntoDeck(playMade.getTargetedCard(), randomPlace);
            }
        }
        else if(p.equals(CardsFactory.mushrooms)) {
            enemy.decreaseBasePower(playMade.getTargetedCard(), CardsFactory.mushroomsBaseDamage);

        }

    }
    private void placeCardOnBoard() {
        if(CardsFactory.cardsThatArePlacedOnEnemyBoard.contains(playMade.getPlayedCard())){
            enemy.spawnCard(playMade.getPlayedCard(), playMade.playedOnRow());
            player.deleteCardFromHand(playMade.getPlayedCard());
        }
        else {
            player.placeCardOnBoard(playMade);
        }
    }

    private void boostRowBy(int amount){
        for(CardDisplay card: player.getRow(playMade.playedOnRow())){
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

    private int findPointsByCriteria(List<CardDisplay> ofBoard, int initailValue, BiPredicate<Integer, Integer> criteria) {
        int result = initailValue;
        for (int i = 0; i < ofBoard.size(); i++) {
            if(criteria.test(ofBoard.get(i).getPoints(), result)) {
                result = ofBoard.get(i).getPoints();
            }
        }
        return result;

    }
    private void burnCardsWithSpecifiedPoints(List<OnePlayerDuel> players, List<Integer> rows,  int points) {
        for (OnePlayerDuel oneOfBothPlayers : players) {
            for (int row = 0; row < rows.size(); row++) {

                List<CardDisplay> cardsOnRow= oneOfBothPlayers.getRow(row);
                for (CardDisplay card : cardsOnRow) {
                    if(card.getPoints() == points){
                        burnCard(card, oneOfBothPlayers);
                    }
                }
            }
        }
    }

    private void dealDamageToWholeRow(int damage){
        List<CardDisplay> row = enemy.getRow(playMade.getAffectedRow());
        for (int i = 0; i < row.size(); i++) {
            strikeCardBy(row.get(i), damage);
        }
    }

    public void invokeOnTurnEndEffect() {
        List<CardDisplay> cardsOnBoard = player.getCardsOnBoard();
        for (var card : cardsOnBoard) {
            if(CardsFactory.cardsWithTurnEndTimer.contains(card)) {
                player.decrementTimer(card);
                int timer = player.getTimer(card);
                invokeSpecificTurnEndCardEffect(card, timer);
            }
        }
    }
    private void invokeSpecificTurnEndCardEffect(CardDisplay card, int cardTimer) {
        if(cardTimer == 0) {
            player.restartTimer(card);
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
            if(CardsFactory.cardsWithTurnStartTimer.contains(card)) {
                player.decrementTimer(card);
                int timer = player.getTimer(card);
                invokeSpecificTurnStartCardEffect(card, timer );
            }
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

    private void invokeSpecificTurnStartCardEffect(CardDisplay card, int cardTimer) {


        if(cardTimer == 0) {
            player.restartTimer(card);
            if(card.equals(CardsFactory.breaker)) {
                CardDisplay anotherBreaker = player.getDeck().stream().filter(c -> c.equals(CardsFactory.breaker)).findFirst().orElse(new CardDisplay());
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
            CardDisplay cardFromDeckToChain= player.getDeck().stream().filter(c -> c.getName().equals(playMade.getTargetedCard().getName())).findFirst().orElse(null);
            if(cardFromDeckToChain== null) {
                return new CardDisplay();
            }
            return cardFromDeckToChain;
        }
        return new CardDisplay();
    }


}
