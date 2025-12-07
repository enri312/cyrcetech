package com.cyrcetech.backend.service;

import com.cyrcetech.backend.dto.response.TicketResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class WebhookService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    // In a real app, configured via application.properties
    private static final String N8N_WEBHOOK_URL = "http://localhost:5678/webhook/ticket-created";

    public WebhookService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Async
    public void notifyTicketCreated(TicketResponse ticket) {
        try {
            String jsonBody = objectMapper.writeValueAsString(ticket);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(N8N_WEBHOOK_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() >= 200 && response.statusCode() < 300) {
                            System.out.println("Webhook sent successfully to n8n: " + response.statusCode());
                        } else {
                            System.err.println("Failed to send webhook to n8n: " + response.statusCode());
                        }
                    })
                    .exceptionally(e -> {
                        System.err.println("Error sending webhook: " + e.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            System.err.println("Error preparing webhook: " + e.getMessage());
        }
    }
}
