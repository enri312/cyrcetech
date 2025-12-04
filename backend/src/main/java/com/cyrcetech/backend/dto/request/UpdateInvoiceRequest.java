package com.cyrcetech.backend.dto.request;

import com.cyrcetech.backend.domain.entity.PaymentMethod;
import com.cyrcetech.backend.domain.entity.PaymentStatus;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

/**
 * DTO for updating invoice
 */
public class UpdateInvoiceRequest {

    private LocalDate dueDate;

    @PositiveOrZero(message = "Subtotal must be zero or positive")
    private Double subtotal;

    @PositiveOrZero(message = "Tax amount must be zero or positive")
    private Double taxAmount;

    @PositiveOrZero(message = "Total amount must be zero or positive")
    private Double totalAmount;

    private PaymentStatus paymentStatus;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;
    private String notes;

    // Constructors
    public UpdateInvoiceRequest() {
    }

    // Getters and Setters
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
