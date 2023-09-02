package com.example.demo.Cards.CardTargetStrattegies;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Duel.OnePlayerDuel;

import java.util.List;
import java.util.stream.Collectors;

public class EnemyCardsTargetable implements CardTargeting {
    @Override
    public List<CardDisplay> getPossibleTargets(OnePlayerDuel player, OnePlayerDuel enemy) {
        return enemy.getCardsOnBoard();
    }
}
