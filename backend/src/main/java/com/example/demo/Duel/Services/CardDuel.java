package com.example.demo.Duel.Services;

import java.util.Collection;
import java.util.List;

public interface CardDuel {
    boolean whoWon();

    List<String> getPlayerCardsDisplay();

    void parseCards(List<String> cardsDisplay);
}
