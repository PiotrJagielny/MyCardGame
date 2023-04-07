package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;

import java.util.List;

public interface CardDuel {
    boolean whoWon();

    void parseCards_forPlayer1(List<CardDisplay> cardsDisplay);
    void parseCards_forPlayer2(List<CardDisplay> cardsDisplay);

    List<CardDisplay> getCardsInDeckDisplay_player1();
    List<CardDisplay> getCardsInDeckDisplay_player2();

    List<CardDisplay> getCardsInHandDisplay_player1();
    List<CardDisplay> getCardsInHandDisplay_player2();

    List<CardDisplay> getCardsOnBoardDisplay_player1();
    List<CardDisplay> getCardsOnBoardDisplay_player2();

    void dealCards();

    void playCard_asPlayer1(CardDisplay cardToPlayDisplay);
    void playCard_asPlayer2(CardDisplay cardToPlayDisplay);

    int getBoardPoints_player1();
    int getBoardPoints_player2();




    public static CardDuel createDuel(){
        return new NormalDuel();
    }
}
