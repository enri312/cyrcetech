package com.cyrcetech.infrastructure;

import com.cyrcetech.entity.Invoice;
import com.cyrcetech.entity.PaymentMethod;
import com.cyrcetech.entity.PaymentStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceDAO {

    public Invoice save(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO invoices (id, ticket_id, invoice_number, issue_date, due_date, subtotal, tax_amount, total_amount, payment_status, payment_date, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String id = invoice.id().isEmpty() ? java.util.UUID.randomUUID().toString() : invoice.id();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, invoice.ticketId());
            stmt.setString(3, invoice.invoiceNumber());
            stmt.setDate(4, Date.valueOf(invoice.issueDate()));
            stmt.setDate(5, invoice.dueDate() != null ? Date.valueOf(invoice.dueDate()) : null);
            stmt.setDouble(6, invoice.subtotal());
            stmt.setDouble(7, invoice.taxAmount());
            stmt.setDouble(8, invoice.totalAmount());
            stmt.setString(9, invoice.paymentStatus().name());
            stmt.setDate(10, invoice.paymentDate() != null ? Date.valueOf(invoice.paymentDate()) : null);
            stmt.setString(11, invoice.paymentMethod() != null ? invoice.paymentMethod().name() : null);
            stmt.setString(12, invoice.notes());
            stmt.executeUpdate();

            return new Invoice(id, invoice.ticketId(), invoice.invoiceNumber(), invoice.issueDate(),
                    invoice.dueDate(), invoice.subtotal(), invoice.taxAmount(), invoice.totalAmount(),
                    invoice.paymentStatus(), invoice.paymentDate(), invoice.paymentMethod(), invoice.notes());
        }
    }

    public void update(Invoice invoice) throws SQLException {
        String sql = "UPDATE invoices SET ticket_id = ?, invoice_number = ?, issue_date = ?, due_date = ?, subtotal = ?, tax_amount = ?, total_amount = ?, payment_status = ?, payment_date = ?, payment_method = ?, notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, invoice.ticketId());
            stmt.setString(2, invoice.invoiceNumber());
            stmt.setDate(3, Date.valueOf(invoice.issueDate()));
            stmt.setDate(4, invoice.dueDate() != null ? Date.valueOf(invoice.dueDate()) : null);
            stmt.setDouble(5, invoice.subtotal());
            stmt.setDouble(6, invoice.taxAmount());
            stmt.setDouble(7, invoice.totalAmount());
            stmt.setString(8, invoice.paymentStatus().name());
            stmt.setDate(9, invoice.paymentDate() != null ? Date.valueOf(invoice.paymentDate()) : null);
            stmt.setString(10, invoice.paymentMethod() != null ? invoice.paymentMethod().name() : null);
            stmt.setString(11, invoice.notes());
            stmt.setString(12, invoice.id());
            stmt.executeUpdate();
        }
    }

    public Optional<Invoice> findById(String id) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToInvoice(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Invoice> findByTicketId(String ticketId) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE ticket_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        }
        return invoices;
    }

    public List<Invoice> findAll() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices ORDER BY issue_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        }
        return invoices;
    }

    public String generateInvoiceNumber() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM invoices WHERE EXTRACT(YEAR FROM issue_date) = EXTRACT(YEAR FROM CURRENT_DATE)";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int count = rs.getInt("count") + 1;
                int year = LocalDate.now().getYear();
                return String.format("INV-%d-%03d", year, count);
            }
        }
        return "INV-" + LocalDate.now().getYear() + "-001";
    }

    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Date dueDate = rs.getDate("due_date");
        Date paymentDate = rs.getDate("payment_date");
        String paymentMethodStr = rs.getString("payment_method");

        return new Invoice(
                rs.getString("id"),
                rs.getString("ticket_id"),
                rs.getString("invoice_number"),
                rs.getDate("issue_date").toLocalDate(),
                dueDate != null ? dueDate.toLocalDate() : null,
                rs.getDouble("subtotal"),
                rs.getDouble("tax_amount"),
                rs.getDouble("total_amount"),
                PaymentStatus.valueOf(rs.getString("payment_status")),
                paymentDate != null ? paymentDate.toLocalDate() : null,
                paymentMethodStr != null ? PaymentMethod.valueOf(paymentMethodStr) : null,
                rs.getString("notes"));
    }
}
