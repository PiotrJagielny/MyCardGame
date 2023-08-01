package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.CardsEffects.RowStatus;
import com.example.demo.CardsServices.CardsParser;
import com.example.demo.Consts;

import java.util.ArrayList;
import java.util.List;

public class OnePlayerDuel {

    private List<Card> cardsInDeck;
    private List<Card> cardsInHand;
    private List<Row> rows;
    private boolean isRoundOverForPlayer;
    private int wonRounds;



    public OnePlayerDuel() {
        rows = new ArrayList<>();
        rows.add(new Row());
        rows.add(new Row());
        rows.add(new Row());

        cardsInDeck = new ArrayList<Card>();
        cardsInHand = new ArrayList<Card>();
        isRoundOverForPlayer = false;
        wonRounds = 0;
    }

    public List<CardDisplay> getCardsInDeck() {
        return CardsParser.getCardsDisplay(cardsInDeck);
    }

    public List<CardDisplay> getCardsInHand() {
        return CardsParser.getCardsDisplay(cardsInHand);
    }

    public List<CardDisplay> getCardsOnBoardOnRow(int number) {

        return CardsParser.getCardsDisplay(rows.get(number).getCards());
    }

    public List<CardDisplay> getCardsOnBoard(){
        List<CardDisplay> wholeBoard = new ArrayList<>();
        for (int i = 0; i < Consts.rowsNumber; i++) {
            wholeBoard.addAll(CardsParser.getCardsDisplay(rows.get(i).getCards()) );
        }
        return wholeBoard;
    }

    public int getBoardPoints(){
        int result = 0;
        for(int i = 0 ; i < Consts.rowsNumber ; ++i){
            result += rows.get(i).getRowPoints();
        }
        return result;
    }
    public int getRowPoints(int row) {
        return rows.get(row).getRowPoints();
    }

    public void parseCards(List<CardDisplay> cardsDisplay) {
        cardsInDeck = CardsParser.getCardsFromDisplays(cardsDisplay);
    }

    public void placeCardOnBoard(PlayerPlay playMade){
        Card playedCard = cardsInHand.stream().filter(c -> c.getDisplay().equals(playMade.getPlayedCard())).findFirst().orElse(null);
        if(playedCard == null) {
            playedCard = cardsInDeck.stream().filter(c -> c.getDisplay().equals(playMade.getPlayedCard())).findFirst().orElse(null);
            final CardDisplay display = playedCard.getDisplay();
            cardsInDeck.removeIf(c -> c.getDisplay().equals(display));
        }
        final CardDisplay display = playedCard.getDisplay();
        cardsInHand.removeIf(c -> c.getDisplay().equals(display));

        if(playMade.getPlayedCard().getPoints() != 0)
            rows.get(playMade.getPlayedCardRowNum()).play(playedCard);
    }

    public void strikeCard(CardDisplay cardToStrike, int strikeAmount){
        rows.forEach(row -> {
            row.strikeCardBy(row.getCards().stream()
                            .filter(c -> c.getDisplay().equals(cardToStrike))
                            .findFirst().orElse(Card.createEmptyCard())    ,strikeAmount
            );
        });
    }

    public void boostCard(CardDisplay cardToBoost, int boostAmount){
        rows.forEach(row -> {
            row.boostCardBy( row.getCards().stream()
                            .filter(c -> c.getDisplay().equals(cardToBoost))
                            .findFirst().orElse(Card.createEmptyCard())    ,boostAmount
            );
        });
    }

    public void burnCard(CardDisplay card) {
        rows.forEach(row -> {
            row.burnCard(
                    row.getCards().stream()
                            .filter(c -> c.getDisplay().equals(card))
                            .findFirst().orElse(Card.createEmptyCard())
            );
        });
    }

    public int getCardRow(CardDisplay card) {
        for (int i = 0; i < Consts.rowsNumber; i++) {
            if(rows.get(i).getCards().contains(Card.createCard(card)))
                return i;
        }
        return -1;
    }

    public void dealCards(int howMany) {
        for(int i = 0 ; i < howMany && cardsInDeck.isEmpty() == false ; ++i){
            Card toDeal = cardsInDeck.get(0);
            cardsInDeck.remove(0);
            cardsInHand.add(toDeal);
        }
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
        dealCards(1);
        clearRows();

        if (playerBoardPoints >= opponentBoardPoints) ++wonRounds;
    }

    private void clearRows(){
        for(int row = 0 ; row < Consts.rowsNumber ; ++row){
            rows.get(row).clearRow();
        }
    }

    public int getWonRounds(){
        return wonRounds;
    }
    public boolean didWon(){return wonRounds == 2;}

    public void setRowStatus(RowStatus status, int rowNumber) {
        rows.get(rowNumber).setStatus(status);
    }


    public String getRowStatusName(int row) {
        return rows.get(row).getStatusName();
    }
    public void clearRowsStatus() {
        rows.forEach(row -> row.clearStatus());
    }

    public void spawnCard(CardDisplay card, int onRow) {
        rows.get(onRow).spawnCard(card);
    }
}
