package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.domain.entity.Invoice;
import com.cyrcetech.backend.repository.InvoiceRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/billing")
@PreAuthorize("hasRole('ADMIN')")
public class BillingController {

    private final InvoiceRepository invoiceRepository;

    public BillingController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping("/daily/{date}")
    public ResponseEntity<List<Invoice>> getDailyBilling(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // Assuming we want invoices ISSUED on this date
        List<Invoice> invoices = invoiceRepository.findAll().stream()
                .filter(i -> i.getIssueDate().isEqual(date))
                .collect(Collectors.toList());
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/monthly/{year}/{month}")
    public ResponseEntity<List<Invoice>> getMonthlyBilling(
            @PathVariable int year, @PathVariable int month) {
        List<Invoice> invoices = invoiceRepository.findAll().stream()
                .filter(i -> i.getIssueDate().getYear() == year && i.getIssueDate().getMonthValue() == month)
                .collect(Collectors.toList());
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/yearly/{year}")
    public ResponseEntity<List<Invoice>> getYearlyBilling(@PathVariable int year) {
        List<Invoice> invoices = invoiceRepository.findAll().stream()
                .filter(i -> i.getIssueDate().getYear() == year)
                .collect(Collectors.toList());
        return ResponseEntity.ok(invoices);
    }
}
