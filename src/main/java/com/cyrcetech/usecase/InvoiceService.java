package com.cyrcetech.usecase;

import com.cyrcetech.entity.Invoice;
import com.cyrcetech.entity.PaymentMethod;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {
    Invoice createInvoice(Invoice invoice);

    void updateInvoice(Invoice invoice);

    Invoice getInvoiceById(String id);

    List<Invoice> getInvoicesByTicketId(String ticketId);

    List<Invoice> getAllInvoices();

    String generateInvoiceNumber();

    void markAsPaid(String invoiceId, LocalDate paymentDate, PaymentMethod method);
}
