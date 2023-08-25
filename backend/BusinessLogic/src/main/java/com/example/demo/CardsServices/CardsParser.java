package com.example.demo.CardsServices;

import com.example.demo.CardsServices.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class CardsParser {

    public static List<Card> getCardsFromDisplays(List<CardDisplay> cardsToParse) {
        List<Card> result = new ArrayList<Card>();

        for (int i = 0; i < cardsToParse.size(); ++i) {
            Card createdCard = Card.createCard(cardsToParse.get(i));
            result.add(createdCard);
        }
        return result;
    }

    public static List<CardDisplay> getCardsDisplay(List<Card> cards) {
        List<CardDisplay> result = new ArrayList<CardDisplay>();
        for(int i = 0 ; i < cards.size() ; ++i){
            result.add(cards.get(i).getDisplay());
        }
        return result;

    }
}
