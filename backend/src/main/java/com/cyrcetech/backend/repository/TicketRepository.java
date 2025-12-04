package com.cyrcetech.backend.repository;

import com.cyrcetech.backend.domain.entity.Ticket;
import com.cyrcetech.backend.domain.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Ticket entity
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    /**
     * Find all tickets for a specific customer
     */
    List<Ticket> findByCustomerId(String customerId);

    /**
     * Find all tickets for a specific equipment
     */
    List<Ticket> findByEquipmentId(String equipmentId);

    /**
     * Find tickets by status
     */
    List<Ticket> findByStatus(TicketStatus status);

    /**
     * Find active tickets (not delivered or cancelled)
     */
    @Query("SELECT t FROM Ticket t WHERE t.status NOT IN ('DELIVERED', 'CANCELLED')")
    List<Ticket> findActiveTickets();

    /**
     * Search tickets by customer name, equipment details, or problem description
     */
    @Query("SELECT t FROM Ticket t WHERE " +
            "LOWER(t.customer.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.equipment.brand) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.equipment.model) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.problemDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Ticket> searchTickets(@Param("searchTerm") String searchTerm);
}
