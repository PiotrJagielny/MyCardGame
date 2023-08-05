package com.example.demo.CardsServices;


public class CardDisplay{

    private String name;
    private int points;
    private String cardInfo;

    public CardDisplay(String name, int points) {
        this.name = name;
        this.points = points;
        cardInfo = "";
    }
    public CardDisplay(String name, int points, String cardInfo) {
        this.name = name;
        this.points = points;
        this.cardInfo = cardInfo;
    }


    public CardDisplay(String name) {
        this.name = name;
        points = 1;
        cardInfo = "";
    }

    public CardDisplay() {
        name="";
        points = 0;
        cardInfo = "";
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
}
