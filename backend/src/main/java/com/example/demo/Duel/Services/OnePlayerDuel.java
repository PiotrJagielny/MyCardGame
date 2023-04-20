package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Parser.CardsParser;
import com.example.demo.CardsServices.Parser.NormalCardsParser;
import com.example.demo.Consts;

import java.util.ArrayList;
import java.util.List;

public class OnePlayerDuel {

    private List<Card> cardsInDeck;
    private List<Card> cardsInHand;
    private List<Row> rows;
    private boolean isRoundOverForPlayer;
    private int wonRounds;

    private CardsParser parser;

    public OnePlayerDuel() {
        rows = new ArrayList<>();
        rows.add(new Row());
        rows.add(new Row());
        rows.add(new Row());

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

    public List<CardDisplay> getCardsOnBoardOnRow(int number) {

        return parser.getCardsDisplay(rows.get(number).getCards());
    }

    public int getBoardPoints(){
        int result = 0;
        for(int i = 0 ; i < Consts.rowsNumber ; ++i){
            result += rows.get(i).getRowPoints();
        }
        return result;
    }

    public void parseCards(List<CardDisplay> cardsDisplay) {
        parser.addCardsToParse(cardsDisplay);
        cardsInDeck = parser.getParsedCards();

    }

    public void playCard(CardDisplay cardToPlayDisplay, int rowNumber) {
        Card cardToPlay = cardsInHand.stream().filter(c -> c.getDisplay().equals(cardToPlayDisplay)).findFirst().orElse(null);

        cardsInHand.removeIf(c -> c.getDisplay().equals(cardToPlay.getDisplay()));
        rows.get(rowNumber).play(cardToPlay);
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
        rows.get(Consts.firstRow).clearBoard();

        if (playerBoardPoints > opponentBoardPoints) ++wonRounds;
    }

    public int getWonRounds(){
        return wonRounds;
    }
    public boolean didWon(){return wonRounds == 2;}
}
