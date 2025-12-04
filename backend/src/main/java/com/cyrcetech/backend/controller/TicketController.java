package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.domain.entity.TicketStatus;
import com.cyrcetech.backend.dto.request.CreateTicketRequest;
import com.cyrcetech.backend.dto.request.UpdateTicketRequest;
import com.cyrcetech.backend.dto.response.TicketResponse;
import com.cyrcetech.backend.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Ticket management
 */
@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Tickets", description = "Service ticket management APIs")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    @Operation(summary = "Get all tickets", description = "Retrieve a list of all service tickets")
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID", description = "Retrieve a ticket by its ID")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable String id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get tickets by customer", description = "Retrieve all tickets for a specific customer")
    public ResponseEntity<List<TicketResponse>> getTicketsByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(ticketService.getTicketsByCustomerId(customerId));
    }

    @GetMapping("/equipment/{equipmentId}")
    @Operation(summary = "Get tickets by equipment", description = "Retrieve all tickets for a specific equipment")
    public ResponseEntity<List<TicketResponse>> getTicketsByEquipmentId(@PathVariable String equipmentId) {
        return ResponseEntity.ok(ticketService.getTicketsByEquipmentId(equipmentId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get tickets by status", description = "Retrieve tickets by their status")
    public ResponseEntity<List<TicketResponse>> getTicketsByStatus(@PathVariable TicketStatus status) {
        return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active tickets", description = "Retrieve all active tickets (not delivered or cancelled)")
    public ResponseEntity<List<TicketResponse>> getActiveTickets() {
        return ResponseEntity.ok(ticketService.getActiveTickets());
    }

    @PostMapping
    @Operation(summary = "Create new ticket", description = "Create a new service ticket")
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ticket", description = "Update an existing ticket")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable String id,
            @Valid @RequestBody UpdateTicketRequest request) {
        return ResponseEntity.ok(ticketService.updateTicket(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ticket", description = "Delete a ticket")
    public ResponseEntity<Void> deleteTicket(@PathVariable String id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search tickets", description = "Search tickets by customer name, equipment, or problem description")
    public ResponseEntity<List<TicketResponse>> searchTickets(@RequestParam String q) {
        return ResponseEntity.ok(ticketService.searchTickets(q));
    }
}
