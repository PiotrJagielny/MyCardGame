package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;


public class Card {
    private static int uniqueCardId = 1;


    private String name;

    private int points;
    private int id;

    public Card() {
        name="";
        points = 0;
        id = uniqueCardId++;
    }

    public Card(String name, int points){
        this.name = name;
        this.points = points;
        id = uniqueCardId++;
    }

    public CardDisplay getDisplay(){return new CardDisplay(name, points, CardsFactory.getCardInfo(name), id);}
    public int getPoints(){return points;}
    public int getId() {return id;}
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
//        return card.getDisplay().equals(this.getDisplay());
        return card.id == id;


    }
}

