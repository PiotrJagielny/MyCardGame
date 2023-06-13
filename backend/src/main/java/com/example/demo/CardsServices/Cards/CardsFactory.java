package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.CardTargetStrattegies.AllEnemyCardsTargetable;
import com.example.demo.CardsServices.CardTargetStrattegies.AllPlayerCardsTargetable;
import com.example.demo.CardsServices.CardTargetStrattegies.CardTargeting;
import com.example.demo.CardsServices.CardTargetStrattegies.NoCardTargetable;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.Consts;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardsFactory {

    public static final CardDisplay witch = new CardDisplay("Witch", 2);
    public static final CardDisplay knight = new CardDisplay("Knight", 3);
    public static final CardDisplay thunder = new CardDisplay("Thunder", 4);
    public static final CardDisplay warrior = new CardDisplay("Warrior",5);
    public static final CardDisplay viking = new CardDisplay("Viking",6);
    public static final CardDisplay capitan = new CardDisplay("Capitan",7);
    public static final CardDisplay armageddon =new CardDisplay("Armageddon",8);
    public static final CardDisplay conflagration =new CardDisplay("Conflagration",0);
    public static final CardDisplay doubler =new CardDisplay("Doubler",2);

    public static final CardDisplay booster = new CardDisplay("Booster", 1);
    public static final int boosterBoostAmount = 3;

    public static final CardDisplay leader = new CardDisplay("Leader", 1);
    public static final int leaderBoostAmount = 2;

    public static final CardDisplay healer = new CardDisplay("Healer", 2);
    public static final int healerBoostAmount = 2;
    public static final int healerMaxCardPointsWithBoost = 2;

    public static final CardDisplay paper = new CardDisplay("Paper", 1);
    public static final CardDisplay minion = new CardDisplay("Minion", 2);
    public static final CardDisplay archer = new CardDisplay("Archer", 1);
    public static final int archerStrikeAmount = 3;

    public static final CardDisplay fireball = new CardDisplay("Fireball", 0);
    public static final int fireballStrikeAmount = 5;

    public static final CardDisplay rip = new CardDisplay("Rip", 0);
    public static final int ripRowDamageAmount = 2;

    public static List<Card> createAllCards(){
        List<Card> result = new ArrayList<Card>(Arrays.asList(
                Card.createCard(knight),
                Card.createCard(witch),
                Card.createCard(thunder),
                Card.createCard(warrior),
                Card.createCard(viking),
                Card.createCard(capitan),
                Card.createCard(armageddon),
                Card.createCard(minion),
                Card.createCard(paper),
                Card.createCard(booster),
                Card.createCard(leader),
                Card.createCard(healer),
                Card.createCard(archer),
                Card.createCard(fireball),
                Card.createCard(conflagration),
                Card.createCard(doubler),
                Card.createCard(rip)
        ));
        return result;
    }


    public static String getCardInfo(String cardName){

        if(cardName.equals(archer.getName())){
            return "Strike enemy by " + archerStrikeAmount;
        }
        else if(cardName.equals(leader.getName())){
            return "Boost whole row by " + leaderBoostAmount;
        }
        else if(cardName.equals(booster.getName())){
            return "Boost your card by " + boosterBoostAmount;
        }
        else if(cardName.equals(healer.getName())){
            return "Boost every card on your row by " + healerBoostAmount + ", if card points is max " + healerMaxCardPointsWithBoost;
        }
        else if(cardName.equals(fireball.getName())) {
            return "Strike enemy by " + fireballStrikeAmount;
        }
        else if(cardName.equals(conflagration.getName())) {
            return "Burn all cards that are max points";
        }
        else if(cardName.equals(doubler.getName())) {
            return "Double chosen card points";
        }
        else if(cardName.equals(rip.getName())) {
            return "Deal " + CardsFactory.ripRowDamageAmount + " damage to whole enemy row";
        }
        else
            return "";
    }

    public static CardTargeting getTargetingStrategy(CardDisplay card){
        if(card.equals(CardsFactory.booster)){
            return new AllPlayerCardsTargetable();
        }
        else if(card.equals(CardsFactory.archer)){
            return new AllEnemyCardsTargetable();
        }
        else if(card.equals(CardsFactory.fireball)){
            return new AllEnemyCardsTargetable();
        }
        else if(card.equals(CardsFactory.doubler)) {
            return new AllPlayerCardsTargetable();
        }
        else {
            return new NoCardTargetable();
        }
    }

    public static List<Integer> getPossibleDestinationRows(CardDisplay card) {
        if(card.equals(fireball))
            return List.of();
        else if(card.equals(conflagration))
            return List.of();
        else
            return getAllRows();
    }

    public static String encodeCardDisplayToString(CardDisplay card) {
        return card.getName() + " : " + card.getPoints();
    }

    public static CardDisplay decodeStringToCardDisplay(String encodedDisplay) {
        String[] decodedDisplay = encodedDisplay.split(" ");
        String cardName = decodedDisplay[0];
        int cardPoints = 0;
        try {
            cardPoints = Integer.parseInt(decodedDisplay[2]);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return new CardDisplay(cardName, cardPoints);
    }

    private static List<Integer> getAllRows() {
        return List.of(Consts.firstRow, Consts.secondRow, Consts.thirdRow);
    }
}
