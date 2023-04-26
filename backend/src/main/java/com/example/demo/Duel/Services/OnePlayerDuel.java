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

    public void playCard(PlayerPlay playMade) {
        Card playedCard = cardsInHand.stream().filter(c -> c.getDisplay().equals(playMade.getPlayedCard())).findFirst().orElse(null);
        Card cardAffected = rows.get(playMade.getAffectedCardRowNum()).getCards().stream().filter(c -> c.getDisplay().equals(playMade.getCardThatGotEffect())).findFirst().orElse(null);

        if(playedCard.getDisplay().getName().equals("Booster")){
            rows.get(playMade.getAffectedCardRowNum()).boostCardBy(cardAffected);
        }
        else if(playedCard.getDisplay().getName().equals("Leader")){
            rows.get(playMade.getPlayedCardRowNum()).boostRowBy(2);
        }
        else if(playedCard.getDisplay().getName().equals("WoodTheHealer")){
            for(Row row : rows){
                for(Card cardOnRow : row.getCards()){
                    if(cardOnRow.getDisplay().getPoints() < 3){
                        cardOnRow.boostPointsBy(2);
                    }
                }
            }
        }

        cardsInHand.removeIf(c -> c.getDisplay().equals(playedCard.getDisplay()));
        rows.get(playMade.getPlayedCardRowNum()).play(playedCard);
    }

    public void dealCards(String howToDeal) {
        if(howToDeal.equals(Consts.roundStartDealStrategy)){
            dealCardsOnNewRoundStart();
        }
        else if(howToDeal.equals(Consts.gameStartDealStrategy)) {
            dealCardsOnGameStart();
        }
    }

    private void dealCardsOnGameStart(){
        for(int i = 0 ; i < 4 && cardsInDeck.isEmpty() == false ; ++i){
            Card toDeal = cardsInDeck.get(0);
            cardsInDeck.remove(0);
            cardsInHand.add(toDeal);
        }
    }

    private void dealCardsOnNewRoundStart(){
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
        dealCards(Consts.roundStartDealStrategy);
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
