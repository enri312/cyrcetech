package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.entity.Invoice;
import com.cyrcetech.infrastructure.api.ApiClient;
import com.cyrcetech.infrastructure.api.ApiConfig;
import com.cyrcetech.infrastructure.api.dto.InvoiceRequestDTO;
import com.cyrcetech.infrastructure.api.dto.InvoiceResponseDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to interact with Invoice REST API
 */
public class InvoiceApiService extends ApiClient {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Get all invoices
     */
    public List<Invoice> getAllInvoices() throws Exception {
        Type listType = new TypeToken<List<InvoiceResponseDTO>>() {
        }.getType();
        String json = getString(ApiConfig.getInvoicesUrl());
        List<InvoiceResponseDTO> dtos = getGson().fromJson(json, listType);

        List<Invoice> invoices = new ArrayList<>();
        for (InvoiceResponseDTO dto : dtos) {
            invoices.add(toEntity(dto));
        }
        return invoices;
    }

    /**
     * Get invoice by ID
     */
    public Invoice getInvoiceById(String id) throws Exception {
        String url = ApiConfig.getInvoicesUrl() + "/" + id;
        InvoiceResponseDTO dto = get(url, InvoiceResponseDTO.class);
        return toEntity(dto);
    }

    /**
     * Create new invoice
     */
    public Invoice createInvoice(Invoice invoice) throws Exception {
        InvoiceRequestDTO request = toRequestDTO(invoice);
        InvoiceResponseDTO response = post(ApiConfig.getInvoicesUrl(), request, InvoiceResponseDTO.class);
        return toEntity(response);
    }

    /**
     * Update existing invoice
     */
    public Invoice updateInvoice(String id, Invoice invoice) throws Exception {
        String url = ApiConfig.getInvoicesUrl() + "/" + id;
        InvoiceRequestDTO request = toRequestDTO(invoice);
        InvoiceResponseDTO response = put(url, request, InvoiceResponseDTO.class);
        return toEntity(response);
    }

    /**
     * Delete invoice
     */
    public void deleteInvoice(String id) throws Exception {
        String url = ApiConfig.getInvoicesUrl() + "/" + id;
        delete(url);
    }

    /**
     * Convert DTO to Entity (Record)
     */
    private Invoice toEntity(InvoiceResponseDTO dto) {
        LocalDate issueDate = dto.getIssueDate() != null ? LocalDate.parse(dto.getIssueDate()) : null;
        LocalDate dueDate = dto.getDueDate() != null ? LocalDate.parse(dto.getDueDate()) : null;
        LocalDate paymentDate = dto.getPaymentDate() != null ? LocalDate.parse(dto.getPaymentDate()) : null;

        return new Invoice(
                dto.getId(),
                dto.getTicketId(),
                dto.getInvoiceNumber(),
                issueDate,
                dueDate,
                dto.getSubtotal(),
                dto.getTaxAmount(),
                dto.getTotalAmount(),
                dto.getPaidAmount(),
                dto.getPaymentStatus(),
                paymentDate,
                dto.getPaymentMethod(),
                dto.getNotes());
    }

    /**
     * Convert Entity (Record) to Request DTO
     */
    private InvoiceRequestDTO toRequestDTO(Invoice invoice) {
        String issueDate = invoice.issueDate() != null ? invoice.issueDate().format(DATE_FORMATTER) : null;
        String dueDate = invoice.dueDate() != null ? invoice.dueDate().format(DATE_FORMATTER) : null;

        return new InvoiceRequestDTO(
                invoice.ticketId(),
                invoice.invoiceNumber(),
                issueDate,
                dueDate,
                invoice.subtotal(),
                invoice.taxAmount(),
                invoice.totalAmount(),
                invoice.paidAmount(),
                invoice.notes());
    }

    /**
     * Generate a new invoice number (client-side for now)
     */
    public String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
    }
}
