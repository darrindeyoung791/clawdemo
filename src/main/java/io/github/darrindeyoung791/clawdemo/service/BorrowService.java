package io.github.darrindeyoung791.clawdemo.service;

import io.github.darrindeyoung791.clawdemo.dao.BorrowRecordDao;
import io.github.darrindeyoung791.clawdemo.dao.EquipmentDao;
import io.github.darrindeyoung791.clawdemo.entity.BorrowRecord;
import io.github.darrindeyoung791.clawdemo.entity.Equipment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRecordDao borrowRecordDao;
    private final EquipmentDao equipmentDao;

    @Transactional
    public void submit(String userId, Long equipmentId, Integer quantity, LocalDateTime endTime) {
        Equipment equipment = equipmentDao.findById(equipmentId);
        if (equipment == null) throw new RuntimeException("Equipment not found");
        if (equipment.getAvailableQuantity() < quantity) {
            throw new RuntimeException("Insufficient available quantity");
        }
        BorrowRecord r = new BorrowRecord();
        r.setUserId(userId);
        r.setEquipmentId(equipmentId);
        r.setQuantity(quantity);
        r.setEndTime(endTime);
        r.setStatus("PENDING");
        borrowRecordDao.insert(r);
    }

    public List<BorrowRecord> listByUser(String userId, String status) {
        return borrowRecordDao.findByUser(userId, status);
    }

    public List<BorrowRecord> listAll(String status) {
        return borrowRecordDao.findAll(status);
    }

    @Transactional
    public void approve(Long id) {
        BorrowRecord r = borrowRecordDao.findById(id);
        if (r == null || !"PENDING".equals(r.getStatus())) {
            throw new RuntimeException("Invalid borrow record");
        }
        Equipment e = equipmentDao.findById(r.getEquipmentId());
        if (e.getAvailableQuantity() < r.getQuantity()) {
            throw new RuntimeException("Insufficient available quantity");
        }
        equipmentDao.updateQuantity(r.getEquipmentId(), -r.getQuantity());
        borrowRecordDao.updateStatusAndStartTime(id, "APPROVED");
    }

    @Transactional
    public void reject(Long id, String reason) {
        BorrowRecord r = borrowRecordDao.findById(id);
        if (r == null || !"PENDING".equals(r.getStatus())) {
            throw new RuntimeException("Invalid borrow record");
        }
        borrowRecordDao.updateStatus(id, "REJECTED", reason);
    }

    @Transactional
    public void returnEquipment(Long id, String userId) {
        BorrowRecord r = borrowRecordDao.findById(id);
        if (r == null || !"APPROVED".equals(r.getStatus())) {
            throw new RuntimeException("Invalid borrow record");
        }
        if (!r.getUserId().equals(userId)) {
            throw new RuntimeException("Not your borrow record");
        }
        equipmentDao.updateQuantity(r.getEquipmentId(), r.getQuantity());
        borrowRecordDao.updateStatus(id, "RETURNED", "");
    }
}
