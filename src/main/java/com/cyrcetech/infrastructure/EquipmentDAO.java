package com.cyrcetech.infrastructure;

import com.cyrcetech.entity.DeviceType;
import com.cyrcetech.entity.Equipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EquipmentDAO {

    public Equipment save(Equipment equipment) throws SQLException {
        String sql = "INSERT INTO equipment (id, brand, model, device_type, serial_number, physical_condition, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String id = equipment.id().isEmpty() ? java.util.UUID.randomUUID().toString() : equipment.id();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, equipment.brand());
            stmt.setString(3, equipment.model());
            stmt.setString(4, equipment.deviceType().name());
            stmt.setString(5, equipment.serialNumber());
            stmt.setString(6, equipment.physicalCondition());
            stmt.setString(7, equipment.customerId());
            stmt.executeUpdate();

            return new Equipment(id, equipment.brand(), equipment.model(), equipment.deviceType(),
                    equipment.serialNumber(), equipment.physicalCondition(), equipment.customerId());
        }
    }

    public List<Equipment> findByCustomerId(String customerId) throws SQLException {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM equipment WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    equipmentList.add(mapResultSetToEquipment(rs));
                }
            }
        }
        return equipmentList;
    }

    public Optional<Equipment> findById(String id) throws SQLException {
        String sql = "SELECT * FROM equipment WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEquipment(rs));
                }
            }
        }
        return Optional.empty();
    }

    private Equipment mapResultSetToEquipment(ResultSet rs) throws SQLException {
        return new Equipment(
                rs.getString("id"),
                rs.getString("brand"),
                rs.getString("model"),
                DeviceType.valueOf(rs.getString("device_type")),
                rs.getString("serial_number"),
                rs.getString("physical_condition"),
                rs.getString("customer_id"));
    }
}
