package com.ascenderp.controller;

import com.ascenderp.dto.LoginRequest;
import com.ascenderp.entity.User;
import com.ascenderp.security.JwtUtil;
import com.ascenderp.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(JwtUtil jwtUtil,
                          UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        if ("admin".equals(request.getUsername())
                && "admin123".equals(request.getPassword())) {

            return jwtUtil.generateToken(request.getUsername());
        }

        return "Invalid Credentials";
    }
}