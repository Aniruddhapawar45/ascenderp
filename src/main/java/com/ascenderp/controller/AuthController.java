package com.ascenderp.controller;

import com.ascenderp.dto.LoginRequest;
import com.ascenderp.entity.User;
import com.ascenderp.security.JwtUtil;
import com.ascenderp.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

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

        Optional<User> userOptional =
                userService.findByUsername(request.getUsername());

        if (userOptional.isPresent()) {

            User user = userOptional.get();

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (encoder.matches(request.getPassword(), user.getPassword())) {
                return jwtUtil.generateToken(user.getUsername());
            }
        }

        return "Invalid Credentials";
    }
}