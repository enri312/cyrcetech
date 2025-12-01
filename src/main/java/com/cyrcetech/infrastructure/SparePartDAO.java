package com.cyrcetech.infrastructure;

import com.cyrcetech.entity.SparePart;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SparePartDAO {

    public SparePart save(SparePart sparePart) throws SQLException {
        String sql = "INSERT INTO spare_parts (id, name, price, stock, provider) VALUES (?, ?, ?, ?, ?)";
        String id = sparePart.id().isEmpty() ? java.util.UUID.randomUUID().toString() : sparePart.id();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, sparePart.name());
            stmt.setDouble(3, sparePart.price());
            stmt.setInt(4, sparePart.stock());
            stmt.setString(5, sparePart.provider());
            stmt.executeUpdate();

            return new SparePart(id, sparePart.name(), sparePart.price(), sparePart.stock(), sparePart.provider());
        }
    }

    public void update(SparePart sparePart) throws SQLException {
        String sql = "UPDATE spare_parts SET name = ?, price = ?, stock = ?, provider = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sparePart.name());
            stmt.setDouble(2, sparePart.price());
            stmt.setInt(3, sparePart.stock());
            stmt.setString(4, sparePart.provider());
            stmt.setString(5, sparePart.id());
            stmt.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM spare_parts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public Optional<SparePart> findById(String id) throws SQLException {
        String sql = "SELECT * FROM spare_parts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSparePart(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<SparePart> findAll() throws SQLException {
        List<SparePart> spareParts = new ArrayList<>();
        String sql = "SELECT * FROM spare_parts";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                spareParts.add(mapResultSetToSparePart(rs));
            }
        }
        return spareParts;
    }

    private SparePart mapResultSetToSparePart(ResultSet rs) throws SQLException {
        return new SparePart(
                rs.getString("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("stock"),
                rs.getString("provider"));
    }
}
