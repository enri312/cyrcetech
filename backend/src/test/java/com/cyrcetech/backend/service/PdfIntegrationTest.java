package com.cyrcetech.backend.service;

import com.cyrcetech.backend.dto.response.TicketResponse;
import com.cyrcepdf.core.PdfBuilder;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

public class PdfIntegrationTest {

    @Test
    public void generatePdf() throws Exception {
        PdfService service = new PdfService();

        TicketResponse ticket = new TicketResponse();
        ticket.setId("TICKET-12345678");
        ticket.setCustomerName("Juan Perez");
        ticket.setEquipmentSummary("Laptop Dell XPS 15");
        ticket.setProblemDescription("La pantalla parpadea y no enciende a veces.\nPosible problema de flex o GPU.");
        ticket.setAiDiagnosis("Fallo probable en el cable flex de video.\nRecomendado reemplazo.");
        ticket.setStatusDisplayName("En Progreso");
        ticket.setEstimatedCost(150000.0);
        ticket.setDateCreated(LocalDateTime.now().toString());

        byte[] pdfBytes = service.generateTicketPdf(ticket);

        File outFile = new File("test_ticket_generated.pdf");
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            fos.write(pdfBytes);
        }

        System.out.println("PDF generated at: " + outFile.getAbsolutePath());
    }
}
