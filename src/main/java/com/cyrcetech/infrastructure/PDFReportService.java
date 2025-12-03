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
    private static final float TITLE_FONT_SIZE = 18;
    private static final float HEADER_FONT_SIZE = 12;
    private static final float BODY_FONT_SIZE = 10;
    private static final float LINE_HEIGHT = 15;
    private static final float TABLE_ROW_HEIGHT = 20;

    public void generateOrdersReport(File outputFile) throws IOException {
        List<Ticket> tickets = ticketService.getAllTickets();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Title
                yPosition = drawTitle(contentStream, "Reporte de Órdenes", yPosition);
                yPosition -= LINE_HEIGHT;

                // Date
                yPosition = drawText(contentStream, "Fecha: " +
                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        MARGIN, yPosition, BODY_FONT_SIZE);
                yPosition -= LINE_HEIGHT * 2;

                // Table headers
                String[] headers = { "Cliente", "Equipo", "Estado", "Fecha" };
                float[] columnWidths = { 150, 150, 100, 100 };
                yPosition = drawTableHeader(contentStream, headers, columnWidths, yPosition);

                // Table rows
                for (Ticket ticket : tickets) {
                    if (yPosition < MARGIN + TABLE_ROW_HEIGHT) {
                        contentStream.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        PDPageContentStream newStream = new PDPageContentStream(document, page);
                        yPosition = page.getMediaBox().getHeight() - MARGIN;
                        yPosition = drawTableHeader(newStream, headers, columnWidths, yPosition);
                        contentStream.close();
                    }

                    String[] rowData = {
                            ticket.getCustomer().name(),
                            ticket.getEquipment().brand() + " " + ticket.getEquipment().model(),
                            ticket.getStatus().toString(),
                            ticket.getDateCreated().toString()
                    };
                    yPosition = drawTableRow(contentStream, rowData, columnWidths, yPosition);
                }

                // Summary
                yPosition -= LINE_HEIGHT * 2;
                yPosition = drawText(contentStream, "Total de órdenes: " + tickets.size(),
                        MARGIN, yPosition, HEADER_FONT_SIZE);
            }

            document.save(outputFile);
        }
    }

    public void generateCustomersReport(File outputFile) throws IOException {
        List<Customer> customers = customerService.getAllCustomers();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Title
                yPosition = drawTitle(contentStream, "Listado de Clientes", yPosition);
                yPosition -= LINE_HEIGHT;

                // Date
                yPosition = drawText(contentStream, "Fecha: " +
                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        MARGIN, yPosition, BODY_FONT_SIZE);
                yPosition -= LINE_HEIGHT * 2;

                // Table headers
                String[] headers = { "Nombre", "RUC / CI", "Teléfono", "Dirección" };
                float[] columnWidths = { 150, 100, 100, 150 };
                yPosition = drawTableHeader(contentStream, headers, columnWidths, yPosition);

                // Table rows
                for (Customer customer : customers) {
                    if (yPosition < MARGIN + TABLE_ROW_HEIGHT) {
                        contentStream.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        PDPageContentStream newStream = new PDPageContentStream(document, page);
                        yPosition = page.getMediaBox().getHeight() - MARGIN;
                        yPosition = drawTableHeader(newStream, headers, columnWidths, yPosition);
                        contentStream.close();
                    }

                    String[] rowData = {
                            customer.name(),
                            customer.taxId(),
                            customer.phone(),
                            customer.address()
                    };
                    yPosition = drawTableRow(contentStream, rowData, columnWidths, yPosition);
                }

                // Summary
                yPosition -= LINE_HEIGHT * 2;
                yPosition = drawText(contentStream, "Total de clientes: " + customers.size(),
                        MARGIN, yPosition, HEADER_FONT_SIZE);
            }

            document.save(outputFile);
        }
    }

    public void generateInventoryReport(File outputFile) throws IOException {
        List<SparePart> spareParts = sparePartService.getAllSpareParts();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Title
                yPosition = drawTitle(contentStream, "Reporte de Inventario", yPosition);
                yPosition -= LINE_HEIGHT;

                // Date
                yPosition = drawText(contentStream, "Fecha: " +
                        LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        MARGIN, yPosition, BODY_FONT_SIZE);
                yPosition -= LINE_HEIGHT * 2;

                // Table headers
                String[] headers = { "Nombre", "Precio", "Stock", "Proveedor" };
                float[] columnWidths = { 150, 100, 80, 170 };
                yPosition = drawTableHeader(contentStream, headers, columnWidths, yPosition);

                // Table rows
                double totalValue = 0;
                int lowStockCount = 0;

                for (SparePart part : spareParts) {
                    if (yPosition < MARGIN + TABLE_ROW_HEIGHT) {
                        contentStream.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        PDPageContentStream newStream = new PDPageContentStream(document, page);
                        yPosition = page.getMediaBox().getHeight() - MARGIN;
                        yPosition = drawTableHeader(newStream, headers, columnWidths, yPosition);
                        contentStream.close();
                    }

                    if (part.isLowStock()) {
                        lowStockCount++;
                    }

                    String[] rowData = {
                            part.name(),
                            "₲" + String.format("%.2f", part.price()),
                            String.valueOf(part.stock()),
                            part.provider()
                    };
                    yPosition = drawTableRow(contentStream, rowData, columnWidths, yPosition);

                    totalValue += part.price() * part.stock();
                }

                // Summary
                yPosition -= LINE_HEIGHT * 2;
                yPosition = drawText(contentStream, "Total de repuestos: " + spareParts.size(),
                        MARGIN, yPosition, HEADER_FONT_SIZE);
                yPosition -= LINE_HEIGHT;
                yPosition = drawText(contentStream, "Valor total del inventario: ₲" +
                        String.format("%.2f", totalValue), MARGIN, yPosition, HEADER_FONT_SIZE);
                yPosition -= LINE_HEIGHT;
                yPosition = drawText(contentStream, "Repuestos con stock bajo: " + lowStockCount,
                        MARGIN, yPosition, HEADER_FONT_SIZE);
            }

            document.save(outputFile);
        }
    }

    private float drawTitle(PDPageContentStream contentStream, String title, float yPosition)
            throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), TITLE_FONT_SIZE);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText(title);
        contentStream.endText();
        return yPosition - LINE_HEIGHT * 2;
    }

    private float drawText(PDPageContentStream contentStream, String text, float x, float y,
            float fontSize) throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        return y - LINE_HEIGHT;
    }

    private float drawTableHeader(PDPageContentStream contentStream, String[] headers,
            float[] columnWidths, float yPosition) throws IOException {
        float xPosition = MARGIN;

        // Draw header background
        contentStream.setNonStrokingColor(0.2f, 0.3f, 0.4f);
        contentStream.addRect(MARGIN, yPosition - TABLE_ROW_HEIGHT + 5,
                getTotalWidth(columnWidths), TABLE_ROW_HEIGHT);
        contentStream.fill();

        // Draw header text
        contentStream.beginText();
        contentStream.setNonStrokingColor(1f, 1f, 1f);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), HEADER_FONT_SIZE);

        for (int i = 0; i < headers.length; i++) {
            contentStream.newLineAtOffset(xPosition, yPosition - TABLE_ROW_HEIGHT + 10);
            contentStream.showText(headers[i]);
            contentStream.newLineAtOffset(-xPosition, -(yPosition - TABLE_ROW_HEIGHT + 10));
            xPosition += columnWidths[i];
        }
        contentStream.endText();

        contentStream.setNonStrokingColor(0f, 0f, 0f);
        return yPosition - TABLE_ROW_HEIGHT;
    }

    private float drawTableRow(PDPageContentStream contentStream, String[] rowData,
            float[] columnWidths, float yPosition) throws IOException {
        float xPosition = MARGIN;

        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), BODY_FONT_SIZE);

        for (int i = 0; i < rowData.length; i++) {
            contentStream.newLineAtOffset(xPosition, yPosition - TABLE_ROW_HEIGHT + 10);
            String text = rowData[i] != null ? rowData[i] : "";
            // Truncate text if too long
            if (text.length() > 30) {
                text = text.substring(0, 27) + "...";
            }
            contentStream.showText(text);
            contentStream.newLineAtOffset(-xPosition, -(yPosition - TABLE_ROW_HEIGHT + 10));
            xPosition += columnWidths[i];
        }
        contentStream.endText();

        // Draw row separator
        contentStream.setStrokingColor(0.9f, 0.9f, 0.9f);
        contentStream.moveTo(MARGIN, yPosition - TABLE_ROW_HEIGHT);
        contentStream.lineTo(MARGIN + getTotalWidth(columnWidths), yPosition - TABLE_ROW_HEIGHT);
        contentStream.stroke();

        return yPosition - TABLE_ROW_HEIGHT;
    }

    private float getTotalWidth(float[] columnWidths) {
        float total = 0;
        for (float width : columnWidths) {
            total += width;
        }
        return total;
    }
}
