package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;

public class PlayerPlay {
    private CardDisplay playedCard;
    private int playedOnRow;

    private CardDisplay cardThatGotEffect;

    private int affectedRow;

    public PlayerPlay(CardDisplay playedCard, int playedOnRow, CardDisplay cardThatGotEffect) {
        this.playedCard = playedCard;
        this.playedOnRow = playedOnRow;
        this.cardThatGotEffect = cardThatGotEffect;
        this.affectedRow = -1;
    }

    public PlayerPlay(CardDisplay playedCard, int playedOnRow, CardDisplay cardThatGotEffect, int affectedRow) {
        this.playedCard = playedCard;
        this.playedOnRow = playedOnRow;
        this.cardThatGotEffect = cardThatGotEffect;
        this.affectedRow = affectedRow;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public PlayerPlay(CardDisplay playedCard, int playedOnRow) {
        this.playedCard = playedCard;
        this.playedOnRow = playedOnRow;
        this.affectedRow = -1;
    }

    public CardDisplay getPlayedCard() {
        return playedCard;
    }


    public int getPlayedCardRowNum() {
        return playedOnRow;
    }


    public CardDisplay getAffectedCard() {
        return cardThatGotEffect;
    }

}