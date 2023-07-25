package com.example.demo.CardsServices.CardTargetStrattegies;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Duel.OnePlayerDuel;

import java.util.List;

public interface CardTargeting {
    List<CardDisplay> getPossibleTargets(OnePlayerDuel player,OnePlayerDuel enemy);
}
