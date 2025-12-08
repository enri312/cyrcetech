package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.dto.request.CreateCustomerRequest;
import com.cyrcetech.backend.dto.request.UpdateCustomerRequest;
import com.cyrcetech.backend.dto.response.CustomerResponse;
import com.cyrcetech.backend.service.AuditLogService;
import com.cyrcetech.backend.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Customer management
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer management APIs")
public class CustomerController {

    private final CustomerService customerService;
    private final AuditLogService auditLogService;

    public CustomerController(CustomerService customerService, AuditLogService auditLogService) {
        this.customerService = customerService;
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        auditLogService.logList("Customer");
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by their ID")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String id) {
        CustomerResponse customer = customerService.getCustomerById(id);
        auditLogService.logView("Customer", id);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Create new customer", description = "Create a new customer in the system")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse created = customerService.createCustomer(request);
        auditLogService.logCreate("Customer", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Update customer", description = "Update an existing customer's information")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable String id,
            @RequestBody UpdateCustomerRequest request) {
        CustomerResponse updated = customerService.updateCustomer(id, request);
        auditLogService.logUpdate("Customer", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete customer", description = "Delete a customer from the system")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        auditLogService.logDelete("Customer", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Search customers", description = "Search customers by name, tax ID, or phone")
    public ResponseEntity<List<CustomerResponse>> searchCustomers(@RequestParam String q) {
        List<CustomerResponse> customers = customerService.searchCustomers(q);
        auditLogService.logSearch("Customer", q);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/export/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    @Operation(summary = "Export customers to PDF", description = "Export all customers to PDF with seniority information")
    public ResponseEntity<byte[]> exportToPdf() {
        try {
            byte[] pdfData = customerService.exportCustomersToPdf();
            auditLogService.logExport("Customer", "PDF");
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=customers.pdf")
                    .header("Content-Type", "application/pdf")
                    .body(pdfData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
