package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NormalDuel implements CardDuel{

    private String whosTurn;

    private Map<String, OnePlayerDuel> players;


    public NormalDuel() {
        whosTurn = "";
        players = new HashMap<String, OnePlayerDuel>();
        whosTurn = PlayerNumber.FirstPlayer.toString();
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
    public int getBoardPointsOf(String player) {
        return players.get(player).getCardsOnBoard().size();
    }

    @Override
    public void playCardAs(CardDisplay cardToPlayDisplay, String player) {
        if(player.equals(whosTurn))
            players.get(player).playCard(cardToPlayDisplay);
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
}
