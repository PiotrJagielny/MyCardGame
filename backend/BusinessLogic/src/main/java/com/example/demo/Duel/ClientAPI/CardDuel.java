package com.example.demo.Duel.ClientAPI;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.CardsFactory;
import com.example.demo.Consts;
import com.example.demo.Duel.CardEffects;
import com.example.demo.Duel.OnePlayerDuel;
import com.example.demo.Duel.PlayerPlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CardDuel {
    private String whosTurn;

    private Map<String, OnePlayerDuel> players;

    public CardDuel() {
        whosTurn = "";
        players = new HashMap<>();
    }

    public boolean didWon(String player) {
        return players.get(player).didWon();
    }

    public void parseCardsFor(List<CardDisplay> cardsDisplay, String player) {
        players.get(player).parseCards(cardsDisplay);
    }

    public List<CardDisplay> getDeckOf(String player) {
        return players.get(player).getDeck();
    }

    public List<CardDisplay> getHandOf(String player) {
        return players.get(player).getHand();
    }

    public List<CardDisplay> getRowOf(String player, int rowNumber) {
        return players.get(player).getRow(rowNumber);
    }

    public int getRowPointsOf(String player, int row) {
        return players.get(player).getRowPoints(row);
    }

    public CardDisplay playCardAs(PlayerPlay playMade, String player) {
        if(isTurnOf(player)){
            CardEffects effects = new CardEffects(players.get(player), players.get(getOpponentOf(player)), playMade);
            effects.invokeOnPlaceEffect();
            if(CardsFactory.cardsWithPlayChainPossibility.contains(playMade.getPlayedCard()) &&
               isNullOrEmpty(playMade.getTargetedCard()) == false) {
                return effects.getPlayChainCard();
            }
            else {
                invokeOnTurnsEffects(effects, getOpponentOf(player), player);

                if(players.get(getOpponentOf(player)).didEndRound()) {
                    invokeOnTurnsEffects(effects,player, getOpponentOf(player) );
                }
                changeTurn();
            }
        }
        return new CardDisplay();
    }

    private void invokeOnTurnsEffects(CardEffects effects, String player, String enemy) {
        effects.invokeOnTurnEndEffect();
        effects.changePerspective(players.get(player), players.get(enemy));
        effects.invokeOnTurnStartEffect();

    }
    public void setFractionFor(String player, String fraction) {players.get(player).setFraction(fraction);}

    private boolean isNullOrEmpty(CardDisplay card) {
        return card == null || card.equals(new CardDisplay());
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
            obj.dealCards(6);
        }
    }


    public void registerPlayerToDuel(String player, String fraction) {
        players.put(player, new OnePlayerDuel(fraction));
        whosTurn = player;
    }


    public void endRoundFor(String player) {
        if(whosTurn.equals(player)){
            CardEffects effects = new CardEffects(players.get(player), players.get(getOpponentOf(player)));
            invokeOnTurnsEffects(effects, getOpponentOf(player), player);
            changeTurn();
            players.get(player).endRound();

            if (didPlayersEndedRound()) {
                startNewRound();
            }

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
            ).stream().filter(addFilter(cardPlayed)).collect(Collectors.toList());
        }
        else
            return List.of();
    }
    private Predicate<CardDisplay> addFilter(CardDisplay c) {
        if(c.getColor().equals(Consts.silver)||c.getColor().equals(Consts.bronze)) {
            return card -> !card.getColor().equals(Consts.gold);
        }
        else {
            return card -> true;
        }
    }
    public List<Integer> getPossibleRowsToAffect(CardDisplay cardPlayed) {
        return CardsFactory.getCardRowsToAffect(cardPlayed.getName());
    }


    public String getRowStatusOf(String userName, int row) {
        return players.get(userName).getRowStatusName(row);
    }

    public List<CardDisplay> getGraveyardOf(String player) {
        return players.get(player).getGraveyard();
    }

    public void mulliganCardFor(CardDisplay cardToMulligan, String player) {
        players.get(player).mulliganCard(cardToMulligan);
    }
}
