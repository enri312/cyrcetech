package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.dto.request.CreateSparePartRequest;
import com.cyrcetech.backend.dto.request.UpdateSparePartRequest;
import com.cyrcetech.backend.dto.response.SparePartResponse;
import com.cyrcetech.backend.service.SparePartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for SparePart management
 */
@RestController
@RequestMapping("/api/spare-parts")
@Tag(name = "Spare Parts", description = "Inventory management APIs")
public class SparePartController {

    private final SparePartService sparePartService;

    public SparePartController(SparePartService sparePartService) {
        this.sparePartService = sparePartService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get all spare parts", description = "Retrieve a list of all spare parts")
    public ResponseEntity<List<SparePartResponse>> getAllSpareParts() {
        return ResponseEntity.ok(sparePartService.getAllSpareParts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get spare part by ID", description = "Retrieve a spare part by its ID")
    public ResponseEntity<SparePartResponse> getSparePartById(@PathVariable String id) {
        return ResponseEntity.ok(sparePartService.getSparePartById(id));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get low stock spare parts", description = "Retrieve spare parts with low stock (less than 5 units)")
    public ResponseEntity<List<SparePartResponse>> getLowStockSpareParts() {
        return ResponseEntity.ok(sparePartService.getLowStockSpareParts());
    }

    @GetMapping("/out-of-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get out of stock spare parts", description = "Retrieve spare parts that are out of stock")
    public ResponseEntity<List<SparePartResponse>> getOutOfStockSpareParts() {
        return ResponseEntity.ok(sparePartService.getOutOfStockSpareParts());
    }

    @GetMapping("/in-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get in stock spare parts", description = "Retrieve spare parts that are in stock")
    public ResponseEntity<List<SparePartResponse>> getInStockSpareParts() {
        return ResponseEntity.ok(sparePartService.getInStockSpareParts());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Create new spare part", description = "Create a new spare part in inventory")
    public ResponseEntity<SparePartResponse> createSparePart(@Valid @RequestBody CreateSparePartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sparePartService.createSparePart(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Update spare part", description = "Update an existing spare part")
    public ResponseEntity<SparePartResponse> updateSparePart(
            @PathVariable String id,
            @Valid @RequestBody UpdateSparePartRequest request) {
        return ResponseEntity.ok(sparePartService.updateSparePart(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete spare part", description = "Delete a spare part from inventory")
    public ResponseEntity<Void> deleteSparePart(@PathVariable String id) {
        sparePartService.deleteSparePart(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Search spare parts", description = "Search spare parts by name or provider")
    public ResponseEntity<List<SparePartResponse>> searchSpareParts(@RequestParam String q) {
        return ResponseEntity.ok(sparePartService.searchSpareParts(q));
    }
}
