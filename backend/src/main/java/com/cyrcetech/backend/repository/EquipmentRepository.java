package com.cyrcetech.backend.repository;

import com.cyrcetech.backend.domain.entity.DeviceType;
import com.cyrcetech.backend.domain.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Equipment entity
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {

    /**
     * Find all equipment for a specific customer
     */
    List<Equipment> findByCustomerId(String customerId);

    /**
     * Find equipment by device type
     */
    List<Equipment> findByDeviceType(DeviceType deviceType);

    /**
     * Search equipment by brand, model, or serial number
     */
    @Query("SELECT e FROM Equipment e WHERE " +
            "LOWER(e.brand) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.model) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.serialNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Equipment> searchEquipment(@Param("searchTerm") String searchTerm);
}
