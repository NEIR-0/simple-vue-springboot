package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.UserDTO;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") Integer page, // ingat ini endpoint jadi request params
            @RequestParam(value = "size", defaultValue = "10") Integer size // ingat ini endpoint jadi request params
    ) {
        return userService.getAllUsers(page, size);
    }
}
