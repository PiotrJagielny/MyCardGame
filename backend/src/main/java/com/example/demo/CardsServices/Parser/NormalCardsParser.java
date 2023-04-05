package com.example.demo.CardsServices.Parser;

import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Cards.NormalCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NormalCardsParser implements CardsParser {

    private List<String> cardsToParse;

    public NormalCardsParser() {
        cardsToParse = new ArrayList<String>();
    }

    @Override
    public List<Card> getParsedCards() {
        List<Card> result = new ArrayList<Card>();

        for(int i = 0; i < cardsToParse.size() ; ++i){
            Card createdCard = new NormalCard(cardsToParse.get(i));
            result.add(createdCard);
        }
        return result;
    }

    @Override
    public void addCardsToParse(List<String> cardDisplays) {
        cardsToParse.addAll(cardDisplays);
    }

    @Override
    public List<String> getCardsDisplays(List<Card> cards) {
        List<String> result = new ArrayList<String>();
        for(int i = 0 ; i < cards.size() ; ++i){
            result.add(cards.get(i).getDisplay());
        }
        return result;

    }
}
