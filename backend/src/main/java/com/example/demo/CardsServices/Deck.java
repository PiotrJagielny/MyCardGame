package com.example.demo.CardsServices;

import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.Consts;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> cardsInDeck;
    private List<Card> allCardsPossibleToAdd;
    private String deckName;

    public Deck(List<Card> allCards, String deckName) {
        this.deckName = deckName;
        cardsInDeck = new ArrayList<Card>();
        copyAllCards(allCards);
    }

    private void copyAllCards(List<Card> allCards) {
        this.allCardsPossibleToAdd = new ArrayList<Card>();
        for (int i = 0; i < allCards.size(); ++i) {
            this.allCardsPossibleToAdd.add(allCards.get(i));
        }
    }

    public String addCard(String cardName){
        String responseMessage="";
        Card card = allCardsPossibleToAdd.stream().filter(c -> c.getName().equals(cardName)).findFirst().orElse(null);

        if(cardsInDeck.size() == Consts.MaxDeckSize) {
            responseMessage = Consts.DeckFullMessage;
            return responseMessage;
        }

        cardsInDeck.add(card);
        allCardsPossibleToAdd.removeIf(c -> c.getName().equals(card.getName()) );
        return responseMessage;
    }

    public void putCardFromDeckBack(String cardName){
        Card card = cardsInDeck.stream().filter(c -> c.getName().equals(cardName)).findFirst().orElse(null);

        allCardsPossibleToAdd.add(card);
        cardsInDeck.removeIf(c -> c.getName().equals(card.getName()) );
    }

    public List<Card> getCardsInDeck() {
        return cardsInDeck;
    }

    public String getDeckName() {
        return deckName;
    }

    public List<Card> getCardsPossibleToAdd() {
        return allCardsPossibleToAdd;
    }

}
