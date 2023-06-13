package com.example.demo.Duel.DataStructures;

import com.example.demo.CardsServices.CardDisplay;

public class PlayerPlay {
    private CardDisplay playedCard;
    private int playedOnRow;

    private CardDisplay cardThatGotEffect;


    public PlayerPlay(CardDisplay playedCard, int playedOnRow, CardDisplay cardThatGotEffect) {
        this.playedCard = playedCard;
        this.playedOnRow = playedOnRow;
        this.cardThatGotEffect = cardThatGotEffect;
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


    public CardDisplay getAffectedCard() {
        return cardThatGotEffect;
    }

}