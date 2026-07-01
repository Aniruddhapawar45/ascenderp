package com.ascenderp.controller;

import com.ascenderp.dto.LoginRequest;
import com.ascenderp.entity.User;
import com.ascenderp.security.JwtUtil;
import com.ascenderp.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    public AuthController(JwtUtil jwtUtil,
                          UserService userService,
                          BCryptPasswordEncoder encoder) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.encoder = encoder;
    }

    // REGISTER (IMPORTANT FIX HERE)
    @PostMapping("/register")
    public User register(@RequestBody User user) {



        return userService.registerUser(user);
    }

    // LOGIN
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        Optional<User> userOptional =
                userService.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            return "Invalid Credentials";
        }

        User user = userOptional.get();

        // 🔥 COMPARE ENCRYPTED PASSWORD
        if (encoder.matches(request.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername());
        }

        return "Invalid Credentials";
    }
}
