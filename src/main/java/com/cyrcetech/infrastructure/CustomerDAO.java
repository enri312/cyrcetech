package com.cyrcetech.infrastructure;

import com.cyrcetech.entity.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAO {

    public Customer save(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (id, name, tax_id, address, phone) VALUES (?, ?, ?, ?, ?)";
        // If ID is empty, generate one or let DB handle it. For now assuming UUID
        // generation in Java or DB serial.
        // Let's assume we generate UUID here if empty.
        String id = customer.id().isEmpty() ? java.util.UUID.randomUUID().toString() : customer.id();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, customer.name());
            stmt.setString(3, customer.taxId());
            stmt.setString(4, customer.address());
            stmt.setString(5, customer.phone());
            stmt.executeUpdate();

            return new Customer(id, customer.name(), customer.taxId(), customer.address(), customer.phone());
        }
    }

    public void update(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET name = ?, tax_id = ?, address = ?, phone = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.name());
            stmt.setString(2, customer.taxId());
            stmt.setString(3, customer.address());
            stmt.setString(4, customer.phone());
            stmt.setString(5, customer.id());
            stmt.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public Optional<Customer> findById(String id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        return customers;
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("tax_id"),
                rs.getString("address"),
                rs.getString("phone"));
    }
}
