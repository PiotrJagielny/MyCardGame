package com.example.demo.DeckBuilding.Services;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;

import java.util.List;

public interface DeckBuilder {
    List<CardDisplay> getCardsPossibleToAdd();
    String addCardToDeck(CardDisplay cardDisplay);
    List<String> getDecksNames();
    List<CardDisplay> getPlayerDeck();
    void createDeck(String deckName);
    void selectDeck(String deckName);
    void putCardFromDeckBack(CardDisplay cardDisplay);
    String deleteCurrentDeck();
}
