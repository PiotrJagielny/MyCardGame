package com.example.server.WebSockets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private Map<String, Integer> playersPoints;

    public Game() {
        playersPoints = new HashMap<>();
    }

    public void registerPlayer(String player) {
        playersPoints.put(player, 0);
    }
    public void increasePlayer(String player) {
        playersPoints.put(player, playersPoints.get(player) + 1);
    }
    public int getPlayerPoints(String player) {
        return playersPoints.get(player);
    }
    public ArrayList<String> getPlayers() {
        return new ArrayList<>(playersPoints.keySet());
    }
}
