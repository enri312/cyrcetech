package com.cyrcetech.usecase;

import com.cyrcetech.entity.Equipment;
import java.util.List;

public interface EquipmentService {
    void createEquipment(Equipment equipment);

    List<Equipment> getEquipmentByCustomerId(String customerId);

    Equipment getEquipmentById(String id);

    void updateEquipment(Equipment equipment);

    void deleteEquipment(String id);

    List<Equipment> getAllEquipment();
}
