package com.cyrcetech.infrastructure.ai;

import com.cyrcetech.usecase.AIService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class OllamaAIService implements AIService {

    private static final String OLLAMA_API_URL = "http://localhost:11434/api/generate";
    private static final String MODEL_NAME = "deepseek-r1:8b"; // Updated based on user feedback
    private final HttpClient httpClient;
    private final Gson gson;

    public OllamaAIService() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    @Override
    public String getDiagnosis(String deviceType, String problemDescription) {
        String prompt = String.format(
                "Actúa como un técnico experto en reparación de computadoras y dispositivos electrónicos. " +
                        "Diagnostica el siguiente problema y sugiere posibles soluciones breves y concisas.\n\n" +
                        "Dispositivo: %s\n" +
                        "Problema: %s\n\n" +
                        "Respuesta (solo texto plano, sin markdown):",
                deviceType, problemDescription);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", MODEL_NAME);
        requestBody.addProperty("prompt", prompt);
        requestBody.addProperty("stream", false);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                if (jsonResponse.has("response")) {
                    return jsonResponse.get("response").getAsString().trim();
                } else {
                    return "Error: Respuesta inesperada de Ollama.";
                }
            } else {
                return "Error al conectar con Ollama (Código: " + response.statusCode()
                        + "). Asegúrate de que Ollama esté corriendo.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error de conexión con IA Local: " + e.getMessage()
                    + ". Verifica que Ollama esté ejecutándose (ollama serve).";
        }
    }
}
