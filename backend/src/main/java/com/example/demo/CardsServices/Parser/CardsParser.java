package com.example.demo.CardsServices.Parser;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;

import java.util.List;

public interface CardsParser {
    List<Card> getParsedCards();

    void addCardsToParse(List<CardDisplay> cardDisplays);

    List<CardDisplay> getCardsDisplay(List<Card> cards);
}
