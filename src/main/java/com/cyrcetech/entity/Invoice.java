package com.cyrcetech.entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an invoice for a repair ticket.
 * Immutable record with validation.
 */
public record Invoice(
        String id,
        String ticketId,
        String invoiceNumber,
        LocalDate issueDate,
        LocalDate dueDate,
        double subtotal,
        double taxAmount,
        double totalAmount,
        PaymentStatus paymentStatus,
        LocalDate paymentDate,
        PaymentMethod paymentMethod,
        String notes) {

    /**
     * Compact constructor with validation
     */
    public Invoice {
        Objects.requireNonNull(ticketId, "Ticket ID cannot be null");
        Objects.requireNonNull(invoiceNumber, "Invoice number cannot be null");
        Objects.requireNonNull(issueDate, "Issue date cannot be null");
        Objects.requireNonNull(paymentStatus, "Payment status cannot be null");

        if (subtotal < 0) {
            throw new IllegalArgumentException("Subtotal cannot be negative");
        }
        if (taxAmount < 0) {
            throw new IllegalArgumentException("Tax amount cannot be negative");
        }
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }
    }

    /**
     * Creates an empty invoice for form initialization
     */
    public static Invoice empty() {
        return new Invoice("", "", "", LocalDate.now(), null, 0.0, 0.0, 0.0,
                PaymentStatus.PENDING, null, null, "");
    }

    /**
     * Checks if the invoice is fully paid
     */
    public boolean isPaid() {
        return paymentStatus == PaymentStatus.PAID;
    }

    /**
     * Checks if the invoice is overdue
     */
    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate) && !isPaid();
    }

    /**
     * Returns formatted total with currency
     */
    public String getFormattedTotal() {
        return String.format("₲%.2f", totalAmount);
    }

    /**
     * Creates a new invoice with updated payment information
     */
    public Invoice markAsPaid(LocalDate paymentDate, PaymentMethod method) {
        return new Invoice(id, ticketId, invoiceNumber, issueDate, dueDate,
                subtotal, taxAmount, totalAmount, PaymentStatus.PAID,
                paymentDate, method, notes);
    }
}
