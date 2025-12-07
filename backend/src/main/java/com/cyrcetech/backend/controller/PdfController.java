package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.dto.response.InvoiceResponse;
import com.cyrcetech.backend.dto.response.TicketResponse;
import com.cyrcetech.backend.service.InvoiceService;
import com.cyrcetech.backend.service.PdfService;
import com.cyrcetech.backend.service.TicketService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents")
public class PdfController {

    private final TicketService ticketService;
    private final InvoiceService invoiceService;
    private final PdfService pdfService;

    public PdfController(TicketService ticketService, InvoiceService invoiceService, PdfService pdfService) {
        this.ticketService = ticketService;
        this.invoiceService = invoiceService;
        this.pdfService = pdfService;
    }

    @GetMapping("/ticket/{id}/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'USER')")
    public ResponseEntity<byte[]> downloadTicketPdf(@PathVariable String id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        byte[] pdfBytes = pdfService.generateTicketPdf(ticket);
        return createPdfResponse(pdfBytes, "orden_servicio_" + id.substring(0, 8));
    }

    @GetMapping("/invoice/{id}/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'USER')")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable String id) {
        InvoiceResponse invoice = invoiceService.getInvoiceById(id);
        byte[] pdfBytes = pdfService.generateInvoicePdf(invoice);
        return createPdfResponse(pdfBytes, "factura_" + id.substring(0, 8));
    }

    private ResponseEntity<byte[]> createPdfResponse(byte[] pdfBytes, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
