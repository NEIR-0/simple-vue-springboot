package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public MessageDTO sendMessage(MessageDTO message) {
        System.out.println("Message from: " + message.getFrom() + " - Content: " + message.getContent());
        return message; // Kirim kembali ke client
    }
}