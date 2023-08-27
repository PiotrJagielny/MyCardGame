package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;


public class Card {
    private final static int idStart = 1;
    private static int uniqueCardId = idStart;


    private String name;

    private int basePoints;

    private int boost;
    private int damage;
    private int id;

    public Card() {
        name="";
        basePoints = 0;
        boost = 0;
        damage = 0;
        id = idStart - 1 ;
    }

    public Card(String name, int points){
        this.name = name;
        this.basePoints = points;
        id = uniqueCardId++;
        boost = 0;
        damage = 0;
    }

    public CardDisplay getDisplay(){return new CardDisplay(name, getPoints(),basePoints, CardsFactory.getCardInfo(name), id);}
    public int getPoints(){return basePoints + boost - damage;}
    public int getId() {return id;}
    public void boostPointsBy(int amount){
        boost += amount;
    }
    public void strikeBy(int amount) {
        damage += amount;
    }


    public static Card createCard(CardDisplay cardDisplay){
        return new Card(cardDisplay.getName(), cardDisplay.getPoints());
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
}

