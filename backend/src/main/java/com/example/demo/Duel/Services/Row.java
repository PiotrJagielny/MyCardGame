package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.CardsParser;

import java.util.ArrayList;
import java.util.List;

public class Row {

    private List<Card> cards;

    public Row() {
        cards = new ArrayList<>();
    }

    public void clearRow(){
        cards.clear();
    }

    public void play(Card aCard){
        cards.add(aCard);
    }

    public void boostCardBy(Card aCard, int boostAmount){
        int cardIndex = cards.indexOf(aCard);
        cards.get(cardIndex).boostPointsBy(boostAmount);
    }

    public void strikeCardBy(Card aCard){
        int cardIndex = cards.indexOf(aCard);
        cards.get(cardIndex).strikeBy(3);
    }

    public void boostRowBy(int amount){
        for(Card card: cards){
            card.boostPointsBy(amount);
        }
    }

    public int getRowPoints(){
        int result = 0;
        for(int i = 0 ; i < cards.size() ; ++i){
            result += cards.get(i).getPoints();
        }
        return result;
    }

    public List<Card> getCards() {
        return cards;
    }
    public List<CardDisplay> getCardsDisplays() {
        return CardsParser.getCardsDisplay( cards);
    }

    public Card get(int cardId){
        return cards.get(cardId);
    }
}
