package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.CardTargetStrattegies.*;
import com.example.demo.Consts;
import com.example.demo.Duel.OnePlayerDuel;


import java.util.*;

public class CardsFactory {

    public static final CardDisplay hotdog = new CardDisplay("Hot dog", 2);
    public static final CardDisplay knight = new CardDisplay("Knight", 3);
    public static final CardDisplay thunder = new CardDisplay("Thunder", 4);
    public static final CardDisplay warrior = new CardDisplay("Warrior",5);
    public static final CardDisplay viking = new CardDisplay("Viking",6);
    public static final CardDisplay capitan = new CardDisplay("Capitan",7);
    public static final CardDisplay armageddon =new CardDisplay("Armageddon",8);
    public static final CardDisplay conflagration =new CardDisplay("Conflagration",0);
    public static final CardDisplay doubler =new CardDisplay("Doubler",2);

    public static final CardDisplay booster = new CardDisplay("Booster", 1);
    public static final int boosterBoost = 3;

    public static final CardDisplay leader = new CardDisplay("Leader", 1);
    public static final int leaderBoost = 2;

    public static final CardDisplay healer = new CardDisplay("Healer", 2);
    public static final int healerBoost = 2;
    public static final int healerMaxCardPointsWithBoost = 2;

    public static final CardDisplay paper = new CardDisplay("Paper", 1);
    public static final CardDisplay minion = new CardDisplay("Minion", 2);
    public static final CardDisplay archer = new CardDisplay("Archer", 1);
    public static final int archerDamage = 3;

    public static final CardDisplay fireball = new CardDisplay("Fireball", 0);
    public static final int fireballDamage = 5;

    public static final CardDisplay rip = new CardDisplay("Rip", 0);
    public static final int ripDamage = 2;


    public static final CardDisplay longer= new CardDisplay("Longer",1);
    public static final int longerTimer = 1;
    public static final int longerBoost = 2;

    public static final CardDisplay rain= new CardDisplay("Rain",0);
    public static final int rainDamage =  2;
    public static final CardDisplay clearSky= new CardDisplay("Clear sky",0);
    public static final CardDisplay sharpshooter= new CardDisplay("Sharpshooter", 4);
    public static final CardDisplay cow= new CardDisplay("Cow", 1);
    public static final int sharpshooterDamage = 2;
    public static final int sharpshooterSelfBoost= 2;
    public static final CardDisplay chort= new CardDisplay("Chort", 6);

    public static final CardDisplay priest = new CardDisplay("Priest", 1);
    public static final CardDisplay witch= new CardDisplay("Witch", 1);
    public static final CardDisplay trebuchet = new CardDisplay("Trebuchet", 2);
    public static final int trebuchetDamage = 1;
    public static final int trebuchetTimer = 2;

    public static final CardDisplay goodPerson = new CardDisplay("Good person", 2);
    public static final int goodPersonBoost= 1;
    public static final int goodPersonTimer= 2;

    public static final CardDisplay gravedigger = new CardDisplay("Gravedigger", 2);
    public static final CardDisplay wildRoam= new CardDisplay("Wild roam", 3);
    public static final CardDisplay supplier = new CardDisplay("Supplier", 1);
    public static final CardDisplay breaker = new CardDisplay("Breaker", 3);
    public static final int breakerTimer = 2;

    public static final CardDisplay epidemic = new CardDisplay("Epidemic", 0);

    private static final Map<String,Integer > mapCardNameToTimer = new HashMap<>() {{
        put(trebuchet.getName(), trebuchetTimer);
        put(goodPerson.getName(), goodPersonTimer);
        put(longer.getName(), longerTimer);
        put(breaker.getName(), breakerTimer);
    }};
    public static int getCardTimer(CardDisplay card) {
        return mapCardNameToTimer.getOrDefault(card.getName(), noTimer);
    }
    public static final int noTimer = -1;


    private static final Map<String, CardTargeting> mapCardNameToTargetingStrategy= new HashMap<>() {{
        put(doubler.getName(), new PlayerCardsTargetable());
        put(booster.getName(), new PlayerCardsTargetable());
        put(archer.getName(), new EnemyCardsTargetable());
        put(fireball.getName(), new EnemyCardsTargetable());
        put(priest.getName(), new CardsInDeckTargetable());
        put(sharpshooter.getName(), new EnemyCardsTargetable());
        put(witch.getName(), new GraveyardCardsTargetable());
        put(supplier.getName(), new PlayerCardsTargetable());
    }};

    public static List<CardDisplay> getPossibleTargetsOf(CardDisplay card, OnePlayerDuel player, OnePlayerDuel enemy) {
        return mapCardNameToTargetingStrategy.getOrDefault(
                card.getName(), new NoCardTargetable()).getPossibleTargets(player, enemy
        );
    }




    private static final Map<String, String> mapCardNameToInfo = new HashMap<>() {{
        put(conflagration.getName(), "Burn all cards that are max points");
        put(doubler.getName(), "Double chosen card points");
        put(booster.getName(), "Boost whole row by " + leaderBoost);
        put(healer.getName(), "Boost every card on your row by " + healerBoost + ", if card points is max " + healerMaxCardPointsWithBoost);
        put(leader.getName(), "Boost whole row by " + leaderBoost);
        put(archer.getName(), "Strike enemy by " + archerDamage);
        put(fireball.getName(), "Strike enemy by " + fireballDamage);
        put(rip.getName(), "Deal " + CardsFactory.ripDamage + " damage to whole enemy row");
        put(longer.getName(), "Boost every single turn by " + longerBoost);
        put(rain.getName(), "Strike max points card on choosen row every turn by " + rainDamage);
        put(clearSky.getName(), "Clear every row status");
        put(priest.getName(), "Play one card from deck");
        put(sharpshooter.getName(), "Deal " + sharpshooterDamage + " damage, if card hit dies boost this card by " + sharpshooterSelfBoost);
        put(cow.getName(), "If this cards dies, spawn " + chort.getName());
        put(trebuchet.getName(), "Deal " + trebuchetDamage + " damage every " + trebuchetTimer + " turns ");
        put(goodPerson.getName(), "Boost by " + goodPersonBoost + " your card on board every " + goodPersonTimer + " turns");
        put(gravedigger.getName(), "Boost by number of cards on your graveyard on deploy");
        put(witch.getName(), "Resurrect card from your graveyard");
        put(wildRoam.getName(), "Play all copies of this card from deck");
        put(supplier.getName(), "Play from deck copy of chosen card from board");
        put(breaker.getName(), "After " + breakerTimer + " turns call copy of this card from deck to the same row");
        put(epidemic.getName(), "Kill all cards with minimum points");
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

    public static final List<CardDisplay> cardsWithPlayChainPossibility = List.of(priest, witch, supplier);


    public static List<Card> createAllCards(){
        return new ArrayList<Card>(Arrays.asList(
                Card.createCard(breaker),
                Card.createCard(epidemic),
                Card.createCard(breaker),
                Card.createCard(knight),
                Card.createCard(knight),
                Card.createCard(hotdog),
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
                Card.createCard(cow),
                Card.createCard(trebuchet),
                Card.createCard(goodPerson),
                Card.createCard(gravedigger),
                Card.createCard(witch),
                Card.createCard(wildRoam),
                Card.createCard(wildRoam),
                Card.createCard(supplier)
        ));
    }
}
