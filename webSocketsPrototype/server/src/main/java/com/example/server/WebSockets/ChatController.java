package com.example.server.WebSockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static List<GameRoom> rooms = new ArrayList<>();

    @MessageMapping("/findEnemy")
    public String receivePrivateMessage(@Payload String userName) {

        System.out.println("szukanie!!!!!! dla: " + userName);

        if(rooms.isEmpty()) {
            rooms.add(new GameRoom(userName));
        }
        else {
            if(isGameRoomFull(rooms.get(rooms.size() -1 ))) {
                rooms.add(new GameRoom(userName));
            }
            else {
                rooms.get(rooms.size() - 1).setSecondPlayer(userName);
                simpMessagingTemplate.convertAndSendToUser(userName, "/private", "Found enemy");
                simpMessagingTemplate.convertAndSendToUser(rooms.get(rooms.size() - 1).getFirstPlayer(), "/private", "Found enemy");
            }
        }

        return userName;
    }
    private boolean isGameRoomFull(GameRoom room) {
        return !room.getFirstPlayer().isEmpty() && !room.getSecondPlayer().isEmpty();
    }
}
