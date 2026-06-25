package io.github.darrindeyoung791.clawdemo.dao;

import io.github.darrindeyoung791.clawdemo.entity.BorrowRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BorrowRecordDao {

    private final JdbcTemplate jdbc;

    public List<BorrowRecord> findByUser(String userId, String status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM borrow_record WHERE user_id=?");
        List<Object> args = new ArrayList<>();
        args.add(userId);
        if (status != null && !status.isBlank()) {
            sql.append(" AND status=?");
            args.add(status);
        }
        sql.append(" ORDER BY created_at DESC");
        return jdbc.query(sql.toString(), new BeanPropertyRowMapper<>(BorrowRecord.class), args.toArray());
    }

    public List<BorrowRecord> findAll(String status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM borrow_record WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (status != null && !status.isBlank()) {
            sql.append(" AND status=?");
            args.add(status);
        }
        sql.append(" ORDER BY created_at DESC");
        return jdbc.query(sql.toString(), new BeanPropertyRowMapper<>(BorrowRecord.class), args.toArray());
    }

    public BorrowRecord findById(Long id) {
        List<BorrowRecord> list = jdbc.query("SELECT * FROM borrow_record WHERE id=?",
                new BeanPropertyRowMapper<>(BorrowRecord.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public void insert(BorrowRecord r) {
        jdbc.update("INSERT INTO borrow_record (user_id, equipment_id, quantity, end_time, status) VALUES (?, ?, ?, ?, ?)",
                r.getUserId(), r.getEquipmentId(), r.getQuantity(), r.getEndTime(), r.getStatus());
    }

    public void updateStatus(Long id, String status, String rejectReason) {
        jdbc.update("UPDATE borrow_record SET status=?, reject_reason=? WHERE id=?",
                status, rejectReason, id);
    }

    public void updateStatusAndStartTime(Long id, String status) {
        jdbc.update("UPDATE borrow_record SET status=?, start_time=NOW() WHERE id=?", status, id);
    }
}
