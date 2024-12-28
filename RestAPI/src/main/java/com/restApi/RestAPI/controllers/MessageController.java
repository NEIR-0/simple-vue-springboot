package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.MessageDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/message")
public class MessageController {

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public MessageDTO sendMessage(MessageDTO message, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String userRole = (String) request.getAttribute("userRole");

        System.out.println("Message from: " + message.getFrom() + " - Content: " + message.getContent());
        System.out.println("User ID: " + userId);   // ID dari JWT
        System.out.println("User Role: " + userRole); // Role dari JWT
        return message; // Kirim kembali ke client
    }
}