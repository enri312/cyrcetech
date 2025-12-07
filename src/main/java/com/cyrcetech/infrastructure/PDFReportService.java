package com.cyrcetech.infrastructure;

import com.cyrcetech.entity.Customer;
import com.cyrcetech.entity.SparePart;
import com.cyrcetech.entity.Ticket;
import com.cyrcetech.infrastructure.api.service.CustomerApiService;
import com.cyrcetech.infrastructure.api.service.SparePartApiService;
import com.cyrcetech.infrastructure.api.service.TicketApiService;
import com.cyrcepdf.core.PdfBuilder;
import com.cyrcepdf.core.StandardFont;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PDFReportService {

    private final CustomerApiService customerApiService = new CustomerApiService();
    private final TicketApiService ticketApiService = new TicketApiService();
    private final SparePartApiService sparePartApiService = new SparePartApiService();

    private static final double MARGIN = 50;
    private static final double ROW_HEIGHT = 20;

    public void generateOrdersReport(File outputFile) throws IOException {
        List<Ticket> tickets;
        try {
            tickets = ticketApiService.getAllTickets();
        } catch (Exception e) {
            throw new IOException("Error fetching tickets from API: " + e.getMessage(), e);
        }

        PdfBuilder builder = PdfBuilder.create().title("Reporte de Órdenes");
        List<List<Ticket>> chunks = partition(tickets, 25);

        if (chunks.isEmpty()) {
            builder.addPage(page -> page.text("No hay órdenes.", MARGIN, 800));
        }

        for (int i = 0; i < chunks.size(); i++) {
            List<Ticket> chunk = chunks.get(i);
            boolean isFirst = (i == 0);

            builder.addPage(page -> {
                double y = 800;
                if (isFirst) {
                    page.text("Reporte de Órdenes", MARGIN, y, StandardFont.HELVETICA_BOLD, 18);
                    y -= 30;
                    page.text("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), MARGIN, y);
                    y -= 40;
                }

                // Header
                drawRow(page, new String[] { "Cliente", "Equipo", "Estado", "Fecha" },
                        new double[] { 150, 150, 100, 100 }, y, true);
                y -= ROW_HEIGHT;

                for (Ticket ticket : chunk) {
                    String[] rowData = {
                            ticket.getCustomer().name(),
                            ticket.getEquipment().brand() + " " + ticket.getEquipment().model(),
                            ticket.getStatus().toString(),
                            ticket.getDateCreated().toString()
                    };
                    drawRow(page, rowData, new double[] { 150, 150, 100, 100 }, y, false);
                    y -= ROW_HEIGHT;
                }

                // Summary on last page if space
                if (isFirst && chunks.size() == 1) { // Simple summary for now
                    y -= 20;
                    page.text("Total de órdenes: " + tickets.size(), MARGIN, y);
                }
            });
        }
        builder.save(outputFile.getAbsolutePath());
    }

    public void generateCustomersReport(File outputFile) throws IOException {
        List<Customer> customers;
        try {
            customers = customerApiService.getAllCustomers();
        } catch (Exception e) {
            throw new IOException("Error fetching customers from API: " + e.getMessage(), e);
        }

        PdfBuilder builder = PdfBuilder.create().title("Listado de Clientes");
        List<List<Customer>> chunks = partition(customers, 25);

        for (int i = 0; i < chunks.size(); i++) {
            List<Customer> chunk = chunks.get(i);
            boolean isFirst = (i == 0);

            builder.addPage(page -> {
                double y = 800;
                if (isFirst) {
                    page.text("Listado de Clientes", MARGIN, y, StandardFont.HELVETICA_BOLD, 18);
                    y -= 30;
                    page.text("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), MARGIN, y);
                    y -= 40;
                }

                drawRow(page, new String[] { "Nombre", "RUC / CI", "Teléfono", "Dirección" },
                        new double[] { 150, 100, 100, 150 }, y, true);
                y -= ROW_HEIGHT;

                for (Customer c : chunk) {
                    drawRow(page, new String[] { c.name(), c.taxId(), c.phone(), c.address() },
                            new double[] { 150, 100, 100, 150 }, y, false);
                    y -= ROW_HEIGHT;
                }

                if (isFirst && chunks.size() == 1) {
                    y -= 20;
                    page.text("Total de clientes: " + customers.size(), MARGIN, y);
                }
            });
        }
        builder.save(outputFile.getAbsolutePath());
    }

    public void generateInventoryReport(File outputFile) throws IOException {
        List<SparePart> parts;
        try {
            parts = sparePartApiService.getAllSpareParts();
        } catch (Exception e) {
            throw new IOException("Error fetching parts: " + e.getMessage(), e);
        }

        PdfBuilder builder = PdfBuilder.create().title("Reporte de Inventario");
        List<List<SparePart>> chunks = partition(parts, 25);

        for (int i = 0; i < chunks.size(); i++) {
            List<SparePart> chunk = chunks.get(i);
            boolean isFirst = (i == 0);

            builder.addPage(page -> {
                double y = 800;
                if (isFirst) {
                    page.text("Reporte de Inventario", MARGIN, y, StandardFont.HELVETICA_BOLD, 18);
                    y -= 70;
                }

                drawRow(page, new String[] { "Nombre", "Precio", "Stock", "Proveedor" },
                        new double[] { 150, 100, 80, 170 }, y, true);
                y -= ROW_HEIGHT;

                for (SparePart p : chunk) {
                    drawRow(page, new String[] { p.name(), "G" + p.price(), String.valueOf(p.stock()), p.provider() },
                            new double[] { 150, 100, 80, 170 }, y, false);
                    y -= ROW_HEIGHT;
                }
            });
        }
        builder.save(outputFile.getAbsolutePath());
    }

    private void drawRow(com.cyrcepdf.core.PdfBuilder.Page page, String[] data, double[] widths, double y,
            boolean isHeader) {
        double x = MARGIN;
        for (int i = 0; i < data.length; i++) {
            String text = data[i] != null ? data[i] : "";
            if (text.length() > 30)
                text = text.substring(0, 27) + "...";

            if (isHeader) {
                page.text(text, x, y, StandardFont.HELVETICA_BOLD, 12);
            } else {
                page.text(text, x, y, StandardFont.HELVETICA, 10);
            }
            x += widths[i];
        }
        // Line separator
        double totalWidth = 0;
        for (double w : widths)
            totalWidth += w;
        page.line(MARGIN, y - 5, MARGIN + totalWidth, y - 5);
    }

    private <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> parts = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            parts.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return parts;
    }
}
