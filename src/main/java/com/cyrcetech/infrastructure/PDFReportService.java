package com.cyrcetech.infrastructure;

import com.cyrcetech.app.DependencyContainer;
import com.cyrcetech.entity.Customer;
import com.cyrcetech.entity.SparePart;
import com.cyrcetech.entity.Ticket;
import com.cyrcetech.usecase.CustomerService;
import com.cyrcetech.usecase.SparePartService;
import com.cyrcetech.usecase.TicketService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFReportService {

    private final CustomerService customerService = DependencyContainer.getCustomerService();
    private final TicketService ticketService = DependencyContainer.getTicketService();
    private final SparePartService sparePartService = DependencyContainer.getSparePartService();

    private static final float MARGIN = 50;
    private static final float FONT_SIZE_TITLE = 18;
    private static final float FONT_SIZE_NORMAL = 12;
    private static final float LINE_HEIGHT = 15;

    public void generateOrdersReport(File outputFile) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Title
                yPosition = drawTitle(contentStream, yPosition, "Reporte de Órdenes");
                yPosition -= LINE_HEIGHT * 2;

                // Get all tickets
                List<Ticket> tickets = ticketService.getAllTickets();

                // Table header
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_NORMAL);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Cliente");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText("Equipo");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText("Estado");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Fecha");
                contentStream.endText();
                yPosition -= LINE_HEIGHT * 1.5f;

                // Table rows
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
                for (Ticket ticket : tickets) {
                    if (yPosition < MARGIN + 50) {
                        // New page if needed
                        contentStream.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        PDPageContentStream newStream = new PDPageContentStream(document, page);
                        yPosition = page.getMediaBox().getHeight() - MARGIN;
                        contentStream.close();
                        return; // Simplified for now
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(MARGIN, yPosition);
                    contentStream.showText(truncate(ticket.getCustomer().name(), 20));
                    contentStream.newLineAtOffset(150, 0);
                    contentStream.showText(
                            truncate(ticket.getEquipment().brand() + " " + ticket.getEquipment().model(), 20));
                    contentStream.newLineAtOffset(150, 0);
                    contentStream.showText(ticket.getStatus().toString());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(ticket.getDateCreated().toString());
                    contentStream.endText();
                    yPosition -= LINE_HEIGHT;
                }

                // Summary
                yPosition -= LINE_HEIGHT * 2;
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_NORMAL);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Total de órdenes: " + tickets.size());
                contentStream.endText();
            }

            document.save(outputFile);
        }
    }

    public void generateCustomersReport(File outputFile) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Title
                yPosition = drawTitle(contentStream, yPosition, "Listado de Clientes");
                yPosition -= LINE_HEIGHT * 2;

                // Get all customers
                List<Customer> customers = customerService.getAllCustomers();

                // Table header
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_NORMAL);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Nombre");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText("RUC / CI");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Teléfono");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Dirección");
                contentStream.endText();
                yPosition -= LINE_HEIGHT * 1.5f;

                // Table rows
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
                for (Customer customer : customers) {
                    if (yPosition < MARGIN + 50)
                        break; // Simplified pagination

                    contentStream.beginText();
                    contentStream.newLineAtOffset(MARGIN, yPosition);
                    contentStream.showText(truncate(customer.name(), 20));
                    contentStream.newLineAtOffset(150, 0);
                    contentStream.showText(truncate(customer.taxId(), 15));
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(truncate(customer.phone(), 15));
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(truncate(customer.address(), 20));
                    contentStream.endText();
                    yPosition -= LINE_HEIGHT;
                }

                // Summary
                yPosition -= LINE_HEIGHT * 2;
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_NORMAL);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Total de clientes: " + customers.size());
                contentStream.endText();
            }

            document.save(outputFile);
        }
    }

    public void generateInventoryReport(File outputFile) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Title
                yPosition = drawTitle(contentStream, yPosition, "Reporte de Inventario");
                yPosition -= LINE_HEIGHT * 2;

                // Get all spare parts
                List<SparePart> spareParts = sparePartService.getAllSpareParts();

                // Table header
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_NORMAL);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Nombre");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText("Precio");
                contentStream.newLineAtOffset(80, 0);
                contentStream.showText("Stock");
                contentStream.newLineAtOffset(80, 0);
                contentStream.showText("Proveedor");
                contentStream.endText();
                yPosition -= LINE_HEIGHT * 1.5f;

                // Table rows
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
                double totalValue = 0;
                int lowStockCount = 0;

                for (SparePart part : spareParts) {
                    if (yPosition < MARGIN + 50)
                        break; // Simplified pagination

                    if (part.isLowStock()) {
                        lowStockCount++;
                        contentStream.setNonStrokingColor(1f, 0.5f, 0f); // Orange for low stock
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(MARGIN, yPosition);
                    contentStream.showText(truncate(part.name(), 20));
                    contentStream.newLineAtOffset(150, 0);
                    contentStream.showText(String.format("₲%.2f", part.price()));
                    contentStream.newLineAtOffset(80, 0);
                    contentStream.showText(String.valueOf(part.stock()));
                    contentStream.newLineAtOffset(80, 0);
                    contentStream.showText(truncate(part.provider(), 15));
                    contentStream.endText();

                    contentStream.setNonStrokingColor(0f, 0f, 0f); // Reset to black
                    totalValue += part.price() * part.stock();
                    yPosition -= LINE_HEIGHT;
                }

                // Summary
                yPosition -= LINE_HEIGHT * 2;
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_NORMAL);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Total de repuestos: " + spareParts.size());
                contentStream.endText();
                yPosition -= LINE_HEIGHT;
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Valor total del inventario: ₲" + String.format("%.2f", totalValue));
                contentStream.endText();
                yPosition -= LINE_HEIGHT;
                contentStream.setNonStrokingColor(1f, 0.5f, 0f);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Repuestos con stock bajo: " + lowStockCount);
                contentStream.endText();
            }

            document.save(outputFile);
        }
    }

    private float drawTitle(PDPageContentStream contentStream, float yPosition, String title) throws IOException {
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_TITLE);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText(title);
        contentStream.endText();
        yPosition -= LINE_HEIGHT;

        // Date
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        contentStream.endText();

        return yPosition;
    }

    private String truncate(String text, int maxLength) {
        if (text == null)
            return "";
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}
