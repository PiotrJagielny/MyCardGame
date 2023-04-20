package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Row {

    private List<Card> cards;

    public Row() {
        cards = new ArrayList<>();
    }

    public void clearBoard(){
        cards.clear();
    }

    public void play(Card aCard){
        cards.add(aCard);
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

    public Card get(int cardId){
        return cards.get(cardId);
    }
}
