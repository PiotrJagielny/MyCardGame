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

    private String SelectedDeck;
    private List<Deck> PlayerDecks;

    public DeckBuilderService() {
        PlayerDecks = new ArrayList<Deck>();
        PlayerDecks.add(new Deck(InitAllCards(),"Deck"));
        SelectedDeck = "Deck";
    }

    private List<Card> InitAllCards() {
        List<Card> result = new ArrayList<Card>(Arrays.asList(
            new Card("Knight"), new Card("Witch"), new Card("Thunder"), new Card("Warrior"),
            new Card("Viking"), new Card("Capitan"), new Card("Armageddon"))
        );
        return result;
    }

    @Override
    public List<Card> GetCardsPossibleToAdd() {
        return PlayerDecks.get( GetSelectedDeckIndex() ).GetCardsPossibleToAdd();
    }

    @Override
    public String AddCardToDeck(String CardName) {
        return PlayerDecks.get( GetSelectedDeckIndex() ).AddCard(CardName);

    }

    @Override
    public void PutCardFromDeckBack(String cardName) {
        PlayerDecks.get(GetSelectedDeckIndex()).PutCardFromDeckBack(cardName);

    }

    @Override
    public String DeleteCurrentDeck() {
        String ResponseMessage = "";
        if(IsAbleToDeleteDeck() == false){
            ResponseMessage = "Cannot delete last deck";
            return ResponseMessage;
        }
        DeleteDeck();
        return ResponseMessage;
    }

    private Boolean IsAbleToDeleteDeck(){return PlayerDecks.size() != 1;}

    private void DeleteDeck(){
        PlayerDecks.removeIf(d -> d.GetDeckName().equals(SelectedDeck));
        SelectedDeck = GetDecksNames().get(0);
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
        return PlayerDecks.get(GetSelectedDeckIndex()).getCardsInDeck();
    }

    @Override
    public void CreateDeck(String deckName) {
        if(GetDecksNames().contains(deckName) == false)
            PlayerDecks.add(new Deck(InitAllCards(), deckName));
    }

    @Override
    public void SelectDeck(String deckName) {
        SelectedDeck = deckName;
    }

    private int GetSelectedDeckIndex() {
        int deckIndex = IntStream.range(0, PlayerDecks.size()).filter(i -> SelectedDeck.equals(PlayerDecks.get(i).GetDeckName())).findFirst().orElse(-1);
        return deckIndex;
    }

}
