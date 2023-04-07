package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;

public class Card {

    private String name;

    private int points;

    public Card(String name){
        this.name = name;
        points = 1;
    }

    public CardDisplay getDisplay(){return new CardDisplay(name);}
    public int getPoints(){return points;}

    public static Card createCard(String name){
        return new Card(name);
    }

}
