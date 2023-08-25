package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.CardsParser;
import com.example.demo.Consts;
import com.example.demo.Utils;

import java.util.ArrayList;
import java.util.List;

public class OnePlayerDuel {

    private List<Card> cardsInDeck;
    private List<Card> cardsInHand;
    private List<Card> graveyard;
    private List<Row> rows;
    private boolean isRoundOverForPlayer;
    private int wonRounds;



    public OnePlayerDuel() {
        rows = new ArrayList<>();
        rows.add(new Row());
        rows.add(new Row());
        rows.add(new Row());
        graveyard = new ArrayList<>();

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

    public List<CardDisplay> getRow(int number) {
        List<CardDisplay> rowCards =CardsParser.getCardsDisplay(rows.get(number).getCards());
        for (int i = 0; i < rowCards.size(); i++) {
            CardDisplay card = rowCards.get(i);
            rowCards.get(i).setTimer(rows.get(number).getTimer(card));
        }
        return rowCards;
    }

    public void addCardToGraveyard(Card card) {
        if(!card.equals(Card.emptyCard())) {
            graveyard.add(card);
        }
    }

    public List<CardDisplay> getCardsOnBoard(){
        List<CardDisplay> wholeBoard = new ArrayList<>();
        for (int i = 0; i < Consts.rowsNumber; i++) {
            wholeBoard.addAll(CardsParser.getCardsDisplay(rows.get(i).getCards()) );
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
        cardsInDeck = CardsParser.getCardsFromDisplays(cardsDisplay);
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
    }

    public void strikeCard(CardDisplay cardToStrike, int strikeAmount){
        for (int i = 0; i < rows.size(); i++) {
            Row row = rows.get(i);
            Card card = row.getCards().stream().filter(c -> c.getId() == cardToStrike.getId())
                            .findFirst().orElse(Card.emptyCard());
            row.strikeCardBy(card, strikeAmount);
            if(card.getPoints() <= strikeAmount ) {
                addCardToGraveyard(card);
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

            if(CardsParser.getCardsDisplay(rows.get(i).getCards()).contains(card))
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

    public int decrementAndGetTimer(CardDisplay card) {
        int cardRow = getCardRow(card);
        if(cardRow == -1)
            return -1;
        return rows.get(cardRow).decrementAndGetTimer(card);
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
        return CardsParser.getCardsDisplay(graveyard);
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
