package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;

public class Card {

    private String name;

    private int points;

    public Card(String name, int points){
        this.name = name;
        this.points = points;
    }

    public CardDisplay getDisplay(){return new CardDisplay(name, points);}
    public int getPoints(){return points;}
    public void boostPointsBy(int amount){
        points += amount;
    }
    public void strikeBy(int amount) {
        points -= amount;
    }


    public static Card createCard(CardDisplay cardDisplay){
        return new Card(cardDisplay.getName(), cardDisplay.getPoints());
    }
}
