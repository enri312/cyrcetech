package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.domain.entity.TicketStatus;
import com.cyrcetech.backend.dto.request.CreateTicketRequest;
import com.cyrcetech.backend.dto.request.UpdateTicketRequest;
import com.cyrcetech.backend.dto.response.TicketResponse;
import com.cyrcetech.backend.service.AuditLogService;
import com.cyrcetech.backend.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final AuditLogService auditLogService;

    public TicketController(TicketService ticketService, AuditLogService auditLogService) {
        this.ticketService = ticketService;
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get all tickets", description = "Retrieve a list of all service tickets")
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        List<TicketResponse> tickets = ticketService.getAllTickets();
        auditLogService.logList("Ticket");
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'USER')")
    @Operation(summary = "Get ticket by ID", description = "Retrieve a ticket by its ID")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable String id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        auditLogService.logView("Ticket", id);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get tickets by customer", description = "Retrieve all tickets for a specific customer")
    public ResponseEntity<List<TicketResponse>> getTicketsByCustomerId(@PathVariable String customerId) {
        List<TicketResponse> tickets = ticketService.getTicketsByCustomerId(customerId);
        auditLogService.logSearch("Ticket", "customerId=" + customerId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/equipment/{equipmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get tickets by equipment", description = "Retrieve all tickets for a specific equipment")
    public ResponseEntity<List<TicketResponse>> getTicketsByEquipmentId(@PathVariable String equipmentId) {
        List<TicketResponse> tickets = ticketService.getTicketsByEquipmentId(equipmentId);
        auditLogService.logSearch("Ticket", "equipmentId=" + equipmentId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get tickets by status", description = "Retrieve tickets by their status")
    public ResponseEntity<List<TicketResponse>> getTicketsByStatus(@PathVariable TicketStatus status) {
        List<TicketResponse> tickets = ticketService.getTicketsByStatus(status);
        auditLogService.logSearch("Ticket", "status=" + status);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get active tickets", description = "Retrieve all active tickets (not delivered or cancelled)")
    public ResponseEntity<List<TicketResponse>> getActiveTickets() {
        List<TicketResponse> tickets = ticketService.getActiveTickets();
        auditLogService.logSearch("Ticket", "active=true");
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'USER')")
    @Operation(summary = "Create new ticket", description = "Create a new service ticket")
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        TicketResponse created = ticketService.createTicket(request);
        auditLogService.logCreate("Ticket", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Update ticket", description = "Update an existing ticket")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable String id,
            @Valid @RequestBody UpdateTicketRequest request) {
        TicketResponse updated = ticketService.updateTicket(id, request);
        auditLogService.logUpdate("Ticket", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete ticket", description = "Delete a ticket")
    public ResponseEntity<Void> deleteTicket(@PathVariable String id) {
        ticketService.deleteTicket(id);
        auditLogService.logDelete("Ticket", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Search tickets", description = "Search tickets by customer name, equipment, or problem description")
    public ResponseEntity<List<TicketResponse>> searchTickets(@RequestParam String q) {
        List<TicketResponse> tickets = ticketService.searchTickets(q);
        auditLogService.logSearch("Ticket", q);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/export/excel")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Export tickets to Excel", description = "Export all tickets to Excel format")
    public ResponseEntity<byte[]> exportToExcel() {
        try {
            byte[] excelData = ticketService.exportTicketsToExcel();
            auditLogService.logExport("Ticket", "Excel");
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=tickets.xlsx")
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
