package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;

import java.util.List;

public interface CardDuel {
    boolean whoWon();

    void parseCardsFor(List<CardDisplay> cardsDisplay, PlayerNumber player);
    List<CardDisplay> getCardsInDeckDisplayOf(PlayerNumber player);
    List<CardDisplay> getCardsInHandDisplayOf(PlayerNumber player);
    List<CardDisplay> getCardsOnBoardDisplayOf(PlayerNumber player);
    int getBoardPointsOf(PlayerNumber forPlayer);
    void playCardAs(CardDisplay cardToPlayDisplay, PlayerNumber player);

    void dealCards();
    void setTurnTo(PlayerNumber player);


    public static CardDuel createDuel(){
        return new NormalDuel();
    }

}
