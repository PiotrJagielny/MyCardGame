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

    public void boostCardBy(CardDisplay aCard, int boostAmount){
        int cardIndex = cards.indexOf(findCard(aCard));
        if(cardIndex == -1) return;
        cards.get(cardIndex).boostPointsBy(boostAmount);
    }
    public void decreaseBasePower(CardDisplay card, int amount) {
        int cardIndex = cards.indexOf(findCard(card));
        if(cardIndex == -1) return;
        cards.get(cardIndex).decreaseBasePower(amount);
        if(cards.get(cardIndex).getDisplay().getBasePoints() <= 0) {
            cards.remove(cardIndex);
        }
    }
    public void addStatusTo(CardDisplay card, String status) {
        int cardIndex = cards.indexOf(findCard(card));
        if(cardIndex == -1) return;
        cards.get(cardIndex).addStatus(status);
    }
    public void removeStatusFromCard(CardDisplay card, String status) {
        int cardIndex = cards.indexOf(findCard(card));
        if(cardIndex == -1) return;
        cards.get(cardIndex).removeStatus(status);
    }

    private Card findCard(CardDisplay card) {
        return cards.stream().filter(c -> c.getId() == card.getId()).findFirst().orElse(Card.emptyCard());
    }

    public void strikeCardBy(CardDisplay aCard, int strikeAmount){
        int cardIndex = cards.indexOf(findCard(aCard));
        if(cardIndex == -1) return;
        cards.get(cardIndex).strikeBy(strikeAmount);
    }
    public void deleteCard(CardDisplay aCard) {
        cards.remove(findCard(aCard));
    }

    public int getRowPoints(){
        return cards.stream().mapToInt(c -> c.getPoints()).sum();
    }

    public void burnCard(CardDisplay card){
        cards.remove(findCard(card));
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
