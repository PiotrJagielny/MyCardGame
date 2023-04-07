package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Parser.CardsParser;
import com.example.demo.CardsServices.Parser.NormalCardsParser;

import java.util.ArrayList;
import java.util.List;

public class NormalDuel implements CardDuel{
    private OnePlayerDuel firstPlayer;
    private OnePlayerDuel secondPlayer;


    public NormalDuel() {
        firstPlayer = new OnePlayerDuel();
        secondPlayer = new OnePlayerDuel();
    }

    @Override
    public boolean whoWon() {
        return false;
    }

    @Override
    public List<CardDisplay> getCardsInDeckDisplay_player1() {
        return firstPlayer.getCardsInDeck();
    }
    @Override
    public List<CardDisplay> getCardsInDeckDisplay_player2() {
        return secondPlayer.getCardsInDeck();
    }

    @Override
    public void parseCards_forPlayer1(List<CardDisplay> cardsDisplay) {
        firstPlayer.parseCards(cardsDisplay);

    }
    @Override
    public void parseCards_forPlayer2(List<CardDisplay> cardsDisplay) {
        secondPlayer.parseCards(cardsDisplay);
    }

    @Override
    public List<CardDisplay> getCardsInHandDisplay_player1() {
        return firstPlayer.getCardsInHand();
    }
    @Override
    public List<CardDisplay> getCardsInHandDisplay_player2() {
        return secondPlayer.getCardsInHand();
    }

    @Override
    public void dealCards() {
        firstPlayer.dealCards();
        secondPlayer.dealCards();
    }

    @Override
    public List<CardDisplay> getCardsOnBoardDisplay_player1() {
        return firstPlayer.getCardsOnBoard();
    }
    @Override
    public List<CardDisplay> getCardsOnBoardDisplay_player2() {
        return secondPlayer.getCardsOnBoard();
    }

    @Override
    public void playCard_asPlayer1(CardDisplay cardToPlayDisplay) {
        firstPlayer.playCard(cardToPlayDisplay);
    }
    @Override
    public void playCard_asPlayer2(CardDisplay cardToPlayDisplay) {
        secondPlayer.playCard(cardToPlayDisplay);
    }

    @Override
    public int getBoardPoints_player1() {
        return firstPlayer.getCardsOnBoard().size();
    }

    @Override
    public int getBoardPoints_player2() {
        return secondPlayer.getCardsOnBoard().size();
    }
}
