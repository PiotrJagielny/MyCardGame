package com.example.demo.Duel.Services;

import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Parser.CardsParser;
import com.example.demo.CardsServices.Parser.NormalCardsParser;

import java.util.ArrayList;
import java.util.List;

public class NormalDuel implements CardDuel{

    private Boolean areCardsAdded;
    private List<Card> playerBattleDeck;

    public NormalDuel() {
        playerBattleDeck = new ArrayList<Card>();
        this.areCardsAdded = false;
    }

    @Override
    public boolean whoWon() {
        return false;
    }

    @Override
    public List<String> getPlayerCardsDisplay() {
        CardsParser parser = new NormalCardsParser();
        return parser.getCardsDisplays(playerBattleDeck);
    }

    @Override
    public void parseCards(List<String> cardsDisplay) {
        areCardsAdded = true;
        CardsParser parser = new NormalCardsParser();
        parser.addCardsToParse(cardsDisplay);
        playerBattleDeck = parser.getParsedCards();
    }
}
