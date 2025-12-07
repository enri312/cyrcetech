package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.domain.entity.PaymentStatus;
import com.cyrcetech.backend.dto.request.CreateInvoiceRequest;
import com.cyrcetech.backend.dto.request.UpdateInvoiceRequest;
import com.cyrcetech.backend.dto.response.InvoiceResponse;
import com.cyrcetech.backend.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Invoice management
 */
@RestController
@RequestMapping("/api/invoices")
@Tag(name = "Invoices", description = "Invoice and billing management APIs")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all invoices", description = "Retrieve a list of all invoices")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get invoice by ID", description = "Retrieve an invoice by its ID")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable String id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @GetMapping("/ticket/{ticketId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get invoice by ticket", description = "Retrieve invoice for a specific ticket")
    public ResponseEntity<InvoiceResponse> getInvoiceByTicketId(@PathVariable String ticketId) {
        return ResponseEntity.ok(invoiceService.getInvoiceByTicketId(ticketId));
    }

    @GetMapping("/number/{invoiceNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get invoice by number", description = "Retrieve invoice by its invoice number")
    public ResponseEntity<InvoiceResponse> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok(invoiceService.getInvoiceByNumber(invoiceNumber));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get invoices by payment status", description = "Retrieve invoices by their payment status")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByPaymentStatus(@PathVariable PaymentStatus status) {
        return ResponseEntity.ok(invoiceService.getInvoicesByPaymentStatus(status));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get overdue invoices", description = "Retrieve all overdue invoices")
    public ResponseEntity<List<InvoiceResponse>> getOverdueInvoices() {
        return ResponseEntity.ok(invoiceService.getOverdueInvoices());
    }

    @GetMapping("/paid")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get paid invoices", description = "Retrieve all paid invoices")
    public ResponseEntity<List<InvoiceResponse>> getPaidInvoices() {
        return ResponseEntity.ok(invoiceService.getPaidInvoices());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get pending invoices", description = "Retrieve all pending invoices")
    public ResponseEntity<List<InvoiceResponse>> getPendingInvoices() {
        return ResponseEntity.ok(invoiceService.getPendingInvoices());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new invoice", description = "Create a new invoice")
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.createInvoice(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update invoice", description = "Update an existing invoice")
    public ResponseEntity<InvoiceResponse> updateInvoice(
            @PathVariable String id,
            @Valid @RequestBody UpdateInvoiceRequest request) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete invoice", description = "Delete an invoice")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}
