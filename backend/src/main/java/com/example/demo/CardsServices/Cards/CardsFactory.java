package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.Cards.Card;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardsFactory {
    
    public List<Card> createAllCards(){
        List<Card> result = new ArrayList<Card>(Arrays.asList(
                Card.createCard("Knight"), Card.createCard("Witch"), Card.createCard("Thunder"), Card.createCard("Warrior"),
                Card.createCard("Viking"), Card.createCard("Capitan"), Card.createCard("Armageddon"))
        );
        return result;
    }
}
