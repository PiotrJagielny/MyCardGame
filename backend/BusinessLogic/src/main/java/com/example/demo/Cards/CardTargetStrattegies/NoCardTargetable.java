package com.example.demo.Cards.CardTargetStrattegies;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Duel.OnePlayerDuel;

import java.util.List;

public class NoCardTargetable implements CardTargeting{
    @Override
    public List<CardDisplay> getPossibleTargets(OnePlayerDuel player, OnePlayerDuel enemy) {
        return List.of();
    }
}
