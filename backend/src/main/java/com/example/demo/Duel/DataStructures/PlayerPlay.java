package com.example.demo.Duel.DataStructures;

import com.example.demo.CardsServices.CardDisplay;

public class PlayerPlay {
    private CardDisplay playedCard;
    private int playedOnRow;

    private CardDisplay cardThatGotEffect;
    private int affectedCardOnRow;

    public PlayerPlay(CardDisplay playedCard, int playedOnRow, CardDisplay cardThatGotEffect, int affectedCardOnRow) {
        this.playedCard = playedCard;
        this.playedOnRow = playedOnRow;
        this.cardThatGotEffect = cardThatGotEffect;
        this.affectedCardOnRow = affectedCardOnRow;
    }

    public PlayerPlay(CardDisplay playedCard, int playedOnRow) {
        this.playedCard = playedCard;
        this.playedOnRow = playedOnRow;
    }

    public CardDisplay getPlayedCard() {
        return playedCard;
    }


    public int getPlayedCardRowNum() {
        return playedOnRow;
    }


    public CardDisplay getCardThatGotEffect() {
        return cardThatGotEffect;
    }

    public int getAffectedCardRowNum() {
        return affectedCardOnRow;
    }

}
