package com.example.demo.CardsServices.CardTargetStrattegies;

import com.example.demo.CardsServices.CardDisplay;

import java.util.ArrayList;
import java.util.List;

public class AllEnemyCardsTargetable implements CardTargeting{
    @Override
    public List<CardDisplay> getPossibleTargets(List<List<CardDisplay>> playerBoard, List<List<CardDisplay>> enemyBoard) {
        List<CardDisplay> result = new ArrayList<>();
        for (int i = 0; i < enemyBoard.size(); i++) {
            result.addAll(enemyBoard.get(i));
        }
        return result;
    }
}
