package com.ascenderp.controller;

import com.ascenderp.entity.User;
import com.ascenderp.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();

    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Tells the frontend who's signed in right now: username, role, and
    // (if linked) which employee record they correspond to. Replaces the
    // old "try an admin-only endpoint and see if it 403s" role-detection hack.
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Map<String, Object> getCurrentUser(Authentication authentication) {

        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("employeeId", user.getEmployee() != null ? user.getEmployee().getId() : null);
        response.put("employeeName", user.getEmployee() != null ? user.getEmployee().getName() : null);

        return response;
    }

    // body: { "employeeId": 5 } — pass employeeId: null to unlink
    @PutMapping("/{id}/link-employee")
    @PreAuthorize("hasRole('ADMIN')")
    public User linkEmployee(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        return userService.linkEmployee(id, body.get("employeeId"));
    }
}