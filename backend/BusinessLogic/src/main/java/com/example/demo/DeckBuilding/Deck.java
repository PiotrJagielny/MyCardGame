package com.example.demo.DeckBuilding;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.Card;
import com.example.demo.Cards.CardsFactory;
import com.example.demo.Consts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Deck {
    private List<Card> cardsInDeck;
    private List<Card> allCardsPossibleToAdd;
    private String deckName;


    public Deck(List<Card> allCards) {

        cardsInDeck = new ArrayList<Card>();
        deckName="";
        this.allCardsPossibleToAdd = allCards;
    }

    public Deck() {
        this.deckName = "";
        cardsInDeck = new ArrayList<Card>();
        this.allCardsPossibleToAdd = new ArrayList<Card>();
    }

    private void copyAllCards(List<Card> allCards) {
        this.allCardsPossibleToAdd = new ArrayList<Card>();
        for (int i = 0; i < allCards.size(); ++i) {
            this.allCardsPossibleToAdd.add(allCards.get(i));
        }
    }

    public String addCard(CardDisplay cardDisplay){
        String responseMessage="";
        Card card = allCardsPossibleToAdd.stream().filter(c -> c.getDisplay().equals(cardDisplay)).findFirst().orElse(null);
        

        if(cardsInDeck.size() == Consts.maxDeckSize) {
            responseMessage = Consts.DeckFullMessage;
            return responseMessage;
        }

        if(card != null) {
            cardsInDeck.add(card);
            allCardsPossibleToAdd.removeIf(c -> c.equals(card) );

        }
        return responseMessage;
    }

    public void putCardFromDeckBack(CardDisplay cardDisplay){
        Card card = cardsInDeck.stream().filter(c -> c.getDisplay().equals(cardDisplay)).findFirst().orElse(null);

        if(card != null){
            allCardsPossibleToAdd.add(card);
            cardsInDeck.removeIf(c -> c.getDisplay().equals(card.getDisplay()) );
        }
    }

    public List<CardDisplay> getCardsInDeck() {
        return CardsFactory.getCardsDisplay(cardsInDeck);
    }

    public String getDeckName() {
        return deckName;
    }

    public List<CardDisplay> getCardsPossibleToAdd() {
        return CardsFactory.getCardsDisplay(allCardsPossibleToAdd);
    }

    public void sortCardsPossibleToAddBy(Comparator<Card> sortCriteria) {
        allCardsPossibleToAdd.sort(sortCriteria);
    }
}
