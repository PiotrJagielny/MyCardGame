package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardsFactory {
    
    public List<Card> createAllCards(){
        List<Card> result = new ArrayList<Card>(Arrays.asList(
                Card.createCard(new CardDisplay("Knight", 2)),
                Card.createCard(new CardDisplay("Witch", 3)),
                Card.createCard(new CardDisplay("Thunder", 4)),
                Card.createCard(new CardDisplay("Warrior",5)),
                Card.createCard(new CardDisplay("Viking",6)),
                Card.createCard(new CardDisplay("Capitan",7)),
                Card.createCard(new CardDisplay("Armageddon",8))
        ));
        return result;
    }
}
