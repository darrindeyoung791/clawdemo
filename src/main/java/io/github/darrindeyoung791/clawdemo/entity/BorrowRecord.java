package io.github.darrindeyoung791.clawdemo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BorrowRecord {
    private Long id;
    private String userId;
    private Long equipmentId;
    private Integer quantity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String rejectReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
