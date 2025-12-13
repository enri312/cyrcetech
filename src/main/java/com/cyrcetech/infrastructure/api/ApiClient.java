package com.cyrcetech.infrastructure.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Base HTTP client for REST API calls
 */
public class ApiClient {

    private final HttpClient httpClient;
    private final Gson gson;

    public ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    /**
     * TypeAdapter for LocalDate to handle JSON serialization/deserialization
     */
    private static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.format(formatter));
            }
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            String dateStr = in.nextString();
            if (dateStr == null || dateStr.isEmpty()) {
                return null;
            }
            return LocalDate.parse(dateStr, formatter);
        }
    }

    /**
     * Perform GET request
     */
    public <T> T get(String url, Class<T> responseType) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json");

        addAuthHeader(requestBuilder);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return gson.fromJson(response.body(), responseType);
        } else {
            throw new RuntimeException("HTTP Error " + response.statusCode() + ": " + response.body());
        }
    }

    /**
     * Perform GET request and return raw JSON string (for lists)
     */
    public String getString(String url) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json");

        addAuthHeader(requestBuilder);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new RuntimeException("HTTP Error " + response.statusCode() + ": " + response.body());
        }
    }

    /**
     * Perform POST request
     */
    public <T> T post(String url, Object requestBody, Class<T> responseType) throws Exception {
        String jsonBody = gson.toJson(requestBody);
        System.out.println("DEBUG POST: URL=" + url);
        System.out.println("DEBUG POST: JSON=" + jsonBody);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json");

        addAuthHeader(requestBuilder);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return gson.fromJson(response.body(), responseType);
        } else {
            throw new RuntimeException("HTTP Error " + response.statusCode() + ": " + response.body());
        }
    }

    /**
     * Perform PUT request
     */
    public <T> T put(String url, Object requestBody, Class<T> responseType) throws Exception {
        String jsonBody = gson.toJson(requestBody);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json");

        addAuthHeader(requestBuilder);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return gson.fromJson(response.body(), responseType);
        } else {
            throw new RuntimeException("HTTP Error " + response.statusCode() + ": " + response.body());
        }
    }

    /**
     * Perform DELETE request
     */
    public void delete(String url) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Content-Type", "application/json");

        addAuthHeader(requestBuilder);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("HTTP Error " + response.statusCode() + ": " + response.body());
        }
    }

    /**
     * Perform GET request for a list
     */
    public <T> T getList(String url, java.lang.reflect.Type listType) throws Exception {
        String json = getString(url);
        return gson.fromJson(json, listType);
    }

    protected Gson getGson() {
        return gson;
    }

    /**
     * Perform GET request and return binary data (for file downloads like
     * PDF/Excel)
     */
    public byte[] getBytes(String url) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET();

        addAuthHeader(requestBuilder);

        HttpRequest request = requestBuilder.build();
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new RuntimeException("HTTP Error " + response.statusCode());
        }
    }

    /**
     * Adds authorization header to request builder if token exists.
     * This avoids the "wrong number of parameters" error when calling
     * headers() with an empty array.
     */
    private void addAuthHeader(HttpRequest.Builder requestBuilder) {
        String token = com.cyrcetech.infrastructure.session.SessionManager.getInstance().getToken();
        if (token != null && !token.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + token);
        }
    }
}
