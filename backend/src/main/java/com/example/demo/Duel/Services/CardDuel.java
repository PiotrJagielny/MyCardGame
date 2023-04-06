package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;

import java.util.List;

public interface CardDuel {
    boolean whoWon();

    void parseCards(List<CardDisplay> cardsDisplay);

    List<CardDisplay> getPlayerCardsInDeckDisplay();

    List<CardDisplay> getPlayerCardsInHandDisplay();

    List<CardDisplay> getCardsOnBoardDisplay();

    void dealCards();

    void playCard(CardDisplay cardToPlayDisplay);

    int getBoardPoints();
}
