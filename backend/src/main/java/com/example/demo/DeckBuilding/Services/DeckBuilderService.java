package com.example.demo.DeckBuilding.Services;

import Cards.Card;
import com.example.demo.DeckBuilding.Services.DeckBuilder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DeckBuilderService implements DeckBuilder {

    private List<Card> Cards;

    public DeckBuilderService() {
        InitAllCards();
    }

    private void InitAllCards() {
        Cards = List.of(
            new Card("Knight"), new Card("Witch"), new Card("Thunder"), new Card("Warrior"),
            new Card("Viking"), new Card("Capitan"), new Card("Armageddon")
        );
    }

    @Override
    public List<Card> GetAllCards() {
        return Cards;
    }
}
