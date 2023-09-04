package com.example.demo.Duel.Rows;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.Card;
import com.example.demo.Cards.CardsFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {

    private List<Card> cards;
    private RowStatus status;

    public Row() {
        cards = new ArrayList<>();
        status = RowStatus.NoStatus;
    }

    public void clearRow(){
        List<CardDisplay> clearedCards = CardsFactory.getCardsDisplay(cards);
        cards.clear();
        for (CardDisplay card : clearedCards) {
           if(card.equals(CardsFactory.cow)) {
               cards.add(Card.createCard(CardsFactory.chort));
           }
        }
    }


    public void play(Card aCard){
        if(aCard.getPoints() > 0 && aCard != Card.emptyCard()) {
            cards.add(aCard);
        }
    }

    public void boostCardBy(Card aCard, int boostAmount){
        int cardIndex = cards.indexOf(aCard);
        if(cardIndex == -1) return;
        cards.get(cardIndex).boostPointsBy(boostAmount);
    }
    public void addStatusTo(Card card, String status) {
        int cardIndex = cards.indexOf(card);
        if(cardIndex == -1) return;
        cards.get(cardIndex).addStatus(status);
    }
    public void removeStatusFromCard(Card card, String status) {
        int cardIndex = cards.indexOf(card);
        if(cardIndex == -1) return;
        cards.get(cardIndex).removeStatus(status);
    }

    public void strikeCardBy(Card aCard, int strikeAmount){
        int cardIndex = cards.indexOf(aCard);
        if(cardIndex == -1) return;
        cards.get(cardIndex).strikeBy(strikeAmount);
    }
    public void deleteCard(Card aCard) {
        cards.remove(aCard);
    }

    public int getRowPoints(){
        return cards.stream().mapToInt(c -> c.getPoints()).sum();
    }

    public void burnCard(Card card){
        cards.remove(card);
    }


    public List<Card> getCards() {
        return cards;
    }

    public Card get(int cardId){
        return cards.get(cardId);
    }

    public void setStatus(RowStatus status) {
        this.status = status;
    }

    public String getStatusName() {
        return status.toString();
    }

    public void clearStatus() {
        status= RowStatus.NoStatus;
    }

    public void spawnCard(CardDisplay card) {
        cards.add(Card.createCard(card));
    }

}
