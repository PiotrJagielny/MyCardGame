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
    private List<Card> cardsOnCemetery;
    private boolean isRoundOverForPlayer;
    private int wonRounds;

    private CardsParser parser;

    public OnePlayerDuel() {
        cardsOnBoard = new ArrayList<Card>();
        cardsOnCemetery = new ArrayList<Card>();
        cardsInDeck = new ArrayList<Card>();
        cardsInHand = new ArrayList<Card>();
        parser = new NormalCardsParser();
        isRoundOverForPlayer = false;
        wonRounds = 0;
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
    public List<CardDisplay> getCardsOnCemetery(){return parser.getCardsDisplay(cardsOnCemetery);}

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
        if(cardsInDeck.isEmpty()) return;

        Card toDeal = cardsInDeck.get(0);
        cardsInDeck.remove(0);
        cardsInHand.add(toDeal);
    }

    public void endRound(){
        isRoundOverForPlayer = true;
    }

    public boolean didEndRound(){
        return isRoundOverForPlayer;
    }

    public void startNewRound(int opponentBoardPoints) {
        int playerBoardPoints = getBoardPoints();
        isRoundOverForPlayer = false;
        dealCards();
        moveCardsFromBoardToCemetery();

        if (playerBoardPoints > opponentBoardPoints) ++wonRounds;
    }

    private void moveCardsFromBoardToCemetery(){
        for(int i = 0 ; i < cardsOnBoard.size() ; ++i){
            cardsOnCemetery.add(new Card(cardsOnBoard.get(i)));
        }
        cardsOnBoard.clear();
    }

    public int getWonRounds(){
        return wonRounds;
    }
    public boolean didWon(){return wonRounds == 2;}
}
