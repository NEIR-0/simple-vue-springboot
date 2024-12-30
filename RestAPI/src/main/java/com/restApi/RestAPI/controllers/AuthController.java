package com.restApi.RestAPI.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.services.RabbitMQSenderService;
import com.restApi.RestAPI.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private RabbitMQSenderService rabbitMQSenderService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTOOutput> createUser(@Valid @RequestBody Users inputUser, BindingResult result) {
        if (result.hasErrors()) {
            // Kumpulkan semua pesan error
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append("\n");
            });

            // Buat response DTO output untuk error
            ResponseDTOOutput errorResponse = new ResponseDTOOutput();
            errorResponse.setStatus("failed");
            errorResponse.setMsg(errorMessages.toString());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        ResponseDTOOutput checkEmail = userService.emailChecker(inputUser.getEmail());
        if (!Objects.equals(checkEmail.getStatus(), "failed")) {
            try {
                rabbitMQSenderService.sendMessageForRegisterUser(inputUser);
            } catch (JsonProcessingException e) {
                System.out.println("Error RabbitMQ Transaction: " + e.getMessage());
                checkEmail.setMsg("Failed to process transaction.");
                checkEmail.setStatus("failed");
            }
            return ResponseEntity.ok(checkEmail);
        } else {
            return ResponseEntity.badRequest().body(checkEmail);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTOOutput> login(@RequestBody Users inputUser) {
        ResponseDTOOutput loginResult = userService.findByEmail(inputUser);
        if (!Objects.equals(loginResult.getStatus(), "failed")) {
            return ResponseEntity.ok(loginResult);
        } else {
            return ResponseEntity.badRequest().body(loginResult);
        }
    }
}
