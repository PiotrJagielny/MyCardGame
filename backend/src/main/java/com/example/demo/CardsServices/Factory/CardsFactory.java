package com.example.demo.CardsServices.Factory;

import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Cards.NormalCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardsFactory {

    public List<Card> createAllCards(){
        List<Card> result = new ArrayList<Card>(Arrays.asList(
                new NormalCard("Knight"), new NormalCard("Witch"), new NormalCard("Thunder"), new NormalCard("Warrior"),
                new NormalCard("Viking"), new NormalCard("Capitan"), new NormalCard("Armageddon"))
        );
        return result;
    }
}
