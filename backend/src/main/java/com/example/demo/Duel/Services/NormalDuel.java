package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Parser.CardsParser;
import com.example.demo.CardsServices.Parser.NormalCardsParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NormalDuel implements CardDuel{
    private PlayerNumber whosTurn;
    private Map<PlayerNumber, OnePlayerDuel> players;


    public NormalDuel() {
        players = new HashMap<PlayerNumber, OnePlayerDuel>();
        players.put(PlayerNumber.FirstPlayer, new OnePlayerDuel());
        players.put(PlayerNumber.SecondPlayer, new OnePlayerDuel());
        whosTurn = PlayerNumber.FirstPlayer;
    }

    @Override
    public boolean whoWon() {
        return false;
    }


    @Override
    public void parseCardsFor(List<CardDisplay> cardsDisplay, PlayerNumber player) {
        players.get(player).parseCards(cardsDisplay);
    }

    @Override
    public List<CardDisplay> getCardsInDeckDisplayOf(PlayerNumber player) {
        return players.get(player).getCardsInDeck();
    }

    @Override
    public List<CardDisplay> getCardsInHandDisplayOf(PlayerNumber player) {
        return players.get(player).getCardsInHand();
    }

    @Override
    public List<CardDisplay> getCardsOnBoardDisplayOf(PlayerNumber player) {
        return players.get(player).getCardsOnBoard();
    }

    @Override
    public int getBoardPointsOf(PlayerNumber player) {
        return players.get(player).getCardsOnBoard().size();
    }

    @Override
    public void playCardAs(CardDisplay cardToPlayDisplay, PlayerNumber player) {
        if(player == whosTurn)
            players.get(player).playCard(cardToPlayDisplay);
    }

    @Override
    public void dealCards() {
        players.get(PlayerNumber.FirstPlayer).dealCards();
        players.get(PlayerNumber.SecondPlayer).dealCards();
    }

    @Override
    public void setTurnTo(PlayerNumber player) {
        whosTurn = player;
    }
}
