package com.example.demo.CardsServices.CardTargetStrattegies;

import com.example.demo.CardsServices.CardDisplay;

import java.util.ArrayList;
import java.util.List;

public class AllPlayerCardsTargetable implements CardTargeting{
    @Override
    public List<CardDisplay> getPossibleTargets(List<List<CardDisplay>> playerBoard, List<List<CardDisplay>> enemyBoard) {
        List<CardDisplay> result = new ArrayList<>();
        for (int i = 0; i < playerBoard.size(); i++) {
            result.addAll(playerBoard.get(i));
        }
        return result;
    }
}
