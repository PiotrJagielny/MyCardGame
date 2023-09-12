package com.example.demo.Cards;


import java.util.ArrayList;
import java.util.List;

public class Card {
    private final static int idStart = 0;
    private static int uniqueCardId = idStart;


    private String name;

    private int basePoints;

    private int boost;
    private int damage;
    private int id;
    private String color;
    private List<String> statuses;
    private String fraction;

    public Card() {
        name="";
        basePoints = 0;
        boost = 0;
        damage = 0;
        id = idStart - 1 ;
        this.color = "";
        statuses = new ArrayList<>();
        fraction = "";
    }

    public Card(String name, int basePoints, String color, String fraction){
        this.color = color;
        this.name = name;
        this.basePoints = basePoints;
        id = uniqueCardId++;
        boost = 0;
        damage = 0;
        statuses = new ArrayList<>();
        this.fraction = fraction;
    }

    public CardDisplay getDisplay(){return new CardDisplay(name, getPoints(),basePoints, CardsFactory.getCardInfo(name), id, color, statuses, fraction);}
    public int getPoints(){return basePoints + boost - damage;}
    public int getId() {return id;}
    public void boostPointsBy(int amount){
        boost += amount;
    }
    public void strikeBy(int amount) {
        damage += amount;
    }
    public void decreaseBasePower(int amount) {
        basePoints -= amount;
    }
    public void addStatus(String status) {
        statuses.add(status);
    }
    public void removeStatus(String status) {
        statuses.remove(status);
    }


    public static Card createCard(CardDisplay c){
        return new Card(c.getName(), c.getBasePoints(), c.getColor(), c.getFraction());
    }
    public static Card emptyCard(){
        return new Card();
    }

    @Override
    public boolean equals(Object obj) {
        Card card = (Card)obj;
        return card.id == id;
    }

    public void resetCard() {
        damage = 0;
        boost = 0;
    }

    public void increaseBasePower(int increaseAmount) {
        basePoints += increaseAmount;
    }

}

