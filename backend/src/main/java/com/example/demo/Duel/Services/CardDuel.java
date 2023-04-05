package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;

import java.util.List;

public interface CardDuel {
    boolean whoWon();

    List<CardDisplay> getPlayerCardsInDeckDisplay();

    void parseCards(List<CardDisplay> cardsDisplay);

    List<CardDisplay> getPlayerCardsInHand();

    void DealCards();
}
