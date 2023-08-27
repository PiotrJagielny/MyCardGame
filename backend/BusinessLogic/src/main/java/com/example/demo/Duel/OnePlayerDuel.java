package com.example.demo.Duel;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.Card;
import com.example.demo.Cards.CardsFactory;
import com.example.demo.Consts;
import com.example.demo.Duel.Rows.Row;
import com.example.demo.Duel.Rows.RowStatus;
import com.example.demo.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnePlayerDuel {

    private List<Card> cardsInDeck;
    private List<Card> cardsInHand;
    private List<Card> graveyard;
    private List<Row> rows;
    private boolean isRoundOverForPlayer;
    private int wonRounds;


    private Map<String, Integer> cardsTimers;

    public OnePlayerDuel() {
        rows = new ArrayList<>();
        rows.add(new Row());
        rows.add(new Row());
        rows.add(new Row());
        graveyard = new ArrayList<>();
        cardsTimers = new HashMap<>();

        cardsInDeck = new ArrayList<Card>();
        cardsInHand = new ArrayList<Card>();
        isRoundOverForPlayer = false;
        wonRounds = 0;
    }

    public List<CardDisplay> getCardsInDeck() {
        return CardsFactory.getCardsDisplay(cardsInDeck);
    }

    public List<CardDisplay> getCardsInHand() {
        return CardsFactory.getCardsDisplay(cardsInHand);
    }

    public List<CardDisplay> getRow(int number) {
        List<CardDisplay> rowCards = CardsFactory.getCardsDisplay(rows.get(number).getCards());
        for (int i = 0; i < rowCards.size(); i++) {
            CardDisplay card = rowCards.get(i);
            rowCards.get(i).setTimer(getTimer(card));
        }
        return rowCards;
    }

    public void addCardToGraveyard(Card card) {
        if(!card.equals(Card.emptyCard())) {
            card.resetCard();
            graveyard.add(card);
        }
    }

    public List<CardDisplay> getCardsOnBoard(){
        List<CardDisplay> wholeBoard = new ArrayList<>();
        for (int i = 0; i < Consts.rowsNumber; i++) {
            wholeBoard.addAll(CardsFactory.getCardsDisplay(rows.get(i).getCards()) );
        }
        return wholeBoard;
    }

    public int getBoardPoints(){
        return rows.stream().mapToInt(r -> r.getRowPoints()).sum();
    }
    public int getRowPoints(int row) {
        return rows.get(row).getRowPoints();
    }

    public void parseCards(List<CardDisplay> cardsDisplay) {
        cardsInDeck = CardsFactory.getCardsFromDisplays(cardsDisplay);
    }

    public void placeCardOnBoard(PlayerPlay playMade){
        List<List<Card>> possiblePlayedCardPlaces = List.of(cardsInHand, cardsInDeck, graveyard);
        Card cc = Card.emptyCard();
        for (List<Card> possiblePlace : possiblePlayedCardPlaces) {
            cc = possiblePlace.stream()
                    .filter(c -> c.getId() == playMade.getPlayedCard().getId())
                    .findFirst().orElse(Card.emptyCard());

            if(possiblePlace.remove(cc))
                break;
        }
        rows.get(playMade.playedOnRow()).play(cc);

        int timer = CardsFactory.getCardTimer(cc.getDisplay());
        if(timer != CardsFactory.noTimer) {
            cardsTimers.put(cc.getDisplay().getName(), timer);
        }
    }

    public void strikeCard(CardDisplay cardToStrike, int strikeAmount){
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            Card card = row.getCards().stream().filter(c -> c.getId() == cardToStrike.getId())
                            .findFirst().orElse(Card.emptyCard());

            if(card.getPoints() <= strikeAmount ) {
                addCardToGraveyard(card);
                row.deleteCard(card);
            }
            else {
                row.strikeCardBy(card, strikeAmount);
            }
        }
    }

    public void boostCard(CardDisplay cardToBoost, int boostAmount){
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            Card card = row.getCards().stream().filter(c -> c.getId() == cardToBoost.getId())
                    .findFirst().orElse(Card.emptyCard());
            row.boostCardBy(card, boostAmount);
        }
    }

    public void burnCard(CardDisplay cardToBurn) {
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            Card card = row.getCards().stream()
                    .filter(c -> c.getId() == cardToBurn.getId())
                    .findFirst().orElse(Card.emptyCard());

            row.burnCard(card);
            addCardToGraveyard(card);
        }
    }

    public int getCardRow(CardDisplay card) {
        for (int i = 0; i < Consts.rowsNumber; i++) {

            if(CardsFactory.getCardsDisplay(rows.get(i).getCards()).contains(card))
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
            graveyard.addAll(rows.get(row).getCards());
            rows.get(row).clearRow();
            clearRowsStatus();
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
    public void deleteCardFromHand(CardDisplay card) {
        Card handCard = cardsInHand.stream().filter(c -> c.getId() == card.getId()).findFirst().orElse(Card.emptyCard());
        cardsInHand.remove(handCard);
    }

    public void restartTimer(CardDisplay card) {
        if(cardsTimers.containsKey(card.getName())) {
            cardsTimers.put(card.getName(), CardsFactory.getCardTimer(card));
        }
    }
    public void decrementTimer(CardDisplay card) {
        if(cardsTimers.containsKey(card.getName())) {
            cardsTimers.put(card.getName(), cardsTimers.get(card.getName()) - 1);
        }
    }
    public int getTimer(CardDisplay card) {
        if(cardsTimers.containsKey(card.getName()) == false) return -1;

        return cardsTimers.get(card.getName());
    }

    public CardDisplay getRandomCardFromBoardWithout(CardDisplay card) {
        int lastCardIndex = getCardsOnBoard().size() - 1;
        int randomCard = Utils.getRandomNumber(0, lastCardIndex);

        CardDisplay cardRolled = getCardsOnBoard().get(randomCard);
        while(cardRolled.equals(card) == true) {
            randomCard = Utils.getRandomNumber(0, lastCardIndex);
            cardRolled = getCardsOnBoard().get(randomCard);
        }

        return cardRolled;


    }

    public List<CardDisplay> getGraveyard() {
        return CardsFactory.getCardsDisplay(graveyard);
    }

    public void mulliganCard(CardDisplay cardToMulligan) {
        Card card = cardsInHand.stream().filter(c -> c.getId() == cardToMulligan.getId()).findFirst().orElse(Card.emptyCard());

        int cardIndex = cardsInHand.indexOf(card);

        if(cardsInDeck.size() > 0) {
            cardsInHand.set(cardIndex,cardsInDeck.get(0));
            cardsInDeck.set(0, card);
        }
    }
}