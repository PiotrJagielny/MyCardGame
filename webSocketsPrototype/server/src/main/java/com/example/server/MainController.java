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
    private static Map<String,Game> gamesBetter= new HashMap<>();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    @CrossOrigin
    public void increaseButtonPoints(@RequestBody String message) {
        String[] extractedMsg = message.split(":");
        String gameID = extractedMsg[1];
        String playerName= extractedMsg[0];
        gamesBetter.get(gameID).increasePlayer(playerName);
        simpMessagingTemplate.convertAndSendToUser(playerName, "/private", "The Points are valid");
        System.out.println(gamesBetter.get(gameID).getPlayers());
    }
    @PutMapping
    @CrossOrigin
    public void registerPlayer(@RequestBody String message) {
        System.out.println(message);
        String[] extractedMsg = message.split(":");
        String gameID = extractedMsg[1];
        String playerName= extractedMsg[0];
        if(gamesBetter.containsKey(gameID) == false) {
            gamesBetter.put(gameID, new Game());
            System.out.println("NOWA GRA");
        }
        gamesBetter.get(gameID).registerPlayer(playerName);
    }
    @GetMapping(path = "getPoints/{userName}/{gameID}")
    @CrossOrigin
    public int getPoints(@PathVariable String userName, @PathVariable String gameID) {
        return gamesBetter.get(gameID).getPlayerPoints(userName);
    }
}
