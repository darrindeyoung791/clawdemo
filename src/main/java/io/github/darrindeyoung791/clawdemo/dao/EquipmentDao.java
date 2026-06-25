package io.github.darrindeyoung791.clawdemo.dao;

import io.github.darrindeyoung791.clawdemo.entity.Equipment;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EquipmentDao {

    private final JdbcTemplate jdbc;

    public List<Equipment> findAll(String keyword, String category) {
        StringBuilder sql = new StringBuilder("SELECT * FROM equipment WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (name LIKE ? OR code LIKE ?)");
            args.add("%" + keyword + "%");
            args.add("%" + keyword + "%");
        }
        if (category != null && !category.isBlank()) {
            sql.append(" AND category = ?");
            args.add(category);
        }
        sql.append(" ORDER BY created_at DESC");
        return jdbc.query(sql.toString(), new BeanPropertyRowMapper<>(Equipment.class), args.toArray());
    }

    public Equipment findById(Long id) {
        List<Equipment> list = jdbc.query("SELECT * FROM equipment WHERE id = ?",
                new BeanPropertyRowMapper<>(Equipment.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public void insert(Equipment e) {
        jdbc.update("INSERT INTO equipment (name, code, category, location, total_quantity, available_quantity, status) VALUES (?, ?, ?, ?, ?, ?, ?)",
                e.getName(), e.getCode(), e.getCategory(), e.getLocation(), e.getTotalQuantity(), e.getAvailableQuantity(), e.getStatus());
    }

    public void update(Equipment e) {
        jdbc.update("UPDATE equipment SET name=?, category=?, location=?, total_quantity=? WHERE id=?",
                e.getName(), e.getCategory(), e.getLocation(), e.getTotalQuantity(), e.getId());
    }

    public void updateQuantity(Long id, int delta) {
        jdbc.update("UPDATE equipment SET available_quantity = available_quantity + ? WHERE id=?", delta, id);
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM equipment WHERE id=?", id);
    }

    public boolean hasActiveBorrows(Long equipmentId) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM borrow_record WHERE equipment_id=? AND status IN ('PENDING','APPROVED')",
                Integer.class, equipmentId);
        return count != null && count > 0;
    }
}
