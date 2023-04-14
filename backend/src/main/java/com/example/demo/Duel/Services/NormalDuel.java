package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;

import java.util.*;

public class NormalDuel implements CardDuel{

    private String whosTurn;

    private Map<String, OnePlayerDuel> players;


    public NormalDuel() {
        whosTurn = "";
        players = new HashMap<String, OnePlayerDuel>();
    }



    @Override
    public boolean whoWon() {
        return false;
    }


    @Override
    public void parseCardsFor(List<CardDisplay> cardsDisplay, String player) {
        players.get(player).parseCards(cardsDisplay);
    }

    @Override
    public List<CardDisplay> getCardsInDeckDisplayOf(String player) {
        return players.get(player).getCardsInDeck();
    }

    @Override
    public List<CardDisplay> getCardsInHandDisplayOf(String player) {
        return players.get(player).getCardsInHand();
    }

    @Override
    public List<CardDisplay> getCardsOnBoardDisplayOf(String player) {
        return players.get(player).getCardsOnBoard();
    }

    @Override
    public List<CardDisplay> getCardsOnCemeteryDisplayOf(String player) {
        return players.get(player).getCardsOnCemetery();
    }

    @Override
    public int getBoardPointsOf(String player) {
        return players.get(player).getBoardPoints();
    }

    @Override
    public void playCardAs(CardDisplay cardToPlayDisplay, String player) {
        if(player.equals(whosTurn)){
            players.get(player).playCard(cardToPlayDisplay);
            changeTurn();
        }
    }

    private void changeTurn(){
        String opponent = getOpponentOf(whosTurn);
        if(players.get(opponent).didEndRound() == false) {
            whosTurn = opponent;
        }
    }

    private String getOpponentOf(String player){
        List<String> playersNames = new ArrayList<>(players.keySet());
        int indexOfPlayerWhoHasTurn = playersNames.indexOf(player);
        int nextPlayerIndex = (indexOfPlayerWhoHasTurn + 1) % playersNames.size();
        return playersNames.get(nextPlayerIndex);
    }

    @Override
    public void dealCards() {
        for (Map.Entry<String, OnePlayerDuel> entry : players.entrySet()) {
            OnePlayerDuel obj = entry.getValue();
            obj.dealCards();
        }
    }

    @Override
    public void setTurnTo(String player) {
        whosTurn = player;
    }

    @Override
    public void registerPlayerToDuel(String player) {
        players.put(player, new OnePlayerDuel());
    }

    @Override
    public void endRoundFor(String player) {
        changeTurn();
        players.get(player).endRound();
        if(didPlayersEndedRound()) startNewRound();
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
        for (Map.Entry<String, OnePlayerDuel> entry : players.entrySet()) {
            OnePlayerDuel obj = entry.getValue();

            String opponent = getOpponentOf(entry.getKey());
            int opponentBoardPoints = players.get(opponent).getBoardPoints();
            obj.startNewRound(opponentBoardPoints);
        }
    }

    @Override
    public boolean didEndRound(String player) {
        return players.get(player).didEndRound();
    }

    @Override
    public int getWonRoundsOf(String player) {
        return players.get(player).getWonRounds();
    }
}
