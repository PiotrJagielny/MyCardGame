package com.example.demo.Cards;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> CardsInDeck;
    private String DeckName;

    public Deck() {
        DeckName = "";
        CardsInDeck = new ArrayList<Card>();
    }

    public void AddCard(Card card){
        CardsInDeck.add(card);
    }

    public List<Card> getCardsInDeck() {
        return CardsInDeck;
    }
    public void RemoveCardFromDeck(Card card){
        CardsInDeck.removeIf(c -> c.getName() == card.getName());
    }

    public Card GetCardBy(String Name){
        return CardsInDeck.stream().filter(c -> c.getName().equals(Name)).findFirst().orElse(null);
    }

    public void setCardsInDeck(List<Card> cardsInDeck) {
        CardsInDeck = cardsInDeck;
    }
}
