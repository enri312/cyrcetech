package com.cyrcetech.backend.service;

import com.cyrcetech.backend.dto.response.InvoiceResponse;
import com.cyrcetech.backend.dto.response.TicketResponse;
import com.cyrcepdf.core.PdfBuilder;
import com.cyrcepdf.core.StandardFont;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class PdfService {

        public byte[] generateInvoicePdf(InvoiceResponse invoice) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                try {
                        PdfBuilder.create()
                                        .title("CYRCETECH - Factura")
                                        .addPage(page -> {
                                                // Header
                                                page.text("CYRCETECH - Factura", 50, 750, StandardFont.HELVETICA_BOLD,
                                                                18);
                                                page.text("Comprobante de Pago", 50, 730, StandardFont.HELVETICA, 12);

                                                double y = 700;
                                                double leading = 15;

                                                // Invoice Details
                                                page.text("Factura Nro: " + (invoice.getId() != null
                                                                ? invoice.getId().substring(0, 8)
                                                                : "N/A"),
                                                                50, y);
                                                y -= leading;
                                                page.text("Fecha: " + (invoice.getIssueDate() != null
                                                                ? invoice.getIssueDate()
                                                                : new Date().toString()), 50, y);
                                                y -= (leading * 2);

                                                // Customer Details
                                                page.text("Cliente: "
                                                                + (invoice.getCustomerName() != null
                                                                                ? invoice.getCustomerName()
                                                                                : "Consumidor Final"),
                                                                50, y);
                                                y -= leading;

                                                // Payment Info
                                                page.text("Método de Pago: "
                                                                + (invoice.getPaymentMethod() != null
                                                                                ? invoice.getPaymentMethod().toString()
                                                                                : "Efectivo"),
                                                                50, y);
                                                y -= leading;
                                                page.text(
                                                                "Estado: " + (invoice.getPaymentStatus() != null
                                                                                ? invoice.getPaymentStatus().toString()
                                                                                : "Pendiente"),
                                                                50, y);
                                                y -= (leading * 2);

                                                // Amount (Big highlight)
                                                page.text("TOTAL: " + invoice.getFormattedTotal(), 50, y,
                                                                StandardFont.HELVETICA_BOLD, 16);

                                                // Notes
                                                if (invoice.getNotes() != null && !invoice.getNotes().isEmpty()) {
                                                        y -= (leading * 3);
                                                        page.text("Notas:", 50, y);
                                                        y -= leading;
                                                        page.text(cleanText(invoice.getNotes()), 50, y);
                                                }

                                                // Footer
                                                page.text("Gracias por su confianza.", 50, 100,
                                                                StandardFont.HELVETICA_OBLIQUE, 10);
                                        })
                                        .save(outputStream);

                        return outputStream.toByteArray();
                } catch (IOException e) {
                        throw new RuntimeException("Error generating Invoice PDF with CyrcePDF", e);
                }
        }

        public byte[] generateTicketPdf(TicketResponse ticket) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                try {
                        PdfBuilder.create()
                                        .title("CYRCETECH - Orden de Servicio")
                                        .addPage(page -> {
                                                // Title
                                                page.text("CYRCETECH - Orden de Servicio", 50, 750,
                                                                StandardFont.HELVETICA_BOLD, 18);

                                                // Ticket Info
                                                double y = 720;
                                                double leading = 15;

                                                page.text("Ticket ID: " + ticket.getId().substring(0, 8), 50, y);
                                                y -= leading;

                                                page.text("Fecha: " + (ticket.getDateCreated() != null
                                                                ? ticket.getDateCreated().toString()
                                                                : new Date().toString()), 50, y);
                                                y -= (leading * 2);

                                                // Content
                                                page.text("Cliente: " + (ticket.getCustomerName() != null
                                                                ? ticket.getCustomerName()
                                                                : "N/A"),
                                                                50, y);
                                                y -= leading;

                                                page.text(
                                                                "Equipo: "
                                                                                + (ticket.getEquipmentSummary() != null
                                                                                                ? ticket.getEquipmentSummary()
                                                                                                : "N/A"),
                                                                50, y);
                                                y -= (leading * 2);

                                                page.text("Problema Reportado:", 50, y);
                                                y -= leading;
                                                page.text(cleanText(ticket.getProblemDescription()), 50, y);
                                                y -= (leading * 2);

                                                page.text("Diagnóstico IA:", 50, y);
                                                y -= leading;
                                                page.text(cleanText(ticket.getAiDiagnosis()), 50, y);
                                                y -= (leading * 2);

                                                // Status & Cost
                                                page.text("Estado: " + ticket.getStatusDisplayName(), 50, y);
                                                y -= leading;
                                                page.text("Estimación: $" + ticket.getEstimatedCost(), 50, y);
                                        })
                                        .save(outputStream);

                        return outputStream.toByteArray();
                } catch (IOException e) {
                        throw new RuntimeException("Error generating PDF with CyrcePDF", e);
                }
        }

        private String cleanText(String text) {
                return text != null ? text.replace("\n", " ").replace("\r", " ") : "N/A";
        }
}
