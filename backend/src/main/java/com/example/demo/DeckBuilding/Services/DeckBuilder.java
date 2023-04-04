package com.example.demo.DeckBuilding.Services;

import com.example.demo.CardsServices.Cards.Card;

import java.util.List;

public interface DeckBuilder {
    List<Card> getCardsPossibleToAdd();
    String addCardToDeck(String cardDisplay);
    List<String> getDecksNames();
    List<Card> getPlayerDeck();
    void createDeck(String deckName);
    void selectDeck(String deckName);
    void putCardFromDeckBack(String cardDisplay);
    String deleteCurrentDeck();
}
