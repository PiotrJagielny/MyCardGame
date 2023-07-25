package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.CardTargetStrattegies.CardTargeting;

import java.util.List;

public class Card {

    private String name;

    private int points;

    public Card() {
        name="";
        points = 0;
    }

    public Card(String name, int points){
        this.name = name;
        this.points = points;
    }

    public CardDisplay getDisplay(){return new CardDisplay(name, points, CardsFactory.getCardInfo(name));}
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
    public static Card createEmptyCard(){
        return new Card();
    }

    @Override
    public boolean equals(Object obj) {
        Card card = (Card)obj;
        return card.getDisplay().equals(this.getDisplay());

    }
}
