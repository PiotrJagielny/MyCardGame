package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.CardTargetStrattegies.*;
import com.example.demo.Consts;
import com.example.demo.Duel.OnePlayerDuel;


import java.util.*;

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


    public static final CardDisplay longer= new CardDisplay("Longer",1);
    public static final int longerBoostAmount = 2;

    public static final CardDisplay rain= new CardDisplay("Rain",0);
    public static final CardDisplay clearSky= new CardDisplay("Clear sky",0);
    public static final CardDisplay priest = new CardDisplay("Priest", 1);
    public static final CardDisplay sharpshooter= new CardDisplay("Sharpshooter", 4);
    public static final CardDisplay cow= new CardDisplay("Cow", 1);
    public static final CardDisplay chort= new CardDisplay("Chort", 6);
    public static final int sharpshooterDamage = 2;
    public static final int sharpshooterSelfBoost= 2;


    public static final int rainStrikeAmount =  2;
    private static final Map<String, CardTargeting> mapCardNameToTargetingStrategy= new HashMap<>() {{
        put(doubler.getName(), new AllPlayerCardsTargetable());
        put(booster.getName(), new AllPlayerCardsTargetable());
        put(archer.getName(), new AllEnemyCardsTargetable());
        put(fireball.getName(), new AllEnemyCardsTargetable());
        put(priest.getName(), new AllCardsInDeckTargetable());
        put(sharpshooter.getName(), new AllEnemyCardsTargetable());
    }};

    public static List<CardDisplay> getPossibleTargetsOf(CardDisplay card, OnePlayerDuel player, OnePlayerDuel enemy) {
        return mapCardNameToTargetingStrategy.getOrDefault(
                card.getName(), new NoCardTargetable()).getPossibleTargets(player, enemy
        );
    }




    private static final Map<String, String> mapCardNameToInfo = new HashMap<>() {{
        put(conflagration.getName(), "Burn all cards that are have points");
        put(doubler.getName(), "Double chosen card points");
        put(booster.getName(), "Boost whole row by " + leaderBoostAmount);
        put(healer.getName(), "Boost every card on your row by " + healerBoostAmount + ", if card points is max " + healerMaxCardPointsWithBoost);
        put(leader.getName(), "Boost whole row by " + leaderBoostAmount);
        put(archer.getName(), "Strike enemy by " + archerStrikeAmount);
        put(fireball.getName(), "Strike enemy by " + fireballStrikeAmount);
        put(rip.getName(), "Deal " + CardsFactory.ripRowDamageAmount + " damage to whole enemy row");
        put(longer.getName(), "Boost every single turn by " + longerBoostAmount);
        put(rain.getName(), "Strike max points card on choosen row every turn by " + rainStrikeAmount);
        put(clearSky.getName(), "Clear every row status");
        put(priest.getName(), "Play one card from deck");
        put(sharpshooter.getName(), "Deal " + sharpshooterDamage + " damage, if card hit dies boost this card by " + sharpshooterSelfBoost);
        put(cow.getName(), "If this cards dies, spawn " + chort.getName());
    }};
    public static String getCardInfo(String cardName){
        return mapCardNameToInfo.getOrDefault(cardName, "");

    }

    private static final Map<String, List<Integer>> mapCardNameToRowsAffect = new HashMap<>() {{
        put(rip.getName(), List.of(Consts.firstRow, Consts.secondRow, Consts.thirdRow));
        put(rain.getName(), List.of(Consts.firstRow, Consts.secondRow, Consts.thirdRow));
    }};

    public static List<Integer> getCardRowsToAffect(String cardName) {
    return mapCardNameToRowsAffect.getOrDefault(cardName, List.of());
    }


    public static List<Card> createAllCards(){
        return new ArrayList<Card>(Arrays.asList(
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
                Card.createCard(rip),
                Card.createCard(longer),
                Card.createCard(rain),
                Card.createCard(clearSky),
                Card.createCard(priest),
                Card.createCard(sharpshooter),
                Card.createCard(cow)
        ));
    }
}
