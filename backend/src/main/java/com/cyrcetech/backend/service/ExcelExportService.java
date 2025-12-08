package com.cyrcetech.backend.service;

import com.cyrcetech.backend.domain.entity.Ticket;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service for exporting data to Excel format.
 */
@Service
public class ExcelExportService {

    private static final Logger log = LoggerFactory.getLogger(ExcelExportService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Export tickets to Excel format.
     *
     * @param tickets List of tickets to export
     * @return byte array containing the Excel file
     * @throws IOException if export fails
     */
    public byte[] exportTicketsToExcel(List<Ticket> tickets) throws IOException {
        log.info("Exporting {} tickets to Excel", tickets.size());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Tickets");

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {
                    "ID", "Cliente", "Equipo", "Tipo Dispositivo", "Problema",
                    "Estado", "Costo Estimado", "Monto Pagado", "Balance",
                    "Fecha Creación", "Última Actualización", "Diagnóstico AI"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            int rowNum = 1;
            for (Ticket ticket : tickets) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(ticket.getId() != null ? ticket.getId() : "");
                row.createCell(1).setCellValue(ticket.getCustomer() != null ? ticket.getCustomer().getName() : "");
                row.createCell(2).setCellValue(ticket.getEquipment() != null ? ticket.getEquipment().getSummary() : "");
                row.createCell(3)
                        .setCellValue(ticket.getEquipment() != null && ticket.getEquipment().getDeviceType() != null
                                ? ticket.getEquipment().getDeviceType().getDisplayName()
                                : "");
                row.createCell(4)
                        .setCellValue(ticket.getProblemDescription() != null ? ticket.getProblemDescription() : "");
                row.createCell(5).setCellValue(ticket.getStatus() != null ? ticket.getStatus().getDisplayName() : "");
                row.createCell(6).setCellValue(ticket.getEstimatedCost());
                row.createCell(7).setCellValue(ticket.getAmountPaid());
                row.createCell(8).setCellValue(ticket.getRemainingBalance());

                Cell dateCreatedCell = row.createCell(9);
                if (ticket.getDateCreated() != null) {
                    dateCreatedCell.setCellValue(ticket.getDateCreated().format(DATE_FORMATTER));
                }

                Cell updatedCell = row.createCell(10);
                if (ticket.getUpdatedAt() != null) {
                    updatedCell.setCellValue(ticket.getUpdatedAt().format(DATETIME_FORMATTER));
                }

                row.createCell(11).setCellValue(ticket.getAiDiagnosis() != null ? ticket.getAiDiagnosis() : "");
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            log.info("Excel export completed successfully");
            return outputStream.toByteArray();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}
