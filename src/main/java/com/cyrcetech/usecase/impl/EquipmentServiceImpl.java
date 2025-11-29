package com.cyrcetech.usecase.impl;

import com.cyrcetech.entity.Equipment;
import com.cyrcetech.infrastructure.EquipmentDAO;
import com.cyrcetech.usecase.EquipmentService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentDAO equipmentDAO = new EquipmentDAO();

    @Override
    public void createEquipment(Equipment equipment) {
        try {
            equipmentDAO.save(equipment);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating equipment", e);
        }
    }

    @Override
    public List<Equipment> getEquipmentByCustomerId(String customerId) {
        try {
            return equipmentDAO.findByCustomerId(customerId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Equipment getEquipmentById(String id) {
        try {
            return equipmentDAO.findById(id).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
