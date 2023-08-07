package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.CardsServices.CardsEffects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDuel {
    private String whosTurn;

    private Map<String, OnePlayerDuel> players;

    public CardDuel() {
        whosTurn = "";
        players = new HashMap<String, OnePlayerDuel>();
    }

    public boolean didWon(String player) {
        return players.get(player).didWon();
    }

    public void parseCardsFor(List<CardDisplay> cardsDisplay, String player) {
        players.get(player).parseCards(cardsDisplay);
    }

    public List<CardDisplay> getCardsInDeckDisplayOf(String player) {
        return players.get(player).getCardsInDeck();
    }

    public List<CardDisplay> getCardsInHandDisplayOf(String player) {
        return players.get(player).getCardsInHand();
    }

    public List<CardDisplay> getCardsOnBoardDisplayOf(String player, int rowNumber) {
        return players.get(player).getCardsOnBoardOnRow(rowNumber);
    }

    public int getRowPointsOf(String player, int row) {
        return players.get(player).getRowPoints(row);
    }

    public CardDisplay playCardAs(PlayerPlay playMade, String player) {
        if(isTurnOf(player)){
            CardEffects effects = new CardEffects(players.get(player), players.get(getOpponentOf(player)), playMade);
            effects.invokeOnPlaceEffect();
            if(CardsFactory.cardsWithPlayChainPossibility.contains(playMade.getPlayedCard()) &&
               playMade.getTargetedCard() != null ) {
                return playMade.getTargetedCard();
            }
            else {
                effects.invokeOnTurnEndEffect();
                effects.changePerspective(players.get(getOpponentOf(player)), players.get(player));
                effects.invokeOnTurnStartEffect();
                changeTurn();
            }
        }
        return new CardDisplay();
    }

    public boolean isTurnOf(String player) {
        return whosTurn.equals(player) && hasGameEnded() == false;
    }

    private boolean hasGameEnded(){
        for (Map.Entry<String, OnePlayerDuel> entry : players.entrySet()) {
            if(didWon(entry.getKey()) == true){
                return true;
            }
        }
        return false;
    }


    private void changeTurn(){
        String opponent = getOpponentOf(whosTurn);
        if(players.get(opponent).didEndRound() == false) {
            whosTurn = opponent;
        }
    }

    public String getOpponentOf(String player){
        List<String> playersNames = new ArrayList<>(players.keySet());
        int indexOfPlayerWhoHasTurn = playersNames.indexOf(player);
        int nextPlayerIndex = (indexOfPlayerWhoHasTurn + 1) % playersNames.size();
        return playersNames.get(nextPlayerIndex);
    }


    public void dealCards() {
        for (Map.Entry<String, OnePlayerDuel> entry : players.entrySet()) {
            OnePlayerDuel obj = entry.getValue();
            obj.dealCards(4);
        }
    }


    public void registerPlayerToDuel(String player) {
        players.put(player, new OnePlayerDuel());
        whosTurn = player;
    }


    public void endRoundFor(String player) {
        if(whosTurn.equals(player)){
            CardEffects effects = new CardEffects(players.get(player), players.get(getOpponentOf(player)));
            effects.invokeOnTurnEndEffect();
            effects.changePerspective(players.get(getOpponentOf(player)), players.get(player));
            effects.invokeOnTurnStartEffect();
            changeTurn();
            players.get(player).endRound();
            if (didPlayersEndedRound()) startNewRound();

        }
    }

    private boolean didPlayersEndedRound() {
        for (Map.Entry<String, OnePlayerDuel> entry : players.entrySet()) {
            OnePlayerDuel obj = entry.getValue();
            if(obj.didEndRound() == false)
                return false;
        }
        return true;
    }
    public boolean didEnemyEndedRound(String userName) {
        return players.get(getOpponentOf(userName)).didEndRound();
    }

    private void startNewRound() {
        Map<String, Integer> playerNameToBoardPointsMap = getPlayersBoardPoints();
        for (Map.Entry<String, OnePlayerDuel> entry : players.entrySet()) {
            OnePlayerDuel obj = entry.getValue();
            String opponent = getOpponentOf(entry.getKey());
            obj.startNewRound(playerNameToBoardPointsMap.get(opponent));
        }
    }
    private Map<String, Integer> getPlayersBoardPoints(){
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, OnePlayerDuel> entry : players.entrySet()) {
            OnePlayerDuel obj = entry.getValue();
            result.put(entry.getKey(), obj.getBoardPoints());
        }
        return result;
    }


    public boolean didEndRound(String player) {
        return players.get(player).didEndRound();
    }


    public int getWonRoundsOf(String player) {
        return players.get(player).getWonRounds();
    }


    public static CardDuel createDuel(){
        return new CardDuel();
    }

    public List<CardDisplay> getPossibleTargetsOf(CardDisplay cardPlayed, String player) {
        if(whosTurn.equals(player) == true){
            return CardsFactory.getPossibleTargetsOf(
                    cardPlayed, players.get(player), players.get(getOpponentOf(player))
            );
        }
        else
            return List.of();
    }
    public List<Integer> getPossibleRowsToAffect(CardDisplay cardPlayed) {
        return CardsFactory.getCardRowsToAffect(cardPlayed.getName());
    }


    public String getRowStatusOf(String userName, int row) {
        return players.get(userName).getRowStatusName(row);
    }

    public List<CardDisplay> getPlayerGraveyard(String firstPlayer) {
        return players.get(firstPlayer).getGraveyard();
    }
}
