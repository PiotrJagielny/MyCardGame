package com.example.demo.CardsServices.CardTargetStrattegies;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;

import java.util.List;

public interface CardTargeting {
    List<CardDisplay> getPossibleTargets(List<List<CardDisplay>> playerBoard, List<List<CardDisplay>> enemyBoard);

    static CardTargeting getTargetingStrategy(CardDisplay card){
        if(card.equals(CardsFactory.booster)){
            return new AllPlayerCardsTargetable();
        }
        else if(card.equals(CardsFactory.fireball)){
            return new AllEnemyCardsTargetable();
        }
        else {
            return new NoCardTargetable();
        }
    }
}
