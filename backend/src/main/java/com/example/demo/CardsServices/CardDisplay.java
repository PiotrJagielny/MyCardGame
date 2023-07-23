package com.example.demo.CardsServices;

import com.example.demo.CardsServices.Cards.Card;

public class CardDisplay{

    private String name;
    private int points;

    public CardDisplay(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public CardDisplay(String name, int points, String cardInfo) {
        this.name = name;
        this.points = points;
    }

    public CardDisplay(String name) {
        this.name = name;
        points = 1;
    }

    public CardDisplay() {
        name="";
        points = 0;
    }


    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }




    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CardDisplay){
            CardDisplay dis = (CardDisplay) obj;
            return dis.getName().equals(name);
        }
        else
            return false;
    }
}
