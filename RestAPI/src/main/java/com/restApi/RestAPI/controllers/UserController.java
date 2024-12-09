package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.config.JwtUtil;
import com.restApi.RestAPI.dto.UserDTO;
import com.restApi.RestAPI.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<UserDTO> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "search", required = false) String search,
            HttpServletRequest request
    ) {
        // Ambil header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Ambil token setelah "Bearer "
            String email = jwtUtil.getEmailFromToken(token);
            Long id = jwtUtil.getIdFromToken(token);

            // Log atau gunakan email dan ID
            System.out.println("User login dengan email: " + email + " dan ID: " + id);
        }
        return userService.getAllUsers(page, size, search);
    }
}
