package com.example.demo.WebSockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
            simpMessagingTemplate.convertAndSendToUser(secondPlayer, "/private", "Found enemy:" + maxGameId);
            wait(50);
            //If message will be send one right after another, then two games will be created
            simpMessagingTemplate.convertAndSendToUser(firstPlayer, "/private", "Found enemy:" + maxGameId);
            wait(50);
            simpMessagingTemplate.convertAndSendToUser(firstPlayer, "/private", "Get into duel page");
            simpMessagingTemplate.convertAndSendToUser(secondPlayer, "/private", "Get into duel page");



        }
        return userName;
    }
    public void wait(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
}
