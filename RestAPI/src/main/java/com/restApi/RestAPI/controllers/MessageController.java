package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.MessageDTO;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.message.Messages;
import com.restApi.RestAPI.services.MessagesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessagesService messagesService;

    @GetMapping
    public ResponseEntity<List<Messages>> getAllMessagesByLogin(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String userRole = (String) request.getAttribute("userRole");

        List<Messages> messages = messagesService.getAllMessagesByUserId(userId, userRole);
        if (messages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Messages>> getAllMessagesById(@PathVariable Long userId, HttpServletRequest request) {
        String userRole = (String) request.getAttribute("userRole");
        System.out.println("userId: " + userId + ">>>>>>>>>>>");
        System.out.println("userRole: " + userRole + ">>>>>>>>>>>");

        List<Messages> messages = messagesService.getAllMessagesByUserId(userId, userRole);
        if (messages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTOOutput> createProduct(
            @RequestBody MessageDTO inputUser,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        String userRole = (String) request.getAttribute("userRole");

        ResponseDTOOutput response = messagesService.createMessage(inputUser, userId, userRole);
        if (!Objects.equals(response.getStatus(), "failed")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @MessageMapping("/responseMessage")
    @SendTo("/topic/responseMessage")
    public List<Messages> sendMessage(@Payload Map<String, Object> payload) {
        System.out.println("Payload received: " + payload);

        // Ambil nilai id dan role dari payload
        String senderIdString = payload.get("senderId").toString();
        Long senderId = Long.valueOf(senderIdString);
        String reciverIdString = payload.get("reciverId").toString();
        Long reciverId = Long.valueOf(reciverIdString);

        System.out.println("senderId: " + senderId);
        System.out.println("reciverId: " + reciverId);
        return messagesService.getAllMessagesRealtime(senderId, reciverId);
    }
}