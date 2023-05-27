package com.example.demo.CardsServices.Cards;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardsFactory {

    public static final CardDisplay witch = new CardDisplay("Witch", 2);
    public static final CardDisplay knight = new CardDisplay("Knight", 3);
    public static final CardDisplay thunder = new CardDisplay("Thunder", 4);
    public static final CardDisplay warrior = new CardDisplay("Warrior",5);
    public static final CardDisplay viking = new CardDisplay("Viking",6);
    public static final CardDisplay capitan = new CardDisplay("Capitan",7);
    public static final CardDisplay armageddon =new CardDisplay("Armageddon",8);

    public static final CardDisplay booster = new CardDisplay("Booster", 1);
    public static final CardDisplay leader = new CardDisplay("Leader", 1);
    public static final CardDisplay woodTheHealer = new CardDisplay("WoodTheHealer", 2);
    public static final CardDisplay paper = new CardDisplay("Paper", 1);
    public static final CardDisplay minion = new CardDisplay("Minion", 2);
    public static final CardDisplay fireball = new CardDisplay("Fireball", 1);

    public static List<Card> createAllCards(){
        List<Card> result = new ArrayList<Card>(Arrays.asList(
                Card.createCard(knight),
                Card.createCard(witch),
                Card.createCard(thunder),
                Card.createCard(warrior),
                Card.createCard(viking),
                Card.createCard(capitan),
                Card.createCard(armageddon),
                Card.createCard(minion),
                Card.createCard(paper)
        ));
        return result;
    }
}
