package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;

import java.util.List;

public interface CardDuel {
    boolean whoWon();

    void parseCardsFor(List<CardDisplay> cardsDisplay, String player);
    List<CardDisplay> getCardsInDeckDisplayOf(String player);
    List<CardDisplay> getCardsInHandDisplayOf(String player);
    List<CardDisplay> getCardsOnBoardDisplayOf(String player);
    int getBoardPointsOf(String player);
    void playCardAs(CardDisplay cardToPlayDisplay, String player);

    void dealCards();
    void setTurnTo(String player);
    void registerPlayerToDuel(String player);


    void endRoundFor(String player);

    boolean didEndRound(String player);



    public static CardDuel createDuel(){
        return new NormalDuel();
    }

    int getWonRoundsOf(String player);
}
