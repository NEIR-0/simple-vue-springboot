package com.restApi.RestAPI.services;

import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.notification.Notifications;
import com.restApi.RestAPI.model.token.Tokens;
import com.restApi.RestAPI.repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsService {
    @Autowired
    NotificationsRepository notificationsRepository;

    public String createNotifications(Notifications notification) {
        notificationsRepository.save(notification);
        return "Berhasil create notification";
    }

    public List<Notifications> getAllUnreadNotifications(Long userId) {
        return notificationsRepository.getAllUnreadNotifications(userId);
    }

    public ResponseDTOOutput updateUnreadNotifications(Long userId) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        Notifications existingToken = notificationsRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("token not found"));

        existingToken.setIsRead(true);
        notificationsRepository.save(existingToken);

        responseStatus.setMsg("products updated successfully");
        responseStatus.setStatus("success");
        return responseStatus;
    }
}
