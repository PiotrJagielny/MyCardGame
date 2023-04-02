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
        CopyAllCards(allCardsPossibleToAdd);
    }

    private void CopyAllCards(List<Card> allCardsPossibleToAdd) {
        AllCardsPossibleToAdd = new ArrayList<Card>();
        for (int i = 0; i < allCardsPossibleToAdd.size(); ++i) {
            AllCardsPossibleToAdd.add(allCardsPossibleToAdd.get(i));
        }
    }

    public String AddCard(String cardName){

        String ResponseMessage="";
        Card card = GetCardBy_From(cardName, AllCardsPossibleToAdd);

        if(CardsInDeck.size() == Consts.MaxDeckSize) {
            ResponseMessage = Consts.DeckFullMessage;
            return ResponseMessage;
        }

        CardsInDeck.add(card);
        RemoveCardFrom(card, AllCardsPossibleToAdd);
        return ResponseMessage;
    }

    public void PutCardFromDeckBack(String cardName){
        Card card = GetCardBy_From(cardName, CardsInDeck);

        AllCardsPossibleToAdd.add(card);
        RemoveCardFrom(card, CardsInDeck);
    }

    public List<Card> getCardsInDeck() {
        return CardsInDeck;
    }

    public String GetDeckName() {
        return DeckName;
    }

    public List<Card> GetCardsPossibleToAdd() {
        return AllCardsPossibleToAdd;
    }

    public void RemoveCardFrom(Card card, List<Card> CardsCollection){
        CardsCollection.removeIf(c -> c.getName() == card.getName());
    }

    public Card GetCardBy_From(String Name, List<Card> CardsCollection){
        return CardsCollection.stream().filter(c -> c.getName().equals(Name)).findFirst().orElse(null);
    }
}
