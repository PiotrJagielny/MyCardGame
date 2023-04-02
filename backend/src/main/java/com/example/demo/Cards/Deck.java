package com.example.demo.Cards;

import com.example.demo.Consts;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> CardsInDeck;

    private List<Card> AllCardsPossibleToAdd;
    private String DeckName;

    public Deck(List<Card> allCardsPossibleToAdd, String deckName) {
        DeckName = deckName;
        CardsInDeck = new ArrayList<Card>();
        AllCardsPossibleToAdd = new ArrayList<Card>();
        for(int i = 0 ; i < allCardsPossibleToAdd.size() ; ++i){
            AllCardsPossibleToAdd.add(allCardsPossibleToAdd.get(i));
        }
    }

    public void AddCard(Card card){

        CardsInDeck.add(card);


    }

    public String AddCard(String cardName){

        String ResponseMessage="";
        Card card = AllCardsPossibleToAdd.stream().filter(c -> c.getName().equals(cardName)).findFirst().orElse(null);
        if (card == null || CardsInDeck.size() == Consts.MaxDeckSize) {
            ResponseMessage = Consts.DeckFullMessage;
            return ResponseMessage;
        }
        CardsInDeck.add(card);
        AllCardsPossibleToAdd.removeIf(c -> c.getName().equals(card.getName()));
        return ResponseMessage;
    }

    public void PutCardFromDeckBack(String cardName){
        Card card = GetCardBy(cardName);

        if(card == null) return;

        AllCardsPossibleToAdd.add(card);
        RemoveCardFromDeck(card);
    }

    public List<Card> getCardsInDeck() {
        return CardsInDeck;
    }
    public void RemoveCardFromDeck(Card card){
        CardsInDeck.removeIf(c -> c.getName() == card.getName());
    }

    public Card GetCardBy(String Name){
        return CardsInDeck.stream().filter(c -> c.getName().equals(Name)).findFirst().orElse(null);
    }


    public String GetDeckName() {
        return DeckName;
    }

    public List<Card> GetCardsPossibleToAdd() {
        return AllCardsPossibleToAdd;
    }
}
