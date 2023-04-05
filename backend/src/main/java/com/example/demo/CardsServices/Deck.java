package com.example.demo.CardsServices;

import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Parser.CardsParser;
import com.example.demo.CardsServices.Parser.NormalCardsParser;
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

    public String addCard(CardDisplay cardDisplay){
        String responseMessage="";
        Card card = allCardsPossibleToAdd.stream().filter(c -> c.getDisplay().getName().equals(cardDisplay.getName())).findFirst().orElse(null);

        if(cardsInDeck.size() == Consts.MaxDeckSize) {
            responseMessage = Consts.DeckFullMessage;
            return responseMessage;
        }

        cardsInDeck.add(card);
        allCardsPossibleToAdd.removeIf(c -> c.getDisplay().getName().equals(card.getDisplay().getName()) );
        return responseMessage;
    }

    public void putCardFromDeckBack(CardDisplay cardDisplay){
        Card card = cardsInDeck.stream().filter(c -> c.getDisplay().getName().equals(cardDisplay.getName())).findFirst().orElse(null);

        allCardsPossibleToAdd.add(card);
        cardsInDeck.removeIf(c -> c.getDisplay().getName().equals(card.getDisplay().getName()) );
    }

    public List<CardDisplay> getCardsInDeck() {
        CardsParser parser = new NormalCardsParser();
        return parser.getCardsDisplay(cardsInDeck);
    }

    public String getDeckName() {
        return deckName;
    }

    public List<CardDisplay> getCardsPossibleToAdd() {
        CardsParser parser = new NormalCardsParser();
        return parser.getCardsDisplay(allCardsPossibleToAdd);
    }

}
