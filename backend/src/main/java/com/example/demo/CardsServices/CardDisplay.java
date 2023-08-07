package com.example.demo.CardsServices;


import com.example.demo.CardsServices.Cards.CardsFactory;

public class CardDisplay{

    private String name;
    private int points;
    private String cardInfo;
    private int timer;

    public CardDisplay(String name, int points) {
        this.name = name;
        this.points = points;
        cardInfo = "";
        timer = CardsFactory.noTimer;
    }
    public CardDisplay(String name, int points, String cardInfo) {
        this.name = name;
        this.points = points;
        this.cardInfo = cardInfo;
        timer = CardsFactory.noTimer;
    }


    public CardDisplay(String name) {
        this.name = name;
        points = 1;
        cardInfo = "";
        timer = CardsFactory.noTimer;
    }

    public CardDisplay() {
        name="";
        points = 0;
        cardInfo = "";
        timer = CardsFactory.noTimer;
    }


    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public String getCardInfo() {
        return cardInfo;
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

    @Override
    public String toString() {
        return "CardDisplay{" +
                "name='" + name + '\'' +
                ", points=" + points +
                ", cardInfo='" + cardInfo + '\'' +
                ", timer=" + timer +
                '}';
    }
}
