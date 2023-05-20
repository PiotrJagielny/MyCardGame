package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.CardsServices.CardsEffects.*;
import com.example.demo.Consts;
import com.example.demo.Duel.DataStructures.PlayerPlay;

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

    public int getBoardPointsOf(String player) {
        return players.get(player).getBoardPoints();
    }

    public void playCardAs(PlayerPlay playMade, String player) {

        if(isTurnOf(player)){
            CardEffect effect = new CardEffect(players.get(player), players.get(getOpponentOf(player)), playMade);
//            if(playMade.getPlayedCard().equals(CardsFactory.booster)){
//                players.get(player).boostCard(playMade.getAffectedCard(), 3);
//                players.get( player ).placeCardOnBoard(playMade);
//            }
//            else if(playMade.getPlayedCard().equals(CardsFactory.leader)){
//                for(CardDisplay cardOnRow: players.get(player).getCardsOnBoardOnRow(playMade.getAffectedCardRowNum())){
//                    players.get(player).boostCard(cardOnRow, 2);
//                }
//                players.get( player ).placeCardOnBoard(playMade);
//                effect.boostRowBy(2);
//            }
//            else if(playMade.getPlayedCard().equals(CardsFactory.woodTheHealer)){
//                for (int i = 0; i < Consts.rowsNumber; i++) {
//                    for(CardDisplay cardOnRow : players.get(player).getCardsOnBoardOnRow(i)){
//                        if(cardOnRow.getPoints() < 3){
//                            players.get(player).boostCard(cardOnRow, 2);
//                        }
//                    }
//                }
//                players.get( player ).placeCardOnBoard(playMade);
//                effect.woodTheHealerBoost(2);
//
//            }
//            else if(playMade.getPlayedCard().equals( CardsFactory.fireball )){
//                players.get( player ).placeCardOnBoard(playMade);
//                players.get( getOpponentOf(player) ).strikeCard(playMade.getAffectedCard(), 3);
//
//            }
//            else{
//                players.get( player ).placeCardOnBoard(playMade);
//            }
            effect.invokeEffect();


            changeTurn();
        }
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
        String g = whosTurn;
        if(players.get(opponent).didEndRound() == false) {
            whosTurn = opponent;;
        }

    }

    private String getOpponentOf(String player){
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
        changeTurn();
        players.get(player).endRound();
        if (didPlayersEndedRound()) startNewRound();
    }

    private boolean didPlayersEndedRound() {
        for (Map.Entry<String, OnePlayerDuel> entry : players.entrySet()) {
            OnePlayerDuel obj = entry.getValue();
            if(obj.didEndRound() == false)
                return false;
        }
        return true;
    }

    private void startNewRound() {
        Map<String, Integer> playerNameToBoardPointsMap = getPlayersNameToBoardPointsMap();
        for (Map.Entry<String, OnePlayerDuel> entry : players.entrySet()) {
            OnePlayerDuel obj = entry.getValue();
            String opponent = getOpponentOf(entry.getKey());
            obj.startNewRound(playerNameToBoardPointsMap.get(opponent));
        }
    }
    private Map<String, Integer> getPlayersNameToBoardPointsMap(){
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

}
