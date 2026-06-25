package io.github.darrindeyoung791.clawdemo.controller;

import io.github.darrindeyoung791.clawdemo.entity.Equipment;
import io.github.darrindeyoung791.clawdemo.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(equipmentService.listAll(keyword, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Equipment equipment = equipmentService.getById(id);
        if (equipment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> listCategories() {
        return ResponseEntity.ok(equipmentService.listCategories());
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Equipment equipment) {
        equipmentService.add(equipment);
        return ResponseEntity.ok(equipment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Equipment equipment) {
        equipment.setId(id);
        equipmentService.update(equipment);
        return ResponseEntity.ok(equipment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            equipmentService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
