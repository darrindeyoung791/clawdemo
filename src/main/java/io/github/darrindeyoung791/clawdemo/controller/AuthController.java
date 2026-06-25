package io.github.darrindeyoung791.clawdemo.controller;

import io.github.darrindeyoung791.clawdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String displayName = body.get("displayName");
        if (username == null || password == null || displayName == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
        }
        if (username.length() <= 2) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username must be longer than 2 characters"));
        }
        if (password.length() <= 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password must be longer than 6 characters"));
        }
        try {
            userService.register(username, password, displayName);
            return ResponseEntity.ok(Map.of("message", "Registration successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
        }
        try {
            return ResponseEntity.ok(userService.login(username, password));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}
