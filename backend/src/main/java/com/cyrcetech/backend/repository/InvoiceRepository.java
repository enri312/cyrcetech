package com.cyrcetech.backend.repository;

import com.cyrcetech.backend.domain.entity.Invoice;
import com.cyrcetech.backend.domain.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Invoice entity
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    /**
     * Find invoice by ticket ID
     */
    Optional<Invoice> findByTicketId(String ticketId);

    /**
     * Find invoice by invoice number
     */
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    /**
     * Find invoices by payment status
     */
    List<Invoice> findByPaymentStatus(PaymentStatus paymentStatus);

    /**
     * Find overdue invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.dueDate < CURRENT_DATE AND i.paymentStatus != 'PAID'")
    List<Invoice> findOverdueInvoices();

    /**
     * Find paid invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus = 'PAID'")
    List<Invoice> findPaidInvoices();

    /**
     * Find pending invoices
     */
    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus = 'PENDING'")
    List<Invoice> findPendingInvoices();
}
