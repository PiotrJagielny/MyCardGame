package com.example.demo.TestsData;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Consts;
import com.example.demo.Duel.CardDuel;
import com.example.demo.Duel.PlayerPlay;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.demo.TestsData.TestConsts.*;

public class TestsUtils {
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

    public static CardDuel createDuel(List<CardDisplay> deck){
        CardDuel result = CardDuel.createDuel();
        result.registerPlayerToDuel(secondPlayer);
        result.registerPlayerToDuel(firstPlayer);
        result.parseCardsFor(deck , secondPlayer);
        result.parseCardsFor(deck , firstPlayer);
        result.dealCards();
        return result;
    }
    public static void setHands(List<CardDisplay> hand1, List<CardDisplay> hand2, CardDuel duel) {
        hand1 = duel.getHandOf(firstPlayer);
        hand2 = duel.getHandOf(secondPlayer);
    }

}
