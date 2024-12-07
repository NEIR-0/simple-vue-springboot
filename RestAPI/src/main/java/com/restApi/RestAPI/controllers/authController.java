package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class authController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody Users inputUser, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            result.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append("\n");
            });
            return ResponseEntity.badRequest().body(errorMessages.toString());
        }

        userService.createUser(inputUser);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users inputUser) {
        String loginResult = userService.findByEmail(inputUser);
        if (loginResult.startsWith("Bearer ")) {
            return ResponseEntity.ok(loginResult);
        } else {
            return ResponseEntity.badRequest().body(loginResult);

        }
    }
}
