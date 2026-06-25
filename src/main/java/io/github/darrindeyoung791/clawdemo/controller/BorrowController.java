package io.github.darrindeyoung791.clawdemo.controller;

import io.github.darrindeyoung791.clawdemo.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    private String currentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping
    public ResponseEntity<?> submit(@RequestBody Map<String, Object> body) {
        try {
            Long equipmentId = Long.valueOf(body.get("equipmentId").toString());
            Integer quantity = Integer.valueOf(body.get("quantity").toString());
            LocalDateTime endTime = null;
            if (body.containsKey("endTime") && body.get("endTime") != null) {
                endTime = LocalDateTime.parse(body.get("endTime").toString());
            }
            borrowService.submit(currentUserId(), equipmentId, quantity, endTime);
            return ResponseEntity.ok(Map.of("message", "Borrow request submitted"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> listMyBorrows(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(borrowService.listByUser(currentUserId(), status));
    }

    @GetMapping("/admin")
    public ResponseEntity<?> listAll(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(borrowService.listAll(status));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        try {
            borrowService.approve(id);
            return ResponseEntity.ok(Map.of("message", "Approved"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            borrowService.reject(id, body.getOrDefault("reason", ""));
            return ResponseEntity.ok(Map.of("message", "Rejected"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<?> returnEquipment(@PathVariable Long id) {
        try {
            borrowService.returnEquipment(id, currentUserId());
            return ResponseEntity.ok(Map.of("message", "Returned"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
