package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.domain.entity.DeviceType;
import com.cyrcetech.backend.dto.request.CreateEquipmentRequest;
import com.cyrcetech.backend.dto.request.UpdateEquipmentRequest;
import com.cyrcetech.backend.dto.response.EquipmentResponse;
import com.cyrcetech.backend.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    @Operation(summary = "Get all equipment", description = "Retrieve a list of all equipment")
    public ResponseEntity<List<EquipmentResponse>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get equipment by ID", description = "Retrieve equipment by its ID")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable String id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get equipment by customer", description = "Retrieve all equipment for a specific customer")
    public ResponseEntity<List<EquipmentResponse>> getEquipmentByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(equipmentService.getEquipmentByCustomerId(customerId));
    }

    @GetMapping("/type/{deviceType}")
    @Operation(summary = "Get equipment by device type", description = "Retrieve equipment by device type")
    public ResponseEntity<List<EquipmentResponse>> getEquipmentByDeviceType(@PathVariable DeviceType deviceType) {
        return ResponseEntity.ok(equipmentService.getEquipmentByDeviceType(deviceType));
    }

    @PostMapping
    @Operation(summary = "Create new equipment", description = "Create a new equipment entry")
    public ResponseEntity<EquipmentResponse> createEquipment(@Valid @RequestBody CreateEquipmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.createEquipment(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update equipment", description = "Update an existing equipment entry")
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable String id,
            @Valid @RequestBody UpdateEquipmentRequest request) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete equipment", description = "Delete an equipment entry")
    public ResponseEntity<Void> deleteEquipment(@PathVariable String id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search equipment", description = "Search equipment by brand, model, or serial number")
    public ResponseEntity<List<EquipmentResponse>> searchEquipment(@RequestParam String q) {
        return ResponseEntity.ok(equipmentService.searchEquipment(q));
    }
}
