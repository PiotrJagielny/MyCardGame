package com.example.demo.Cards.CardTargetStrattegies;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Duel.OnePlayerDuel;

import java.util.List;

public class PlayerCardsTargetable implements CardTargeting{
    @Override
    public List<CardDisplay> getPossibleTargets(OnePlayerDuel player, OnePlayerDuel enemy) {
        return player.getCardsOnBoard();
    }
}
