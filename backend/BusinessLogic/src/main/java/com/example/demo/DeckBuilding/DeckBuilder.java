package com.example.demo.DeckBuilding;

import com.example.demo.Cards.Card;
import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.CardsFactory;
import com.example.demo.Consts;

import java.util.*;

public class DeckBuilder {
    private Map<String, Deck> playerDecks;

    public DeckBuilder() {
        playerDecks = new HashMap<>();
    }


    public List<CardDisplay> getCardsPossibleToAdd(String deckName) {
        return playerDecks.get( deckName ).getCardsPossibleToAdd();
    }
    public List<CardDisplay> getCurrentDeck(String deckName) {
        return playerDecks.get(deckName).getCardsInDeck();
    }

    public String addCardToDeck(CardDisplay card, String deckName) {
        return playerDecks.get( deckName ).addCard(card);
    }

    public void sortCardsPossibleToAddBy(String deckName, String criteria) {
        playerDecks.get(deckName).sortCardsPossibleToAddBy(getComparator(criteria));
    }

    private Comparator<Card> getComparator(String criteria) {
        switch(criteria.toLowerCase()) {
            case "points":
                return new Comparator<Card>() {
                    @Override
                    public int compare(Card o1, Card o2) {
                        return Integer.compare(o1.getPoints(), o2.getPoints());
                    }
                };
            case "color":
                return new Comparator<Card>() {
                    @Override
                    public int compare(Card o1, Card o2) {
                        return Integer.compare(
                                getColorNumber(o1.getDisplay().getColor()), getColorNumber(o2.getDisplay().getColor())
                        );
                    }
                };
            case "name":
                return new Comparator<Card>() {
                    @Override
                    public int compare(Card o1, Card o2) {
                        return o1.getDisplay().getName().compareTo(o2.getDisplay().getName());
                    }
                };
            default:
                return new Comparator<Card>() {
                    @Override
                    public int compare(Card o1, Card o2) {
                        return 0;
                    }
                };
        }
    }

    private int getColorNumber(String color) {
        if(color.equals(Consts.gold)) {
            return 1;
        }
        else if(color.equals(Consts.silver)) {
            return 2;
        }
        else if(color.equals(Consts.bronze)) {
            return 3;
        }
        else {
            return -1;
        }
    }




    public void putCardFromDeckBack(CardDisplay cardDisplay, String deckName) {
        playerDecks.get(deckName).putCardFromDeckBack(cardDisplay);
    }


    public String deleteCurrentDeck(String deckName) {
        String responseMessage = "";
        if(isAbleToDeleteDeck(deckName) == false){
            responseMessage = "Cannot delete last deck";
            return responseMessage;
        }
        deleteDeck(deckName);
        return responseMessage;
    }

    private Boolean isAbleToDeleteDeck(String deckName){return playerDecks.size() != 1;}

    private void deleteDeck(String deckName) {
        playerDecks.remove(deckName);
    }



    public List<String> getDecksNames() {
        List<String> result = new ArrayList<String>();
        for (Map.Entry<String, Deck> entry : playerDecks.entrySet()) {
            result.add(entry.getKey());
        }
        return result;
    }




    public void createDeck(String deckName) {
        if(getDecksNames().contains(deckName) == false){
            playerDecks.put(deckName, new Deck(CardsFactory.createAllCards()));
        }
    }




    public boolean isDeckValid(String deckName) {
        Deck deck = playerDecks.get(deckName);
        return deck.getCardsInDeck().size() >= Consts.minDeckSize && deck.getCardsInDeck().size() <= Consts.maxDeckSize;
    }
}
