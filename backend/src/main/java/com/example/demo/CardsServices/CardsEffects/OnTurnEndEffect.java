package com.example.demo.CardsServices.CardsEffects;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.CardsFactory;
import com.example.demo.Duel.OnePlayerDuel;

import java.util.List;

public class OnTurnEndEffect {
    private OnePlayerDuel player;

    public OnTurnEndEffect(OnePlayerDuel player ) {
        this.player = player;
    }



    private void invokeOnTurnEndEffect() {
        List<CardDisplay> cardsOnBoard = player.getCardsOnBoard();
        for (var card : cardsOnBoard) {
            invokeSpecificCardTurnEffect(card);
        }
        player.updateRows();
    }
    private void invokeSpecificCardTurnEffect(CardDisplay card) {
        if(card.equals(CardsFactory.longer)) {
            player.boostCard(card, CardsFactory.longerBoostAmount);
        }
    }

    public void invokeEffect(){
        invokeOnTurnEndEffect();
    }
}
