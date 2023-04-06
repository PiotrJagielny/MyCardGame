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
    private List<Card> cardsOnBoard;

    private CardsParser parser;

    public NormalDuel() {
        parser = new NormalCardsParser();
        cardsOnBoard = new ArrayList<Card>();
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
    public List<CardDisplay> getPlayerCardsInHandDisplay() {
        return parser.getCardsDisplay(playerCardsInHand);
    }

    @Override
    public void dealCards() {
        Card toDeal = playerCardsInDeck.get(0);

        playerCardsInDeck.remove(0);
        playerCardsInHand.add(toDeal);
    }

    @Override
    public List<CardDisplay> getCardsOnBoardDisplay() {
        return parser.getCardsDisplay(cardsOnBoard);
    }

    @Override
    public void playCard(CardDisplay cardToPlayDisplay) {
        Card cardToPlay = playerCardsInHand.stream().filter(c -> c.getDisplay().equals(cardToPlayDisplay)).findFirst().orElse(null);


        playerCardsInHand.removeIf(c -> c.getDisplay().equals(cardToPlay.getDisplay()));
        cardsOnBoard.add(cardToPlay);
    }

    @Override
    public int getBoardPoints() {
        if(cardsOnBoard.isEmpty()) return 0;
        else return 1;
    }
}
