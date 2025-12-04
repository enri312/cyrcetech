package com.cyrcetech.backend.dto.request;

import com.cyrcetech.backend.domain.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

/**
 * DTO for creating new invoice
 */
public class CreateInvoiceRequest {

    @NotBlank(message = "Ticket ID is required")
    private String ticketId;

    @NotBlank(message = "Invoice number is required")
    private String invoiceNumber;

    private LocalDate issueDate;

    private LocalDate dueDate;

    @NotNull(message = "Subtotal is required")
    @PositiveOrZero(message = "Subtotal must be zero or positive")
    private Double subtotal;

    @PositiveOrZero(message = "Tax amount must be zero or positive")
    private Double taxAmount;

    @NotNull(message = "Total amount is required")
    @PositiveOrZero(message = "Total amount must be zero or positive")
    private Double totalAmount;

    private String notes;

    // Constructors
    public CreateInvoiceRequest() {
    }

    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
