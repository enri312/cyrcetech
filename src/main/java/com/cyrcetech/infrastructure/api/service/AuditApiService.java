package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.infrastructure.api.ApiConfig;
import com.cyrcetech.infrastructure.api.dto.AuditLogDTO;
import com.cyrcetech.infrastructure.session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service to interact with Audit REST API
 */
public class AuditApiService {

    private final HttpClient httpClient;
    private final Gson gson;

    public AuditApiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // Custom Gson to handle LocalDateTime
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) -> {
                    String dateStr = json.getAsString();
                    if (dateStr == null || dateStr.isEmpty()) {
                        return null;
                    }
                    return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                })
                .create();
    }

    /**
     * Get all audit logs
     * 
     * @return List of audit logs
     * @throws Exception if request fails
     */
    public List<AuditLogDTO> getAllLogs() throws Exception {
        String url = ApiConfig.getBaseUrl() + "/audit";
        String json = getString(url);
        Type listType = new TypeToken<List<AuditLogDTO>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }

    private String getString(String url) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json");

        String token = SessionManager.getInstance().getToken();
        if (token != null && !token.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + token);
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new RuntimeException("HTTP Error " + response.statusCode() + ": " + response.body());
        }
    }
}
