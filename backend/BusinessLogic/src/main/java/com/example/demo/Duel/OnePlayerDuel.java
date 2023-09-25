package com.example.demo.Duel;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.Card;
import com.example.demo.Cards.CardsFactory;
import com.example.demo.Consts;
import com.example.demo.Duel.Rows.Row;
import com.example.demo.Duel.Rows.RowStatus;
import com.example.demo.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class OnePlayerDuel {

    private List<Card> deck;
    private List<Card> hand;
    private List<Card> graveyard;
    private List<Row> rows;
    private boolean isRoundOverForPlayer;
    private int wonRounds;

    private String fraction;


    private Map<Integer, Integer> cardsTimers;

    public OnePlayerDuel(String fraction) {
        rows = new ArrayList<>();
        rows.add(new Row());
        rows.add(new Row());
        rows.add(new Row());
        graveyard = new ArrayList<>();
        cardsTimers = new HashMap<>();

        deck = new ArrayList<Card>();
        hand = new ArrayList<Card>();
        isRoundOverForPlayer = false;
        wonRounds = 0;
        this.fraction = fraction;
    }

    public void setFraction(String fraction){this.fraction = fraction;}

    public List<CardDisplay> getDeck() {
        return CardsFactory.getCardsDisplay(deck);
    }

    public List<CardDisplay> getHand() {
        return CardsFactory.getCardsDisplay(hand);
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

    public void insertCardIntoDeck(CardDisplay card, int inPlace) {
        Card cardToInsert = Card.createCard(card);
        cardToInsert.resetCard();
        deck.add(inPlace, cardToInsert);
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
        deck = CardsFactory.getCardsFromDisplays(cardsDisplay);
    }

    public void placeCardOnBoard(PlayerPlay playMade){
        List<List<Card>> possiblePlayedCardPlaces = List.of(hand, deck, graveyard);
        Card cc = Card.emptyCard();
        for (List<Card> possiblePlace : possiblePlayedCardPlaces) {
            cc = possiblePlace.stream()
                    .filter(c -> c.getId() == playMade.getPlayedCard().getId())
                    .findFirst().orElse(Card.emptyCard());

            if(possiblePlace.remove(cc))
                break;
        }
        if(fraction.equals(Consts.Fraction.humans) ) {
            cc.boostPointsBy(Consts.Fraction.humansGoldBoost);
        }
        rows.get(playMade.playedOnRow()).play(cc);

        int timer = CardsFactory.getCardTimer(cc.getDisplay());
        if(timer != CardsFactory.noTimer) {
            cardsTimers.put(cc.getDisplay().getId(), timer);
        }
    }

    public void increaseBasePower(CardDisplay card, int increaseAmount) {
        Card target = findCard(card);
        target.increaseBasePower(increaseAmount);

    }
    public void decreaseBasePower(CardDisplay card, int decreaseAmount) {
        rows.forEach(r -> r.decreaseBasePower(card, decreaseAmount));
    }
    public void addStatusToCard(CardDisplay card, String status) {
        rows.forEach(r -> r.addStatusTo(card, status));
    }
    public void removeStatusFromCard(CardDisplay card, String status) {
        rows.forEach(r -> r.removeStatusFromCard(card, status));
    }
    private Card findCard(CardDisplay card) {
        for (int i = 0; i < rows.size(); i++) {
            for (Card cardOnRow : rows.get(i).getCards()) {
                if(cardOnRow.getId() == card.getId()) {
                    return cardOnRow;
                }
            }
        }
        return Card.emptyCard();
    }
    public void strikeCard(CardDisplay cardToStrike, int strikeAmount){
        if(cardToStrike.getPoints() <= strikeAmount) {
            addCardToGraveyard(findCard(cardToStrike));
            rows.forEach(r -> r.deleteCard(cardToStrike));
        }
        else {
            rows.forEach(r -> r.strikeCardBy(cardToStrike, strikeAmount));
        }
    }

    public void boostCard(CardDisplay cardToBoost, int boostAmount){
        rows.forEach(r -> r.boostCardBy(cardToBoost, boostAmount));
    }

    public void burnCard(CardDisplay cardToBurn) {
        addCardToGraveyard(findCard(cardToBurn));
        rows.forEach(r -> r.burnCard(cardToBurn));
    }

    public int getCardRow(CardDisplay card) {
        for (int i = 0; i < Consts.rowsNumber; i++) {
            for (Card cardOnRow : rows.get(i).getCards()) {
                if(cardOnRow.getId() == card.getId()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void dealCards(int howMany) {
        for(int i = 0; i < howMany && deck.isEmpty() == false ; ++i){
            Card toDeal = deck.get(0);
            deck.remove(0);
            hand.add(toDeal);
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
        Card randomCard = new Card(getRandomCardFromBoardWithout(new CardDisplay()));
        int randomCardRow = getCardRow(randomCard.getDisplay());

        for(int row = 0 ; row < rows.size(); ++row){
            List<Card> cardsOnRow = new ArrayList<>(rows.get(row).getCards());
            for (int i = 0; i < cardsOnRow.size(); i++) {
                Card card = cardsOnRow.get(i);
                if(!(card.equals(randomCard) && fraction.equals(Consts.Fraction.monsters))) {
                    addCardToGraveyard(card);
                }
            }
            rows.get(row).clearRow();
            rows.get(row).clearStatus();
            clearRowsStatus();
        }

        if(fraction.equals(Consts.Fraction.monsters)) {
            rows.get(randomCardRow).play(randomCard);
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
        Card handCard = hand.stream().filter(c -> c.getId() == card.getId()).findFirst().orElse(Card.emptyCard());
        hand.remove(handCard);
    }

    public void restartTimer(CardDisplay card) {
        if(cardsTimers.containsKey(card.getId())) {
            cardsTimers.put(card.getId(), CardsFactory.getCardTimer(card));
        }
    }
    public void decrementTimer(CardDisplay card) {
        if(cardsTimers.containsKey(card.getId())) {
            cardsTimers.put(card.getId(), cardsTimers.get(card.getId()) - 1);
        }
    }
    public int getTimer(CardDisplay card) {
        return cardsTimers.getOrDefault(card.getId(), -1);
    }

    public CardDisplay getRandomCardFromBoardWithout(CardDisplay card) {
        List<CardDisplay> cardsOnBoard = getCardsOnBoard();
        if(cardsOnBoard.size() == 1 && cardsOnBoard.get(0).equals(card) ) {
            return new CardDisplay();
        }
        else if(cardsOnBoard.size() == 0) {
            return new CardDisplay();
        }

        int lastCardIndex = cardsOnBoard.size() - 1;
        int randomCard = Utils.getRandomNumber(0, lastCardIndex);

        CardDisplay cardRolled = cardsOnBoard.get(randomCard);
        while(cardRolled.equals(card) == true) {
            randomCard = Utils.getRandomNumber(0, lastCardIndex);
            cardRolled = cardsOnBoard.get(randomCard);
        }

        return cardRolled;

    }

    public List<CardDisplay> getGraveyard() {
        return graveyard.stream().map(c -> c.getDisplay()).collect(Collectors.toList());
    }

    public void mulliganCard(CardDisplay cardToMulligan) {
        Card card = hand.stream().filter(c -> c.getId() == cardToMulligan.getId()).findFirst().orElse(Card.emptyCard());

        int cardIndex = hand.indexOf(card);

        if(deck.size() > 0) {
            hand.set(cardIndex, deck.get(0));
            deck.set(0, card);
        }
    }

}
