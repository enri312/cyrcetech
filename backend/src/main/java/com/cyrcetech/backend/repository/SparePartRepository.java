package com.cyrcetech.backend.repository;

import com.cyrcetech.backend.domain.entity.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for SparePart entity
 */
@Repository
public interface SparePartRepository extends JpaRepository<SparePart, String> {

    /**
     * Find spare parts with low stock (less than 5 units)
     */
    @Query("SELECT s FROM SparePart s WHERE s.stock > 0 AND s.stock < 5")
    List<SparePart> findLowStock();

    /**
     * Find spare parts out of stock
     */
    @Query("SELECT s FROM SparePart s WHERE s.stock = 0")
    List<SparePart> findOutOfStock();

    /**
     * Find spare parts in stock
     */
    @Query("SELECT s FROM SparePart s WHERE s.stock > 0")
    List<SparePart> findInStock();

    /**
     * Search spare parts by name or provider
     */
    @Query("SELECT s FROM SparePart s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.provider) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<SparePart> searchSpareParts(@Param("searchTerm") String searchTerm);
}
