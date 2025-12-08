package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.domain.entity.AuditLog;
import com.cyrcetech.backend.domain.entity.Role;
import com.cyrcetech.backend.dto.response.AuditLogResponse;
import com.cyrcetech.backend.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for accessing audit logs.
 * Only accessible by ADMIN users.
 */
@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit Logs", description = "API for viewing system audit logs (Admin only)")
@PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @Operation(summary = "Get all audit logs")
    public ResponseEntity<List<AuditLogResponse>> getAllLogs() {
        List<AuditLogResponse> logs = auditLogService.getAllLogs().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get audit logs by user ID")
    public ResponseEntity<List<AuditLogResponse>> getLogsByUser(@PathVariable String userId) {
        List<AuditLogResponse> logs = auditLogService.getLogsByUser(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/entity/{entityType}")
    @Operation(summary = "Get audit logs by entity type")
    public ResponseEntity<List<AuditLogResponse>> getLogsByEntityType(@PathVariable String entityType) {
        List<AuditLogResponse> logs = auditLogService.getLogsByEntityType(entityType).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get audit logs by user role")
    public ResponseEntity<List<AuditLogResponse>> getLogsByRole(@PathVariable Role role) {
        List<AuditLogResponse> logs = auditLogService.getLogsByRole(role).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get audit logs within date range")
    public ResponseEntity<List<AuditLogResponse>> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AuditLogResponse> logs = auditLogService.getLogsByDateRange(startDate, endDate).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    private AuditLogResponse toResponse(AuditLog log) {
        AuditLogResponse response = new AuditLogResponse();
        response.setId(log.getId());
        response.setUserId(log.getUserId());
        response.setUsername(log.getUsername());
        response.setUserRole(log.getUserRole());
        response.setUserRoleDisplayName(log.getUserRole() != null ? log.getUserRole().name() : null);
        response.setAction(log.getAction());
        response.setActionDisplayName(log.getAction() != null ? log.getAction().getDisplayName() : null);
        response.setEntityType(log.getEntityType());
        response.setEntityId(log.getEntityId());
        response.setTimestamp(log.getTimestamp() != null ? log.getTimestamp().format(FORMATTER) : null);
        response.setDetails(log.getDetails());
        response.setIpAddress(log.getIpAddress());
        return response;
    }
}
