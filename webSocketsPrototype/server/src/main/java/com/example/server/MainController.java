package com.example.server;

import com.example.server.WebSockets.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "button")
public class MainController {
    private static Map<String, Integer> userPoints = new HashMap<>();
    private static List<Game> games = new ArrayList<>();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    @CrossOrigin
    public void increaseButtonPoints(@RequestBody String userName) {
        userPoints.put(userName, userPoints.get(userName) + 1);
        simpMessagingTemplate.convertAndSendToUser(userName, "/private", "The Points are valid");
    }
    @PutMapping
    @CrossOrigin
    public void registerGame(@RequestBody String players) {
        String[] playersExtracted = players.split(":");
        Game game = new Game();
        game.registerPlayer(playersExtracted[0]);
        game.registerPlayer(playersExtracted[1]);
        games.add(game);
    }
    @GetMapping(path = "getPoints/{userName}")
    @CrossOrigin
    public int getPoints(@PathVariable String userName) {
        return userPoints.get(userName);
    }
}
