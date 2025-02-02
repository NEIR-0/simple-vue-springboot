package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.NotificationDTO;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.services.NotificationsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notification")
public class NotificationsController {
    @Autowired
    NotificationsService notificationsService;

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getAllUnreadNotifications(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("userId: " + userId + "??????");
        List<NotificationDTO> notifications = notificationsService.getAllUnreadNotifications(userId);
        if (notifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/update-unread/{notifId}")
    public ResponseEntity<ResponseDTOOutput> updateUnreadNotifications(
            @PathVariable Long notifId
    ) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        ResponseDTOOutput response =  notificationsService.updateUnreadNotifications(notifId);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else {
            responseStatus.setMsg("failed due create token");
            responseStatus.setStatus("failed");
            return ResponseEntity.badRequest().body(responseStatus);
        }
    }

    @MessageMapping("/responseNotifications")
    @SendTo("/topic/responseNotifications")
    public List<NotificationDTO> sendNotifications(@Payload Map<String, Object> payload) {
        System.out.println("Payload received: " + payload);
        String userIdString = payload.get("userId").toString();
        Long userId = Long.valueOf(userIdString);
        System.out.println("userId: " + userId + "??????");
        return notificationsService.getAllUnreadNotifications(userId);
    }
}
