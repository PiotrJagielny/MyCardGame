package com.example.server;

import com.example.server.WebSockets.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    public void increaseButtonPoints(@RequestBody String message) throws SQLException {
        String[] extractedMsg = message.split(":");
        String gameID = extractedMsg[1];
        String playerName= extractedMsg[0];
        gamesBetter.get(gameID).increasePlayer(playerName);
        simpMessagingTemplate.convertAndSendToUser(playerName, "/private", "The Points are valid");

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
        }
        gamesBetter.get(gameID).registerPlayer(playerName);
        String jdbcURL = "jdbc:postgresql://localhost:5433/postgres";
        String username = "postgres";
        String password = "1234";
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connected to database");
            String query = "INSERT INTO tabela VALUES(3);";
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(query);
            if(rows > 0 ) {
                System.out.println("A new row has been inserted");
            }


            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @GetMapping(path = "getPoints/{userName}/{gameID}")
    @CrossOrigin
    public int getPoints(@PathVariable String userName, @PathVariable String gameID) {
        return gamesBetter.get(gameID).getPlayerPoints(userName);
    }
}
