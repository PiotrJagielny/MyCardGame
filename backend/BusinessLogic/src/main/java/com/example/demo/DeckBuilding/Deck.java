package com.example.demo.DeckBuilding;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Cards.Card;
import com.example.demo.Cards.CardsFactory;
import com.example.demo.Consts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Deck {
    private List<Card> cardsInDeck;
    private List<Card> allCardsPossibleToAdd;
    private String deckName;

    private String currentNamesFilter;
    private Comparator<CardDisplay> currentSortCriteria;


    public Deck(List<Card> allCards) {

        cardsInDeck = new ArrayList<Card>();
        deckName="";
        this.allCardsPossibleToAdd = allCards;
        currentNamesFilter = "";
        currentSortCriteria = getComparator("");
    }


    public String addCard(CardDisplay cardDisplay){
        String responseMessage="";
        Card card = allCardsPossibleToAdd.stream().filter(c -> c.getDisplay().equals(cardDisplay)).findFirst().orElse(null);
        

        if(cardsInDeck.size() == Consts.maxDeckSize) {
            responseMessage = Consts.DeckFullMessage;
            return responseMessage;
        }

        if(card != null) {
            cardsInDeck.add(card);
            allCardsPossibleToAdd.removeIf(c -> c.equals(card) );

        }
        return responseMessage;
    }

    public void putCardFromDeckBack(CardDisplay cardDisplay){
        Card card = cardsInDeck.stream().filter(c -> c.getDisplay().equals(cardDisplay)).findFirst().orElse(null);

        if(card != null){
            allCardsPossibleToAdd.add(card);
            cardsInDeck.removeIf(c -> c.getDisplay().equals(card.getDisplay()) );
        }
    }

    public List<CardDisplay> getCardsInDeck() {
        return CardsFactory.getCardsDisplay(cardsInDeck);
    }


    public List<CardDisplay> getCardsPossibleToAdd() {
        return allCardsPossibleToAdd.stream()
                .filter(c -> c.getDisplay().getName().contains(currentNamesFilter))
                .map(c -> c.getDisplay())
                .sorted(currentSortCriteria)
                .collect(Collectors.toList());
    }

    public void sortCardsPossibleToAddBy(String criteria) {
        currentSortCriteria = getComparator(criteria);
    }

    public void searchForCards(String searchString) {
        currentNamesFilter = searchString;
    }
    private Comparator<CardDisplay> getComparator(String criteria) {
        switch(criteria.toLowerCase()) {
            case "points":
                return new Comparator<CardDisplay>() {
                    @Override
                    public int compare(CardDisplay o1, CardDisplay o2) {
                        return Integer.compare(o1.getPoints(), o2.getPoints());
                    }
                };
            case "color":
                return new Comparator<CardDisplay>() {
                    @Override
                    public int compare(CardDisplay o1, CardDisplay o2) {
                        return Integer.compare(
                                getColorNumber(o1.getColor()), getColorNumber(o2.getColor())
                        );
                    }
                };
            case "name":
                return new Comparator<CardDisplay>() {
                    @Override
                    public int compare(CardDisplay o1, CardDisplay o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                };
            default:
                return new Comparator<CardDisplay>() {
                    @Override
                    public int compare(CardDisplay o1, CardDisplay o2) {
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

}
