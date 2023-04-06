package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;

public class NormalCard implements Card {



    private String name;

    private int points;

    public NormalCard(String name) {
        points = 1;
        this.name = name;
    }

    public CardDisplay getDisplay() {
        return new CardDisplay(name);
    }

    @Override
    public int getPoints() {
        return points;
    }

}
