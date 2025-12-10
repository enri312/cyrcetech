package com.cyrcetech.backend.service;

import com.cyrcetech.backend.domain.entity.AuditAction;
import com.cyrcetech.backend.domain.entity.AuditLog;
import com.cyrcetech.backend.domain.entity.Role;
import com.cyrcetech.backend.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for audit logging.
 * Provides methods to log user actions throughout the system.
 */
@Service
@Transactional
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Log an action performed by the current authenticated user.
     *
     * @param action     The type of action performed
     * @param entityType The type of entity affected (e.g., "Customer", "Ticket")
     * @param entityId   The ID of the affected entity (null for list/search
     *                   operations)
     * @param details    Additional details about the action
     */
    public void logAction(AuditAction action, String entityType, String entityId, String details) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String userId = null;
            String username = "anonymous";
            Role userRole = null;

            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                username = auth.getName();
                // Try to extract role from authorities
                userRole = auth.getAuthorities().stream()
                        .filter(a -> a.getAuthority().startsWith("ROLE_"))
                        .map(a -> {
                            String roleName = a.getAuthority(); // Keep full ROLE_ADMIN format
                            try {
                                return Role.valueOf(roleName);
                            } catch (IllegalArgumentException e) {
                                log.debug("Could not parse role: {}", roleName);
                                return null;
                            }
                        })
                        .findFirst()
                        .orElse(null);
            }

            AuditLog auditLog = new AuditLog(userId, username, userRole, action, entityType, entityId, details);
            auditLogRepository.save(auditLog);

            log.debug("Audit log created: {} {} {} by {}", action, entityType, entityId, username);
        } catch (Exception e) {
            log.error("Failed to create audit log: {}", e.getMessage());
            // Don't throw - audit logging should not break main functionality
        }
    }

    /**
     * Log a login action - special case since SecurityContext is not yet available.
     *
     * @param username The username that logged in
     * @param role     The user's role
     * @param userId   The user's ID
     */
    public void logLogin(String username, Role role, String userId) {
        try {
            AuditLog auditLog = new AuditLog(userId, username, role, AuditAction.LOGIN, "User", userId,
                    "Inicio de sesión exitoso");
            auditLogRepository.save(auditLog);
            log.info("Login audit log created for user: {}", username);
        } catch (Exception e) {
            log.error("Failed to create login audit log: {}", e.getMessage());
        }
    }

    /**
     * Log a list/view all action.
     */
    public void logList(String entityType) {
        logAction(AuditAction.LIST, entityType, null, "Listed all " + entityType.toLowerCase() + "s");
    }

    /**
     * Log a view single entity action.
     */
    public void logView(String entityType, String entityId) {
        logAction(AuditAction.VIEW, entityType, entityId, "Viewed " + entityType.toLowerCase() + " details");
    }

    /**
     * Log a search action.
     */
    public void logSearch(String entityType, String searchTerm) {
        logAction(AuditAction.SEARCH, entityType, null, "Search: " + searchTerm);
    }

    /**
     * Log a create action.
     */
    public void logCreate(String entityType, String entityId) {
        logAction(AuditAction.CREATE, entityType, entityId, "Created new " + entityType.toLowerCase());
    }

    /**
     * Log an update action.
     */
    public void logUpdate(String entityType, String entityId) {
        logAction(AuditAction.UPDATE, entityType, entityId, "Updated " + entityType.toLowerCase());
    }

    /**
     * Log a delete action.
     */
    public void logDelete(String entityType, String entityId) {
        logAction(AuditAction.DELETE, entityType, entityId, "Deleted " + entityType.toLowerCase());
    }

    /**
     * Log an export action.
     */
    public void logExport(String entityType, String format) {
        AuditAction action = "PDF".equalsIgnoreCase(format) ? AuditAction.EXPORT_PDF : AuditAction.EXPORT_EXCEL;
        logAction(action, entityType, null, "Exported " + entityType.toLowerCase() + " to " + format);
    }

    /**
     * Get all audit logs.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    /**
     * Get logs by user.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByUser(String userId) {
        return auditLogRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    /**
     * Get logs by entity type.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByEntityType(String entityType) {
        return auditLogRepository.findByEntityTypeOrderByTimestampDesc(entityType);
    }

    /**
     * Get logs within date range.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByDateRange(startDate, endDate);
    }

    /**
     * Get logs by user role.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByRole(Role role) {
        return auditLogRepository.findByUserRoleOrderByTimestampDesc(role);
    }
}
