package io.github.darrindeyoung791.clawdemo.service;

import io.github.darrindeyoung791.clawdemo.dao.EquipmentDao;
import io.github.darrindeyoung791.clawdemo.entity.Equipment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentDao equipmentDao;

    public List<Equipment> listAll(String keyword, String category) {
        return equipmentDao.findAll(keyword, category);
    }

    public Equipment getById(Long id) {
        return equipmentDao.findById(id);
    }

    public void add(Equipment e) {
        e.setCode(UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
        e.setAvailableQuantity(e.getTotalQuantity());
        e.setStatus("AVAILABLE");
        equipmentDao.insert(e);
    }

    public void update(Equipment e) {
        Equipment existing = equipmentDao.findById(e.getId());
        if (existing == null) throw new RuntimeException("Equipment not found");
        e.setCode(existing.getCode());
        e.setAvailableQuantity(existing.getAvailableQuantity());
        equipmentDao.update(e);
    }

    public void delete(Long id) {
        if (equipmentDao.hasActiveBorrows(id)) {
            throw new RuntimeException("Equipment has active borrow records");
        }
        equipmentDao.deleteById(id);
    }

    public List<String> listCategories() {
        return List.of("INSTRUMENT", "CONSUMABLE", "SOFTWARE", "OTHER");
    }
}
