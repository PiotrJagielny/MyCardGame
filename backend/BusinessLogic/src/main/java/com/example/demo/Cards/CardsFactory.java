package com.example.demo.Cards;

import com.example.demo.Cards.CardTargetStrattegies.*;
import com.example.demo.Consts;
import com.example.demo.Duel.OnePlayerDuel;

import java.util.*;

public class CardsFactory {

    public static final CardDisplay hotdog = new CardDisplay("Hot dog", 2, Consts.bronze);
    public static final CardDisplay knight = new CardDisplay("Knight", 3, Consts.bronze);
    public static final CardDisplay thunder = new CardDisplay("Thunder", 4, Consts.bronze);
    public static final CardDisplay warrior = new CardDisplay("Warrior",5, Consts.bronze);
    public static final CardDisplay viking = new CardDisplay("Viking",6, Consts.silver);
    public static final CardDisplay capitan = new CardDisplay("Capitan",7, Consts.silver);
    public static final CardDisplay armageddon =new CardDisplay("Armageddon",8, Consts.silver);
    public static final CardDisplay conflagration =new CardDisplay("Conflagration",0, Consts.silver);
    public static final CardDisplay doubler =new CardDisplay("Doubler",2, Consts.silver);

    public static final CardDisplay booster = new CardDisplay("Booster", 1, Consts.bronze);
    public static final int boosterBoost = 3;

    public static final CardDisplay leader = new CardDisplay("Leader", 1, Consts.bronze);
    public static final int leaderBoost = 2;

    public static final CardDisplay healer = new CardDisplay("Healer", 2, Consts.silver);
    public static final int healerBoost = 2;
    public static final int healerMaxCardPointsWithBoost = 2;

    public static final CardDisplay paper = new CardDisplay("Paper", 1, Consts.bronze);
    public static final CardDisplay minion = new CardDisplay("Minion", 2, Consts.bronze);
    public static final CardDisplay archer = new CardDisplay("Archer", 1, Consts.bronze);
    public static final int archerDamage = 3;

    public static final CardDisplay fireball = new CardDisplay("Fireball", 0, Consts.bronze);
    public static final int fireballDamage = 5;

    public static final CardDisplay rip = new CardDisplay("Rip", 0, Consts.bronze);
    public static final int ripDamage = 2;


    public static final CardDisplay longer= new CardDisplay("Longer",1, Consts.bronze);
    public static final int longerTimer = 1;
    public static final int longerBoost = 2;

    public static final CardDisplay rain= new CardDisplay("Rain",0, Consts.bronze);

    public static final int rainDamage =  2;
    public static final CardDisplay clearSky= new CardDisplay("Clear sky",0, Consts.bronze);
    public static final CardDisplay sharpshooter= new CardDisplay("Sharpshooter", 4, Consts.gold);
    public static final CardDisplay cow= new CardDisplay("Cow", 1, Consts.silver);
    public static final int sharpshooterDamage = 2;
    public static final int sharpshooterSelfBoost= 2;
    public static final CardDisplay chort= new CardDisplay("Chort", 6, Consts.silver);

    public static final CardDisplay priest = new CardDisplay("Priest", 1, Consts.gold);
    public static final CardDisplay witch= new CardDisplay("Witch", 1, Consts.bronze);
    public static final CardDisplay trebuchet = new CardDisplay("Trebuchet", 2, Consts.bronze);
    public static final int trebuchetDamage = 1;
    public static final int trebuchetTimer = 2;

    public static final CardDisplay goodPerson = new CardDisplay("Good person", 2, Consts.bronze);
    public static final int goodPersonBoost= 1;
    public static final int goodPersonTimer= 2;

    public static final CardDisplay gravedigger = new CardDisplay("Gravedigger", 2, Consts.silver);
    public static final CardDisplay wildRoam= new CardDisplay("Wild roam", 3, Consts.bronze);
    public static final CardDisplay supplier = new CardDisplay("Supplier", 1, Consts.bronze);
    public static final CardDisplay breaker = new CardDisplay("Breaker", 3, Consts.bronze);
    public static final int breakerTimer = 2;

    public static final CardDisplay epidemic = new CardDisplay("Epidemic", 0, Consts.bronze);
    public static final CardDisplay ginger = new CardDisplay("Ginger", 2, Consts.silver);
    public static final int gingerDamage = 1;
    public static final CardDisplay spy = new CardDisplay("Spy", 8, Consts.silver);


