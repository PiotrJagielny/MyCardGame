package com.example.server.WebSockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static List<GameRoom> rooms = new ArrayList<>();
    private static int maxGameId = 0;

    @MessageMapping("/findEnemy")
    public String receivePrivateMessage(@Payload String userName) {

        if(rooms.isEmpty()) {
            rooms.add(new GameRoom(userName));
        }
        else {
            if(isGameRoomFull(rooms.get(rooms.size() -1 ))) {
                rooms.add(new GameRoom(userName));
            }
            else {
                rooms.get(rooms.size() - 1).setSecondPlayer(userName);
                maxGameId++;
                simpMessagingTemplate.convertAndSendToUser(userName, "/private", "Found enemy:" + maxGameId);
                try {
                    //If message will be send one right after another, then two games will be created
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                simpMessagingTemplate.convertAndSendToUser(rooms.get(rooms.size() - 1).getFirstPlayer(), "/private", "Found enemy:" + maxGameId);

            }
        }

        return userName;
    }
    @MessageMapping("/sendToEnemy")
    public String sendMessageToEnemy(@Payload String userName) {
        System.out.println("JEST WYSYLANIE DO ENEMY");
        return userName;
    }

    private boolean isGameRoomFull(GameRoom room) {
        return !room.getFirstPlayer().isEmpty() && !room.getSecondPlayer().isEmpty();
    }
}
