package com.cyrcetech.backend.repository;

import com.cyrcetech.backend.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Customer entity.
 * Provides CRUD operations and custom queries.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    /**
     * Search customers by name, tax ID, or phone
     */
    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.taxId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Customer> searchCustomers(@Param("searchTerm") String searchTerm);

    /**
     * Find customer by tax ID
     */
    Customer findByTaxId(String taxId);

    /**
     * Check if customer exists by tax ID
     */
    boolean existsByTaxId(String taxId);
}
