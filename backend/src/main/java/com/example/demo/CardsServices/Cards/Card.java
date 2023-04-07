package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;

public interface Card {

    CardDisplay getDisplay();
    int getPoints();

    public static Card createCard(String name){
        return new NormalCard(name);
    }

}
