package com.cyrcetech.infrastructure;

import com.cyrcetech.entity.Ticket;
import com.cyrcetech.entity.TicketStatus;
import com.cyrcetech.entity.Customer;
import com.cyrcetech.entity.Equipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDAO {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final EquipmentDAO equipmentDAO = new EquipmentDAO();

    public Ticket save(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO tickets (id, customer_id, equipment_id, problem_description, observations, status, amount_paid, estimated_cost, date_created, ai_diagnosis) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String id = ticket.getId() == null || ticket.getId().isEmpty() ? java.util.UUID.randomUUID().toString()
                : ticket.getId();
        ticket.setId(id);

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, ticket.getCustomer().id());
            stmt.setString(3, ticket.getEquipment().id());
            stmt.setString(4, ticket.getProblemDescription());
            stmt.setString(5, ticket.getObservations());
            stmt.setString(6, ticket.getStatus().name());
            stmt.setDouble(7, ticket.getAmountPaid());
            stmt.setDouble(8, ticket.getEstimatedCost());
            stmt.setDate(9, Date.valueOf(ticket.getDateCreated()));
            stmt.setString(10, ticket.getAiDiagnosis());
            stmt.executeUpdate();

            return ticket;
        }
    }

    public List<Ticket> findAll() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getString("id"));
        ticket.setProblemDescription(rs.getString("problem_description"));
        ticket.setObservations(rs.getString("observations"));
        ticket.setStatus(TicketStatus.valueOf(rs.getString("status")));
        ticket.setAmountPaid(rs.getDouble("amount_paid"));
        ticket.setEstimatedCost(rs.getDouble("estimated_cost"));
        ticket.setDateCreated(rs.getDate("date_created").toLocalDate());
        ticket.setAiDiagnosis(rs.getString("ai_diagnosis"));

        // Fetch related entities
        Optional<Customer> customer = customerDAO.findById(rs.getString("customer_id"));
        customer.ifPresent(ticket::setCustomer);

        Optional<Equipment> equipment = equipmentDAO.findById(rs.getString("equipment_id"));
        equipment.ifPresent(ticket::setEquipment);

        return ticket;
    }
}
