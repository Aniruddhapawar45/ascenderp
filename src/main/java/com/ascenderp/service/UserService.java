package com.ascenderp.service;

import com.ascenderp.entity.User;
import com.ascenderp.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        user.setPassword(
                encoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}