package com.ascenderp.service;

import com.ascenderp.entity.Employee;
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

        // only keep the id reference if one was supplied — avoids trying
        // to cascade-create a brand new Employee row
        if (user.getEmployee() != null && user.getEmployee().getId() != null) {
            Employee employee = new Employee();
            employee.setId(user.getEmployee().getId());
            user.setEmployee(employee);
        } else {
            user.setEmployee(null);
        }

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        user.setPassword(
                encoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    // Lets an admin attach (or detach, with employeeId = null) an existing
    // user account to an employee record after the fact.
    public User linkEmployee(Long userId, Long employeeId) {
        User user = getUserById(userId);
        if (employeeId == null) {
            user.setEmployee(null);
        } else {
            Employee employee = new Employee();
            employee.setId(employeeId);
            user.setEmployee(employee);
        }
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