package com.example.demo.TestsData;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Consts;
import com.example.demo.Duel.ClientAPI.CardDuel;
import com.example.demo.Duel.PlayerPlay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.demo.TestsData.TestConsts.*;

public final class TestsUtils {
    private TestsUtils() {}
    public static int getBoardPointsOf(String player, CardDuel duel) {
        return IntStream.range(0, Consts.rowsNumber)
                .mapToObj(i -> duel.getRowPointsOf(player, i)).collect(Collectors.toList())
                .stream().mapToInt(Integer::intValue).sum();
    }

    public static CardDisplay playCardWithoutTargeting(CardDuel duel, CardDisplay cardPlayed, int onRow, String player) {
        return duel.playCardAs(new PlayerPlay(cardPlayed, onRow), player);
    }
    public static CardDisplay playCardWithCardTargeting(CardDuel duel, CardDisplay cardPlayed, int onRow, CardDisplay cardTargeted, String player) {
        return duel.playCardAs(new PlayerPlay(cardPlayed, onRow, cardTargeted), player);
    }
    public static CardDisplay playCardWithRowTargeting(CardDuel duel,CardDisplay cardPlayed, int onRow, int affectedRow, String player){
        return duel.playCardAs(new PlayerPlay(cardPlayed, onRow, new CardDisplay(),affectedRow), player);
    }

    public static CardDisplay playSpecialCardWithoutTargeting(CardDuel duel, CardDisplay cardPlayed, String player) {
        return duel.playCardAs(new PlayerPlay(cardPlayed), player);
    }
    public static CardDisplay playSpecialCardWithCardTargeting(CardDuel duel, CardDisplay cardPlayed, CardDisplay cardTargeted, String player) {
        return duel.playCardAs(new PlayerPlay(cardPlayed, cardTargeted), player);
    }
    public static CardDisplay playSpecialCardWithRowTargeting(CardDuel duel, CardDisplay cardPlayed, int rowTargeted, String player) {
        return duel.playCardAs(new PlayerPlay(rowTargeted, cardPlayed), player);
    }

    public static CardDisplay findByName(List<CardDisplay> cards, CardDisplay cardToFind) {
        return cards.stream().filter(c -> c.equals(cardToFind)).findFirst().orElse(new CardDisplay());
    }

    public static List<CardDisplay> board(CardDuel duel, String player) {
        List<CardDisplay> allCards = new ArrayList<>();
        for (int i = 0; i < Consts.rowsNumber; i++) {
            allCards.addAll(duel.getRowOf(player, i));
        }
        return allCards;
    }




    public static CardDuel createDuel(List<CardDisplay> deck){
        CardDuel result = CardDuel.createDuel();
        result.registerPlayerToDuel(player2, "");
        result.registerPlayerToDuel(player1, "");
        result.parseCardsFor(deck , player2);
        result.parseCardsFor(deck , player1);
        result.dealCards();
        return result;
    }

}
