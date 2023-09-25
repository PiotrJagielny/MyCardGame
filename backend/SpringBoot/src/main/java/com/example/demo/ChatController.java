package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static Map<String,String> playerEnemy = new HashMap<>();
    private static List<String> waitingPlayers= new ArrayList<>();
    private static Map<String,Integer> playerGameId = new HashMap<>();
    private static int maxGameId = 0;

    @MessageMapping("/findEnemy")
    public String receivePrivateMessage(@Payload String userName) {

        if(waitingPlayers.isEmpty()){
            waitingPlayers.add(userName);
        }
        else {
            String firstPlayer = waitingPlayers.get(0);
            String secondPlayer = userName;
            playerEnemy.put(firstPlayer, secondPlayer);
            playerEnemy.put(secondPlayer, firstPlayer);
            waitingPlayers.remove(0);

            maxGameId++;
            playerGameId.put(firstPlayer, maxGameId);
            playerGameId.put(secondPlayer, maxGameId);

            simpMessagingTemplate.convertAndSendToUser(secondPlayer, "/private", "Found enemy:" + maxGameId);
        }
        return userName;
    }
    @MessageMapping("/enemyFound")
    public String sendToEnemyThatIsReadyForDuel(@Payload String username) {
        simpMessagingTemplate.convertAndSendToUser(playerEnemy.get(username), "/registerAfterEnemy", "Found enemy:" + playerGameId.get(username));
        return username;
    }
    @MessageMapping("/getIntoDuelPage")
    public String getIntoDuelPage(@Payload String username) {
        simpMessagingTemplate.convertAndSendToUser(username, "/private", "Get into duel page");
        simpMessagingTemplate.convertAndSendToUser(playerEnemy.get(username), "/private", "Get into duel page");
        return username;
    }
    @MessageMapping("/sendToEnemy")
    public String sendMessageToEnemy(@Payload String userName) {
        simpMessagingTemplate.convertAndSendToUser(playerEnemy.get(userName), "/private", "Get data from server:" + userName);
        return userName;
    }
    @MessageMapping("/sendTrigger")
    public String sendTriggerToEnemy(@Payload String userName) {
        simpMessagingTemplate.convertAndSendToUser(playerEnemy.get(userName), "/game", "Get data from server:" + userName);
        return userName;
    }

    @MessageMapping("/mulliganEnded")
    public String sendToEnemyThatMulliganIsEnded(@Payload String userName) {
        simpMessagingTemplate.convertAndSendToUser(playerEnemy.get(userName), "/mulligan", "end");
        return userName;
    }
}
