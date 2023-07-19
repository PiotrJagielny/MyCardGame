package com.example.demo.TestsData;

import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.Consts;
import com.example.demo.Duel.CardDuel;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestsUtils {
    public static int getBoardPointsOf(String player, CardDuel duel) {
        return IntStream.range(0, Consts.rowsNumber)
                .mapToObj(i -> duel.getRowPointsOf(player, i)).collect(Collectors.toList())
                .stream().mapToInt(Integer::intValue).sum();
    }
}
