package com.cyrcetech.backend.service;

import com.cyrcetech.backend.domain.entity.Invoice;
import com.cyrcetech.backend.domain.entity.PaymentStatus;
import com.cyrcetech.backend.domain.entity.Ticket;
import com.cyrcetech.backend.dto.request.CreateInvoiceRequest;
import com.cyrcetech.backend.dto.request.UpdateInvoiceRequest;
import com.cyrcetech.backend.dto.response.InvoiceResponse;
import com.cyrcetech.backend.exception.ResourceNotFoundException;
import com.cyrcetech.backend.repository.InvoiceRepository;
import com.cyrcetech.backend.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service layer for Invoice business logic
 */
@Service
@Transactional
public class InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final InvoiceRepository invoiceRepository;
    private final TicketRepository ticketRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, TicketRepository ticketRepository) {
        this.invoiceRepository = invoiceRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Get all invoices
     */
    public List<InvoiceResponse> getAllInvoices() {
        log.debug("Fetching all invoices");
        return invoiceRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get invoice by ID
     */
    public InvoiceResponse getInvoiceById(String id) {
        log.debug("Fetching invoice with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        return toResponse(invoice);
    }

    /**
     * Get invoice by ticket ID
     */
    public InvoiceResponse getInvoiceByTicketId(String ticketId) {
        log.debug("Fetching invoice for ticket: {}", ticketId);
        Invoice invoice = invoiceRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found for ticket: " + ticketId));
        return toResponse(invoice);
    }

    /**
     * Get invoice by invoice number
     */
    public InvoiceResponse getInvoiceByNumber(String invoiceNumber) {
        log.debug("Fetching invoice with number: {}", invoiceNumber);
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with number: " + invoiceNumber));
        return toResponse(invoice);
    }

    /**
     * Get invoices by payment status
     */
    public List<InvoiceResponse> getInvoicesByPaymentStatus(PaymentStatus status) {
        log.debug("Fetching invoices with payment status: {}", status);
        return invoiceRepository.findByPaymentStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get overdue invoices
     */
    public List<InvoiceResponse> getOverdueInvoices() {
        log.debug("Fetching overdue invoices");
        return invoiceRepository.findOverdueInvoices().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get paid invoices
     */
    public List<InvoiceResponse> getPaidInvoices() {
        log.debug("Fetching paid invoices");
        return invoiceRepository.findPaidInvoices().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get pending invoices
     */
    public List<InvoiceResponse> getPendingInvoices() {
        log.debug("Fetching pending invoices");
        return invoiceRepository.findPendingInvoices().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create new invoice
     */
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        log.debug("Creating new invoice for ticket: {}", request.getTicketId());

        // Validate ticket exists
        String ticketId = Objects.requireNonNull(request.getTicketId(), "Ticket ID cannot be null");
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + request.getTicketId()));

        Invoice invoice = new Invoice();
        invoice.setTicket(ticket);
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setIssueDate(request.getIssueDate() != null ? request.getIssueDate() : LocalDate.now());
        invoice.setDueDate(request.getDueDate());
        invoice.setSubtotal(request.getSubtotal());
        invoice.setTaxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : 0.0);
        invoice.setTotalAmount(request.getTotalAmount());
        invoice.setNotes(request.getNotes());

        Invoice saved = invoiceRepository.save(invoice);
        log.info("Invoice created with id: {}", saved.getId());

        return toResponse(saved);
    }

    /**
     * Update existing invoice
     */
    public InvoiceResponse updateInvoice(String id, UpdateInvoiceRequest request) {
        log.debug("Updating invoice with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        if (request.getDueDate() != null) {
            invoice.setDueDate(request.getDueDate());
        }
        if (request.getSubtotal() != null) {
            invoice.setSubtotal(request.getSubtotal());
        }
        if (request.getTaxAmount() != null) {
            invoice.setTaxAmount(request.getTaxAmount());
        }
        if (request.getTotalAmount() != null) {
            invoice.setTotalAmount(request.getTotalAmount());
        }
        if (request.getPaymentStatus() != null) {
            invoice.setPaymentStatus(request.getPaymentStatus());
        }
        if (request.getPaymentDate() != null) {
            invoice.setPaymentDate(request.getPaymentDate());
        }
        if (request.getPaymentMethod() != null) {
            invoice.setPaymentMethod(request.getPaymentMethod());
        }
        if (request.getNotes() != null) {
            invoice.setNotes(request.getNotes());
        }

        Invoice updated = invoiceRepository.save(Objects.requireNonNull(invoice));
        log.info("Invoice updated: {}", id);

        return toResponse(updated);
    }

    /**
     * Delete invoice
     */
    public void deleteInvoice(String id) {
        log.debug("Deleting invoice with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");

        if (!invoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found with id: " + id);
        }

        invoiceRepository.deleteById(id);
        log.info("Invoice deleted: {}", id);
    }

    /**
     * Convert Invoice entity to InvoiceResponse DTO
     */
    private InvoiceResponse toResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setId(invoice.getId());
        response.setTicketId(invoice.getTicket().getId());
        response.setCustomerName(invoice.getTicket().getCustomer().getName());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setIssueDate(invoice.getIssueDate().format(DATE_FORMATTER));
        response.setDueDate(invoice.getDueDate() != null ? invoice.getDueDate().format(DATE_FORMATTER) : null);
        response.setSubtotal(invoice.getSubtotal());
        response.setTaxAmount(invoice.getTaxAmount());
        response.setTotalAmount(invoice.getTotalAmount());
        response.setFormattedTotal(invoice.getFormattedTotal());
        response.setPaymentStatus(invoice.getPaymentStatus());
        response.setPaymentStatusDisplayName(invoice.getPaymentStatus().getDisplayName());
        response.setPaymentDate(
                invoice.getPaymentDate() != null ? invoice.getPaymentDate().format(DATE_FORMATTER) : null);
        response.setPaymentMethod(invoice.getPaymentMethod());
        response.setPaymentMethodDisplayName(
                invoice.getPaymentMethod() != null ? invoice.getPaymentMethod().getDisplayName() : null);
        response.setNotes(invoice.getNotes());
        response.setPaid(invoice.isPaid());
        response.setOverdue(invoice.isOverdue());
        return response;
    }
}
