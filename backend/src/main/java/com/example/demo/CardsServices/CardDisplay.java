package com.example.demo.CardsServices;

import com.example.demo.CardsServices.Cards.Card;

public class CardDisplay {
    private String name;

    public CardDisplay(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CardDisplay){
            CardDisplay dis = (CardDisplay) obj;
            return dis.getName() == name;
        }
        else
            return false;
    }
}
