package com.example.demo.CardsServices.CardTargetStrattegies;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Duel.OnePlayerDuel;

import java.util.ArrayList;
import java.util.List;

public class AllPlayerCardsTargetable implements CardTargeting{
    @Override
    public List<CardDisplay> getPossibleTargets(OnePlayerDuel player, OnePlayerDuel enemy) {
        return player.getCardsOnBoard();
    }
}
