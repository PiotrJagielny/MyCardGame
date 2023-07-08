package com.example.server.WebSockets;

public class GameRoom {
    private String firstPlayer;
    private String secondPlayer;

    public GameRoom(String player) {
        firstPlayer = player;
        secondPlayer = "";
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer = secondPlayer;
    }
}
