package com.cyrcetech.backend.repository;

import com.cyrcetech.backend.domain.entity.AuditAction;
import com.cyrcetech.backend.domain.entity.AuditLog;
import com.cyrcetech.backend.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for AuditLog entity
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {

    /**
     * Find logs by user ID
     */
    List<AuditLog> findByUserIdOrderByTimestampDesc(String userId);

    /**
     * Find logs by username
     */
    List<AuditLog> findByUsernameOrderByTimestampDesc(String username);

    /**
     * Find logs by user role
     */
    List<AuditLog> findByUserRoleOrderByTimestampDesc(Role userRole);

    /**
     * Find logs by action type
     */
    List<AuditLog> findByActionOrderByTimestampDesc(AuditAction action);

    /**
     * Find logs by entity type
     */
    List<AuditLog> findByEntityTypeOrderByTimestampDesc(String entityType);

    /**
     * Find logs by entity type and entity ID
     */
    List<AuditLog> findByEntityTypeAndEntityIdOrderByTimestampDesc(String entityType, String entityId);

    /**
     * Find logs within a date range
     */
    @Query("SELECT a FROM AuditLog a WHERE a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
    List<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Find recent logs (last N entries)
     */
    @Query("SELECT a FROM AuditLog a ORDER BY a.timestamp DESC LIMIT :limit")
    List<AuditLog> findRecentLogs(@Param("limit") int limit);

    /**
     * Find logs by user and date range
     */
    @Query("SELECT a FROM AuditLog a WHERE a.userId = :userId AND a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
    List<AuditLog> findByUserAndDateRange(@Param("userId") String userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
