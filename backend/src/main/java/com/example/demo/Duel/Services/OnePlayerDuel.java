package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.CardsParser;
import com.example.demo.Consts;
import com.example.demo.Duel.DataStructures.PlayerPlay;

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

    public int getBoardPoints(){
        int result = 0;
        for(int i = 0 ; i < Consts.rowsNumber ; ++i){
            result += rows.get(i).getRowPoints();
        }
        return result;
    }

    public void parseCards(List<CardDisplay> cardsDisplay) {
        cardsInDeck = CardsParser.getCardsFromDisplays(cardsDisplay);
    }

    public void placeCardOnBoard(PlayerPlay playMade){
        Card playedCard = cardsInHand.stream().filter(c -> c.getDisplay().equals(playMade.getPlayedCard())).findFirst().orElse(null);
        cardsInHand.removeIf(c -> c.getDisplay().equals(playedCard.getDisplay()));
        rows.get(playMade.getPlayedCardRowNum()).play(playedCard);
    }

    public void strikeCard(CardDisplay cardToStrike, int strikeAmount){
        int cardRow = findCardRow(cardToStrike);
        Card cardAffected = rows.get(cardRow).getCards().stream().filter(c -> c.getDisplay().equals(cardToStrike)).findFirst().orElse(null);
        rows.get(cardRow).strikeCardBy(cardAffected);
    }

    public void boostCard(CardDisplay cardToBoost, int boostAmount){
        int cardRow = findCardRow(cardToBoost);
        Card cardAffected = rows.get(cardRow).getCards().stream().filter(c -> c.getDisplay().equals(cardToBoost)).findFirst().orElse(null);
        rows.get(cardRow).boostCardBy(cardAffected, boostAmount);
    }

    private int findCardRow(CardDisplay card){
        for(int i = 0 ; i <  Consts.rowsNumber ; ++i){
            if(rows.get(i).getCardsDisplays().contains(card))
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

}
