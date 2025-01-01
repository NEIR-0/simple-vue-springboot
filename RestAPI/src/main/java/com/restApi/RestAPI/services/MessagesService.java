package com.restApi.RestAPI.services;

import com.restApi.RestAPI.dto.MessageDTO;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.model.message.Messages;
import com.restApi.RestAPI.repository.MessagesRepository;
import com.restApi.RestAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MessagesService {
    @Autowired
    MessagesRepository messagesRepository;

    @Autowired
    UserRepository userRepository;

    public List<Messages> getAllMessagesByUserId(Long userId, String userRole) {
        if (!Objects.equals(userRole, "admin")) {
            Users admin = userRepository.findByRole("admin")
                    .orElseThrow(() -> new RuntimeException("Admin user not found"));
            return messagesRepository.findAllMessagesByUserId(userId);
        } else {
            Users admin = userRepository.findByRole("admin")
                    .orElseThrow(() -> new RuntimeException("Admin user not found"));
            return messagesRepository.findAllMessagesByUserIdAndAdminId(userId, admin.getId());
        }
    }

    public List<Messages> getAllMessagesRealtime(Long senderId, Long reciverId) {
        return messagesRepository.findAllMessagesByUserIdAndAdminId(senderId, reciverId);
    }

    public ResponseDTOOutput createMessage(MessageDTO inputUser, Long userId, String userRole) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        // Validate input parameters
        if (inputUser == null) {
            throw new IllegalArgumentException("Invalid input Messages");
        }

        // Create new product object
        Messages newMessages = new Messages();
        newMessages.setContent(inputUser.getContent());
        if(!Objects.equals(userRole, "admin")){
            newMessages.setSender(userRole);
        }else {
            newMessages.setSender("admin");
        }

        if(inputUser.getSendTo() == null){
            Optional<Users> user = userRepository.findById(userId);
            newMessages.setSenderId(user.orElse(null));

            Optional<Users> admin = userRepository.findByRole("admin");
            newMessages.setReceiverId(admin.orElse(null));

            responseStatus.setMsg(user.get().getId() + "-" + admin.get().getId());
        }else{
            Optional<Users> admin = userRepository.findById(userId);
            newMessages.setSenderId(admin.orElse(null));

            Optional<Users> user = userRepository.findById(inputUser.getSendTo());
            newMessages.setReceiverId(user.orElse(null));

            responseStatus.setMsg(admin.get().getId() + "-" + user.get().getId());
        }

        messagesRepository.save(newMessages);

        responseStatus.setStatus("success");

        return responseStatus;
    }
}
