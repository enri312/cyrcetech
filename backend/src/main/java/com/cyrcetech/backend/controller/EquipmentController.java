package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.domain.entity.DeviceType;
import com.cyrcetech.backend.dto.request.CreateEquipmentRequest;
import com.cyrcetech.backend.dto.request.UpdateEquipmentRequest;
import com.cyrcetech.backend.dto.response.EquipmentResponse;
import com.cyrcetech.backend.service.AuditLogService;
import com.cyrcetech.backend.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Equipment management
 */
@RestController
@RequestMapping("/api/equipment")
@Tag(name = "Equipment", description = "Equipment management APIs")
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final AuditLogService auditLogService;

    public EquipmentController(EquipmentService equipmentService, AuditLogService auditLogService) {
        this.equipmentService = equipmentService;
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get all equipment", description = "Retrieve a list of all equipment")
    public ResponseEntity<List<EquipmentResponse>> getAllEquipment() {
        List<EquipmentResponse> equipment = equipmentService.getAllEquipment();
        auditLogService.logList("Equipment");
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get equipment by ID", description = "Retrieve equipment by its ID")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable String id) {
        EquipmentResponse equipment = equipmentService.getEquipmentById(id);
        auditLogService.logView("Equipment", id);
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get equipment by customer", description = "Retrieve all equipment for a specific customer")
    public ResponseEntity<List<EquipmentResponse>> getEquipmentByCustomerId(@PathVariable String customerId) {
        List<EquipmentResponse> equipment = equipmentService.getEquipmentByCustomerId(customerId);
        auditLogService.logSearch("Equipment", "customerId=" + customerId);
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/type/{deviceType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get equipment by device type", description = "Retrieve equipment by device type")
    public ResponseEntity<List<EquipmentResponse>> getEquipmentByDeviceType(@PathVariable DeviceType deviceType) {
        List<EquipmentResponse> equipment = equipmentService.getEquipmentByDeviceType(deviceType);
        auditLogService.logSearch("Equipment", "deviceType=" + deviceType);
        return ResponseEntity.ok(equipment);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Create new equipment", description = "Create a new equipment entry")
    public ResponseEntity<EquipmentResponse> createEquipment(@Valid @RequestBody CreateEquipmentRequest request) {
        EquipmentResponse created = equipmentService.createEquipment(request);
        auditLogService.logCreate("Equipment", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Update equipment", description = "Update an existing equipment entry")
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable String id,
            @Valid @RequestBody UpdateEquipmentRequest request) {
        EquipmentResponse updated = equipmentService.updateEquipment(id, request);
        auditLogService.logUpdate("Equipment", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete equipment", description = "Delete an equipment entry")
    public ResponseEntity<Void> deleteEquipment(@PathVariable String id) {
        equipmentService.deleteEquipment(id);
        auditLogService.logDelete("Equipment", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Search equipment", description = "Search equipment by brand, model, or serial number")
    public ResponseEntity<List<EquipmentResponse>> searchEquipment(@RequestParam String q) {
        List<EquipmentResponse> equipment = equipmentService.searchEquipment(q);
        auditLogService.logSearch("Equipment", q);
        return ResponseEntity.ok(equipment);
    }
}
