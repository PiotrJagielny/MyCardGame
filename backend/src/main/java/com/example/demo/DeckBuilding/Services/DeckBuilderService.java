package com.example.demo.DeckBuilding.Services;

import com.example.demo.Cards.Card;
import com.example.demo.Cards.Deck;
import com.example.demo.Consts;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeckBuilderService implements DeckBuilder {

    private List<Card> CardsPossibleToAdd;
    private Deck PlayerDeck;


    public DeckBuilderService() {
        InitAllCards();
        PlayerDeck = new Deck();
    }

    private void InitAllCards() {
        CardsPossibleToAdd = new ArrayList<Card>(Arrays.asList(
            new Card("Knight"), new Card("Witch"), new Card("Thunder"), new Card("Warrior"),
            new Card("Viking"), new Card("Capitan"), new Card("Armageddon"))
        );
    }

    @Override
    public List<Card> GetCardsPossibleToAdd() {
        return CardsPossibleToAdd;
    }

    @Override
    public String AddCardToDeck(String CardName) {
        String ResponseMessage="";
        Card card = CardsPossibleToAdd.stream().filter(c -> c.getName().equals(CardName)).findFirst().orElse(null);
        if (card == null || PlayerDeck.getCardsInDeck().size() == Consts.MaxDeckSize) {
            ResponseMessage = Consts.DeckFullMessage;
            return ResponseMessage;
        }

        PlayerDeck.AddCard(card);
        CardsPossibleToAdd.removeIf(c -> c.getName() == card.getName());
        return ResponseMessage;
    }

    @Override
    public void PutCardFromDeckBack(String CardName) {
        Card card = PlayerDeck.GetCardBy(CardName);

        if(card == null) return;

        CardsPossibleToAdd.add(card);
        PlayerDeck.RemoveCardFromDeck(card);
    }

    @Override
    public List<Card> GetPlayerDeck() {
        return PlayerDeck.getCardsInDeck();
    }
}
