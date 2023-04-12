package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Parser.CardsParser;
import com.example.demo.CardsServices.Parser.NormalCardsParser;

import java.util.ArrayList;
import java.util.List;

public class OnePlayerDuel {

    private List<Card> cardsInDeck;
    private List<Card> cardsInHand;
    private List<Card> cardsOnBoard;

    private CardsParser parser;

    public OnePlayerDuel() {
        cardsOnBoard = new ArrayList<Card>();
        cardsInDeck = new ArrayList<Card>();
        cardsInHand = new ArrayList<Card>();
        parser = new NormalCardsParser();
    }

    public List<CardDisplay> getCardsInDeck() {
        return parser.getCardsDisplay(cardsInDeck);
    }

    public List<CardDisplay> getCardsInHand() {
        return parser.getCardsDisplay(cardsInHand);
    }

    public List<CardDisplay> getCardsOnBoard() {
        return parser.getCardsDisplay(cardsOnBoard);
    }

    public int getBoardPoints(){
        int result = 0;
        for(int i = 0 ; i < cardsOnBoard.size() ; ++i){
            result += cardsOnBoard.get(i).getPoints();
        }
        return result;
    }

    public void parseCards(List<CardDisplay> cardsDisplay) {
        parser.addCardsToParse(cardsDisplay);
        cardsInDeck = parser.getParsedCards();

    }

    public void playCard(CardDisplay cardToPlayDisplay) {
        Card cardToPlay = cardsInHand.stream().filter(c -> c.getDisplay().equals(cardToPlayDisplay)).findFirst().orElse(null);

        cardsInHand.removeIf(c -> c.getDisplay().equals(cardToPlay.getDisplay()));
        cardsOnBoard.add(cardToPlay);
    }

    public void dealCards() {
        Card toDeal = cardsInDeck.get(0);

        cardsInDeck.remove(0);
        cardsInHand.add(toDeal);
    }
}
