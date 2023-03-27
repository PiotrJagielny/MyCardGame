package com.example.demo.DeckBuilder;

import com.example.demo.DeckBuilding.Services.DeckBuilder;
import com.example.demo.DeckBuilding.Services.DeckBuilderService;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class DeckBuilderTests {
    @Test
    public void TestDeckBuilder(){
        DeckBuilder deckBuilder = new DeckBuilderService();
        Assert.isTrue(deckBuilder != null, "");
    }

    @Test
    public void TestAddingCardToPlayerDeck(){
        DeckBuilder deckBuilder = new DeckBuilderService();

        int PossibleCardsToAddBeforeAddingToDeck = deckBuilder.GetCardsPossibleToAdd().size();
        deckBuilder.AddCardToDeck( deckBuilder.GetCardsPossibleToAdd().get(0).getName() );
        int PossibleCardsToAddAfterAddingToDeck = deckBuilder.GetCardsPossibleToAdd().size();

        Assert.isTrue(deckBuilder.GetPlayerDeck().size() == 1, "");
        Assert.isTrue(PossibleCardsToAddBeforeAddingToDeck == PossibleCardsToAddAfterAddingToDeck + 1, "");
    }

    @Test
    public void TestAddingBackCardFromDeck(){
        DeckBuilder deckBuilder = new DeckBuilderService();

        deckBuilder.AddCardToDeck( deckBuilder.GetCardsPossibleToAdd().get(0).getName() );
        deckBuilder.PutCardFromDeckBack( deckBuilder.GetPlayerDeck().get(0).getName() );

        Assert.isTrue(deckBuilder.GetPlayerDeck().isEmpty() == true, "");
    }
}
