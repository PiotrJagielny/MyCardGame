package com.example.demo.DeckBuilding.Services;

import com.example.demo.Cards.Card;

import java.util.List;
import java.util.function.Supplier;

public interface DeckBuilder {
    List<Card> GetCardsPossibleToAdd();

    String AddCardToDeck(String CardName);

    List<String> GetDecksNames();
    List<Card> GetPlayerDeck();

    void CreateDeck(String deckName);

    void SelectDeck(String deckName);

    void PutCardFromDeckBack(String cardName);
}