    public static final CardDisplay blueFire = new CardDisplay("Blue fire", 2, Consts.gold);
    public static final int blueFireThreshold = 10;

    public static final CardDisplay axer= new CardDisplay("Axer", 4 , Consts.bronze);
    public static final CardDisplay copier = new CardDisplay("Copier", 3, Consts.bronze);
    public static final int copierCopiesCount = 2;

    public static final CardDisplay mushrooms = new CardDisplay("Mushrooms", 0, Consts.bronze);
    public static final int mushroomsBaseDamage= 3;

    public static final CardDisplay tastyMushroom = new CardDisplay("Tasty mushroom", 0, Consts.bronze);
    public static final int tastyMushroomBaseIncrease = 3;

    public static final CardDisplay giant= new CardDisplay("Giant", 9, Consts.gold);

    public static final CardDisplay handcuffs= new CardDisplay("Handcuffs", 0, Consts.silver);
    public static final int handcuffsDamage = 3;
    public static final CardDisplay key = new CardDisplay("Key", 0, Consts.silver);
    public static final int keyBoost = 4;


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
        put(axer.getName(), new EnemyCardsTargetable());
        put(copier.getName(), new PlayerCardsTargetable());
        put(mushrooms.getName(), new EnemyCardsTargetable());
        put(tastyMushroom.getName(), new PlayerCardsTargetable());
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
        put(ginger.getName(), "Deal " + gingerDamage + " damage to whole row");
        put(spy.getName(), "Place this card on opposite enemy row and draw 1 card");
        put(blueFire.getName(), "If opposite row has at least " + blueFireThreshold + " points burn all max points cards on this row");
        put(axer.getName(), "Deal damage to enemy card by number of weakened cards on enemy board");
        put(copier.getName(), "Choose card on your board and insert " + copierCopiesCount + " copier into your deck");
        put(mushrooms.getName(), "Decrease base power of enemy card by " + mushroomsBaseDamage);
        put(tastyMushroom.getName(), "Increase base power of enemy card by " + tastyMushroomBaseIncrease);
        put(handcuffs.getName(), "Lock enemy card and deal " + handcuffsDamage + " damage");
        put(key.getName(), "Unlock your card and boost it by " + keyBoost);
    }};
    public static String getCardInfo(String cardName){
        return mapCardNameToInfo.getOrDefault(cardName, "");
    }

    private static final Map<String, List<Integer>> mapCardNameToRowsAffect = new HashMap<>() {{
        put(rip.getName(), List.of(Consts.firstRow, Consts.secondRow, Consts.thirdRow));
        put(rain.getName(), List.of(Consts.firstRow, Consts.secondRow, Consts.thirdRow));
        put(ginger.getName(), List.of(Consts.firstRow, Consts.secondRow, Consts.thirdRow));
    }};

    public static List<Integer> getCardRowsToAffect(String cardName) {
    return mapCardNameToRowsAffect.getOrDefault(cardName, List.of());
    }

    public static final List<CardDisplay> cardsWithPlayChainPossibility = List.of(priest, witch, supplier);
    public static final List<CardDisplay> cardsThatArePlacedOnEnemyBoard= List.of(spy);
    public static final List<CardDisplay> cardsWithTurnEndTimer = List.of(trebuchet, goodPerson, longer);
    public static final List<CardDisplay> cardsWithTurnStartTimer = List.of(breaker);


    public static List<Card> createAllCards(){
        return new ArrayList<Card>(Arrays.asList(
                Card.createCard(breaker),
                Card.createCard(mushrooms),
                Card.createCard(copier),
                Card.createCard(tastyMushroom),
                Card.createCard(ginger),
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
    public static List<Card> getCardsFromDisplays(List<CardDisplay> cardsToParse) {
        List<Card> result = new ArrayList<Card>();

        for (int i = 0; i < cardsToParse.size(); ++i) {
            Card createdCard = Card.createCard(cardsToParse.get(i));
            result.add(createdCard);
        }
        return result;
    }

    public static List<CardDisplay> getCardsDisplay(List<Card> cards) {
        List<CardDisplay> result = new ArrayList<CardDisplay>();
        for(int i = 0 ; i < cards.size() ; ++i){
            result.add(cards.get(i).getDisplay());
        }
        return result;

    }
}
