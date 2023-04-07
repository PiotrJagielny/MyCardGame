package com.example.demo.CardsServices.Parser;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class NormalCardsParser implements CardsParser {

    private List<CardDisplay> cardsToParse;

    public NormalCardsParser() {
        cardsToParse = new ArrayList<CardDisplay>();
    }

    @Override
    public List<Card> getParsedCards() {
        List<Card> result = new ArrayList<Card>();

        for(int i = 0; i < cardsToParse.size() ; ++i){
            Card createdCard = Card.createCard(cardsToParse.get(i).getName());
            result.add(createdCard);
        }
        cardsToParse.clear();
        return result;
    }

    @Override
    public void addCardsToParse(List<CardDisplay> cardDisplays) {
        cardsToParse.addAll(cardDisplays);
    }

    @Override
    public List<CardDisplay> getCardsDisplay(List<Card> cards) {
        List<CardDisplay> result = new ArrayList<CardDisplay>();
        for(int i = 0 ; i < cards.size() ; ++i){
            result.add(new CardDisplay( cards.get(i).getDisplay().getName() ));
        }
        return result;

    }
}
