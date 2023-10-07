package com.example.demo.Duel.Rows;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.Card;
import com.example.demo.Cards.CardsFactory;

import java.util.ArrayList;
import java.util.List;

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
        Card c = findCard(aCard);
        c.boostPointsBy(boostAmount);
    }
    public void decreaseBasePower(CardDisplay card, int amount) {
        Card c = findCard(card);
        c.decreaseBasePower(amount);
        if(c.getDisplay().getBasePoints() <= 0) {
            cards.remove(c);
        }
    }
    public void addStatusTo(CardDisplay card, String status) {
        Card c = findCard(card);
        c.addStatus(status);
    }
    public void removeStatusFromCard(CardDisplay card, String status) {
        Card c = findCard(card);
        c.removeStatus(status);
    }

    private Card findCard(CardDisplay card) {
        return cards.stream().filter(c -> c.getId() == card.getId()).findFirst().orElse(Card.emptyCard());
    }

    public void strikeCardBy(CardDisplay aCard, int strikeAmount){
        Card c = findCard(aCard);
        c.strikeBy(strikeAmount);
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
