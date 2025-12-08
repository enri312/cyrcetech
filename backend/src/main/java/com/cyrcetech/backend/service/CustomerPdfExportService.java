package com.cyrcetech.backend.service;

import com.cyrcetech.backend.domain.entity.Customer;
import com.cyrcepdf.core.PdfBuilder;
import com.cyrcepdf.core.StandardFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service for exporting customer data to PDF format using CyrcePDF.
 */
@Service
public class CustomerPdfExportService {

    private static final Logger log = LoggerFactory.getLogger(CustomerPdfExportService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Export customers to PDF format with seniority information.
     *
     * @param customers List of customers to export
     * @return byte array containing the PDF file
     * @throws IOException if export fails
     */
    public byte[] exportCustomersToPdf(List<Customer> customers) throws IOException {
        log.info("Exporting {} customers to PDF", customers.size());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfBuilder.create()
                    .title("Reporte de Clientes - Cyrcetech")
                    .addPage(page -> {
                        float y = 750;
                        float leading = 15;

                        // Title
                        page.text("REPORTE DE CLIENTES CYRCETECH", 50, y, StandardFont.HELVETICA_BOLD, 18);
                        y -= 25;
                        page.text("Fecha de generación: " + LocalDate.now().format(DATE_FORMATTER), 50, y,
                                StandardFont.HELVETICA, 10);
                        y -= 30;

                        // Table Header
                        page.text("Nombre", 50, y, StandardFont.HELVETICA_BOLD, 9);
                        page.text("RUC/DNI", 150, y, StandardFont.HELVETICA_BOLD, 9);
                        page.text("Teléfono", 230, y, StandardFont.HELVETICA_BOLD, 9);
                        page.text("Fecha Reg.", 310, y, StandardFont.HELVETICA_BOLD, 9);
                        page.text("Antigüedad", 390, y, StandardFont.HELVETICA_BOLD, 9);
                        page.text("Categoría", 480, y, StandardFont.HELVETICA_BOLD, 9);
                        y -= 5;

                        // Draw header line
                        y -= leading;

                        // Data rows
                        for (Customer customer : customers) {
                            if (y < 50) {
                                break; // Avoid going below page margin
                            }

                            String name = truncate(customer.getName() != null ? customer.getName() : "", 18);
                            String taxId = customer.getTaxId() != null ? customer.getTaxId() : "";
                            String phone = customer.getFormattedPhone();
                            String regDate = customer.getRegistrationDate() != null
                                    ? customer.getRegistrationDate().format(DATE_FORMATTER)
                                    : "";
                            String seniority = customer.getFormattedSeniority();
                            String category = customer.getCategory() != null
                                    ? customer.getCategory().getDisplayName()
                                    : "";

                            page.text(name, 50, y, StandardFont.HELVETICA, 8);
                            page.text(taxId, 150, y, StandardFont.HELVETICA, 8);
                            page.text(phone, 230, y, StandardFont.HELVETICA, 8);
                            page.text(regDate, 310, y, StandardFont.HELVETICA, 8);
                            page.text(seniority, 390, y, StandardFont.HELVETICA, 8);
                            page.text(category, 480, y, StandardFont.HELVETICA_BOLD, 8);

                            y -= leading;
                        }

                        // Footer with totals
                        y -= 10;
                        page.text("Total de clientes: " + customers.size(), 50, y, StandardFont.HELVETICA_BOLD, 10);
                        y -= leading;

                        // Count by category
                        long nuevos = customers.stream()
                                .filter(c -> c.getCategory() != null && c.getCategory().name().equals("NUEVO"))
                                .count();
                        long regulares = customers.stream()
                                .filter(c -> c.getCategory() != null && c.getCategory().name().equals("REGULAR"))
                                .count();
                        long vips = customers.stream()
                                .filter(c -> c.getCategory() != null && c.getCategory().name().equals("VIP"))
                                .count();
                        long especiales = customers.stream()
                                .filter(c -> c.getCategory() != null && c.getCategory().name().equals("ESPECIAL"))
                                .count();

                        page.text(String.format("Nuevos: %d | Regulares: %d | VIP: %d | Especiales: %d",
                                nuevos, regulares, vips, especiales), 50, y, StandardFont.HELVETICA, 9);
                    })
                    .save(outputStream);

            log.info("PDF export completed successfully");
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("Failed to export customers to PDF: {}", e.getMessage(), e);
            throw new IOException("Failed to export customers to PDF", e);
        }
    }

    private String truncate(String text, int maxLength) {
        if (text == null)
            return "";
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }
}
