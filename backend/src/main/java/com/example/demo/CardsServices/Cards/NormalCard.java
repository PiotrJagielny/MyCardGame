package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;

public class NormalCard implements Card {

    private String name;

    public NormalCard(String display) {
        this.name = display;
    }

    public CardDisplay getDisplay() {
        return new CardDisplay(name);
    }


}
