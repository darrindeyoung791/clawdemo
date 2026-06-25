package io.github.darrindeyoung791.clawdemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private String id;
    private String username;
    private String password;
    private String displayName;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
