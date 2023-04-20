package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;

import java.util.List;

public interface CardDuel {
    boolean didWon(String player);

    void parseCardsFor(List<CardDisplay> cardsDisplay, String player);
    List<CardDisplay> getCardsInDeckDisplayOf(String player);
    List<CardDisplay> getCardsInHandDisplayOf(String player);

    List<CardDisplay> getCardsOnBoardDisplayOf(String player, int rowNumber);
    int getBoardPointsOf(String player);
    void playCardAs(CardDisplay cardToPlayDisplay, String player, int onRow);

    void dealCards();
    void registerPlayerToDuel(String player);


    void endRoundFor(String player);

    boolean isTurnOf(String player);
    boolean didEndRound(String player);

    int getWonRoundsOf(String player);


    public static CardDuel createDuel(){
        return new NormalDuel();
    }

}
