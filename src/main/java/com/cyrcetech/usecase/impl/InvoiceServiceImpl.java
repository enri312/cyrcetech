package com.cyrcetech.usecase.impl;

import com.cyrcetech.entity.Invoice;
import com.cyrcetech.entity.PaymentMethod;
import com.cyrcetech.infrastructure.InvoiceDAO;
import com.cyrcetech.usecase.InvoiceService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceDAO invoiceDAO;

    public InvoiceServiceImpl(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        try {
            return invoiceDAO.save(invoice);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating invoice", e);
        }
    }

    @Override
    public void updateInvoice(Invoice invoice) {
        try {
            invoiceDAO.update(invoice);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating invoice", e);
        }
    }

    @Override
    public Invoice getInvoiceById(String id) {
        try {
            return invoiceDAO.findById(id).orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching invoice", e);
        }
    }

    @Override
    public List<Invoice> getInvoicesByTicketId(String ticketId) {
        try {
            return invoiceDAO.findByTicketId(ticketId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching invoices for ticket", e);
        }
    }

    @Override
    public List<Invoice> getAllInvoices() {
        try {
            return invoiceDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching all invoices", e);
        }
    }

    @Override
    public String generateInvoiceNumber() {
        try {
            return invoiceDAO.generateInvoiceNumber();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating invoice number", e);
        }
    }

    @Override
    public void markAsPaid(String invoiceId, LocalDate paymentDate, PaymentMethod method) {
        try {
            Invoice invoice = invoiceDAO.findById(invoiceId)
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));
            Invoice updatedInvoice = invoice.markAsPaid(paymentDate, method);
            invoiceDAO.update(updatedInvoice);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error marking invoice as paid", e);
        }
    }
}
