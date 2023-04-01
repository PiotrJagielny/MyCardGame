package com.example.demo.DeckBuilding.Services;

import com.example.demo.Cards.Card;
import com.example.demo.Cards.Deck;
import com.example.demo.Consts;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DeckBuilderService implements DeckBuilder {

    private List<Card> CardsPossibleToAdd;

    private String SelectedDeck;

    private List<Deck> PlayerDecks;
    public DeckBuilderService() {
        InitAllCards();
        PlayerDecks = new ArrayList<Deck>();
        PlayerDecks.add(new Deck("Deck"));
        SelectedDeck = "Deck";
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
        int deckIndex = IntStream.range(0,PlayerDecks.size()).filter(i -> SelectedDeck.equals(PlayerDecks.get(i).GetDeckName())).findFirst().orElse(-1);
        String ResponseMessage="";
        Card card = CardsPossibleToAdd.stream().filter(c -> c.getName().equals(CardName)).findFirst().orElse(null);
        if (card == null || PlayerDecks.get(deckIndex).getCardsInDeck().size() == Consts.MaxDeckSize) {
            ResponseMessage = Consts.DeckFullMessage;
            return ResponseMessage;
        }
        PlayerDecks.get(deckIndex).AddCard(card);
        CardsPossibleToAdd.removeIf(c -> c.getName() == card.getName());
        return ResponseMessage;
    }


    @Override
    public List<String> GetDecksNames() {
        List<String> Result = new ArrayList<String>();

        for(int i = 0 ; i < PlayerDecks.size() ; ++i){
            Result.add(PlayerDecks.get(i).GetDeckName());
        }

        return Result;
    }

    @Override
    public List<Card> GetPlayerDeck() {
        int deckIndex = IntStream.range(0, PlayerDecks.size()).filter(i -> SelectedDeck.equals(PlayerDecks.get(i).GetDeckName())).findFirst().orElse(-1);
        return PlayerDecks.get(deckIndex).getCardsInDeck();
    }

    @Override
    public void CreateDeck(String deckName) {
        if(GetDecksNames().contains(deckName) == false)
            PlayerDecks.add(new Deck(deckName));
    }

    @Override
    public void SelectDeck(String deckName) {
        SelectedDeck = deckName;
    }

    @Override
    public void PutCardFromDeckBack(String cardName) {
        int deckIndex = IntStream.range(0, PlayerDecks.size()).filter(i -> SelectedDeck.equals(PlayerDecks.get(i).GetDeckName())).findFirst().orElse(-1);
        Card card = PlayerDecks.get(deckIndex).GetCardBy(cardName);

        if(card == null) return;

        CardsPossibleToAdd.add(card);
        PlayerDecks.get(deckIndex).RemoveCardFromDeck(card);
    }
}
