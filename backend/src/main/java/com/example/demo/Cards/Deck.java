package com.example.demo.Cards;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> CardsInDeck;

    public Deck() {
        CardsInDeck = new ArrayList<Card>();
    }

    public void AddCard(Card card){
        CardsInDeck.add(card);
    }

    public List<Card> getCardsInDeck() {
        return CardsInDeck;
    }

    public void setCardsInDeck(List<Card> cardsInDeck) {
        CardsInDeck = cardsInDeck;
    }
}
