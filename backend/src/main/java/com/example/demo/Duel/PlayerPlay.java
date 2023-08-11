package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;

public class PlayerPlay {
    private CardDisplay playedCard;
    private int playedOnRow;

    private CardDisplay cardTargeted;

    private int affectedRow;

    public PlayerPlay(CardDisplay playedCard, int playedOnRow, CardDisplay cardThatGotEffect) {
        this.playedCard = playedCard;
        this.playedOnRow = playedOnRow;
        this.cardTargeted = cardThatGotEffect;
        this.affectedRow = -1;
    }

    public PlayerPlay(CardDisplay playedCard, int playedOnRow, CardDisplay cardThatGotEffect, int affectedRow) {
        this.playedCard = playedCard;
        this.playedOnRow = playedOnRow;
        this.cardTargeted = cardThatGotEffect;
        this.affectedRow = affectedRow;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public PlayerPlay(CardDisplay playedCard, int playedOnRow) {
        this.playedCard = playedCard;
        this.playedOnRow = playedOnRow;
        this.affectedRow = 0;
        this.cardTargeted = new CardDisplay();
    }
    public PlayerPlay(int rowTargeted, CardDisplay playedCard) {
        this.affectedRow = rowTargeted;
        this.playedCard = playedCard;
        this.playedOnRow = 0;
        this.cardTargeted = new CardDisplay();
    }
    public PlayerPlay(CardDisplay playedCard) {
        this.playedCard = playedCard;
        this.affectedRow = 0;
        this.playedOnRow = 0;
        this.cardTargeted = new CardDisplay();
    }
    public PlayerPlay(CardDisplay playedCard, CardDisplay cardTargeted) {
        this.playedCard = playedCard;
        this.affectedRow = 0;
        this.playedOnRow = 0;
        this.cardTargeted =cardTargeted;
    }

    public CardDisplay getPlayedCard() {
        return playedCard;
    }


    public int getPlayedCardRowNum() {
        return playedOnRow;
    }


    public CardDisplay getTargetedCard() {
        return cardTargeted;
    }

}