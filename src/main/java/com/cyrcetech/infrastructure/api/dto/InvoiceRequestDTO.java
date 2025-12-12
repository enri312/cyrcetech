package com.cyrcetech.infrastructure.api.dto;

/**
 * DTO for creating/updating Invoice via API
 * Using String for dates to ensure correct serialization format "yyyy-MM-dd"
 */
public class InvoiceRequestDTO {
    private String ticketId;
    private String invoiceNumber;
    private String issueDate;
    private String dueDate;
    private Double subtotal;
    private Double taxAmount;
    private Double totalAmount;
    private Double paidAmount;
    private String notes;

    public InvoiceRequestDTO() {
    }

    public InvoiceRequestDTO(String ticketId, String invoiceNumber, String issueDate, String dueDate,
            Double subtotal, Double taxAmount, Double totalAmount, String notes) {
        this.ticketId = ticketId;
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.paidAmount = 0.0;
        this.notes = notes;
    }

    public InvoiceRequestDTO(String ticketId, String invoiceNumber, String issueDate, String dueDate,
            Double subtotal, Double taxAmount, Double totalAmount, Double paidAmount, String notes) {
        this.ticketId = ticketId;
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.notes = notes;
    }

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

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
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

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
