package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Parser.CardsParser;
import com.example.demo.CardsServices.Parser.NormalCardsParser;

import java.util.ArrayList;
import java.util.List;

public class NormalDuel implements CardDuel{

    private Boolean areCardsAdded;
    private List<Card> playerCardsInDeck;
    private List<Card> playerCardsInHand;

    private CardsParser parser;

    public NormalDuel() {
        parser = new NormalCardsParser();
        playerCardsInDeck = new ArrayList<Card>();
        playerCardsInHand = new ArrayList<Card>();
        this.areCardsAdded = false;
    }

    @Override
    public boolean whoWon() {
        return false;
    }

    @Override
    public List<CardDisplay> getPlayerCardsInDeckDisplay() {
        return parser.getCardsDisplay(playerCardsInDeck);
    }

    @Override
    public void parseCards(List<CardDisplay> cardsDisplay) {
        areCardsAdded = true;
        parser.addCardsToParse(cardsDisplay);
        playerCardsInDeck = parser.getParsedCards();
    }

    @Override
    public List<CardDisplay> getPlayerCardsInHand() {
        return parser.getCardsDisplay(playerCardsInHand);
    }

    @Override
    public void DealCards() {
        Card toDeal = playerCardsInDeck.get(0);

        playerCardsInDeck.remove(0);
        playerCardsInHand.add(toDeal);
    }
}
