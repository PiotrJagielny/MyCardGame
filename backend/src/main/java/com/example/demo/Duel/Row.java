package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.CardsServices.CardsParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {

    private List<Card> cards;
    private RowStatus status;
    private Map<String, Integer> cardsTimers;

    public Row() {
        cardsTimers = new HashMap<>();
        cards = new ArrayList<>();
        status = RowStatus.NoStatus;
    }

    public void clearRow(){
        List<CardDisplay> clearedCards = CardsParser.getCardsDisplay(cards);
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
            int cardTimer = CardsFactory.getCardTimer(aCard.getDisplay());
            if(cardTimer != CardsFactory.noTimer) {
                cardsTimers.put(aCard.getDisplay().getName(), cardTimer);
            }
        }
    }

    public void boostCardBy(Card aCard, int boostAmount){
        int cardIndex = cards.indexOf(aCard);
        if(cardIndex == -1) return;
        cards.get(cardIndex).boostPointsBy(boostAmount);
    }

    public void strikeCardBy(Card aCard, int strikeAmount){
        int cardIndex = cards.indexOf(aCard);
        if(cardIndex == -1) return;
        if(cards.get(cardIndex).getPoints() - strikeAmount < 1) {
            cards.remove(cardIndex);
        }
        else {
            cards.get(cardIndex).strikeBy(strikeAmount);
        }
    }

    public int getRowPoints(){
        int result = 0;
        for(int i = 0 ; i < cards.size() ; ++i){
            result += cards.get(i).getPoints();
        }
        return result;
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

    public int decrementAndGetTimer(CardDisplay card) {
        if(cardsTimers.containsKey(card.getName()) == false) return -1;

        int result = cardsTimers.get(card.getName()) - 1;
        cardsTimers.put(card.getName(), result);
        if(cardsTimers.get(card.getName()) == 0)  {
            cardsTimers.put(card.getName(), CardsFactory.getCardTimer(card));
        }
        return result;
    }
    public int getTimer(CardDisplay card) {
        if(cardsTimers.containsKey(card.getName())) {
            return cardsTimers.get(card.getName());
        }
        return -1;
    }
}
