package io.github.darrindeyoung791.clawdemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Equipment {
    private Long id;
    private String name;
    private String code;
    private String category;
    private String location;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
