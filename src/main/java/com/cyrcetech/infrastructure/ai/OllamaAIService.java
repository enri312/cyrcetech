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
    private static final String MODEL_NAME = "phi4-mini";
    private final HttpClient httpClient;
    private final Gson gson;

    public OllamaAIService() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofMinutes(5)) // 5 minutes for large models
                .build();
        this.gson = new Gson();
    }

    @Override
    public String getDiagnosis(String deviceType, String problemDescription) {
        // Shorter prompt for faster response
        String prompt = String.format(
                "Eres un técnico de reparación. Diagnóstico breve para:\\n" +
                        "Dispositivo: %s\\n" +
                        "Problema: %s\\n" +
                        "Da 2-3 posibles causas y soluciones, en español, texto plano:",
                deviceType, problemDescription);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", MODEL_NAME);
        requestBody.addProperty("prompt", prompt);
        requestBody.addProperty("stream", false);
        // Limit response length for faster generation
        JsonObject options = new JsonObject();
        options.addProperty("num_predict", 500); // Limit tokens
        requestBody.add("options", options);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_API_URL))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofMinutes(5)) // 5 minutes timeout
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                if (jsonResponse.has("response")) {
                    String result = jsonResponse.get("response").getAsString().trim();
                    // Remove thinking tags if present
                    result = result.replaceAll("<think>.*?</think>", "").trim();
                    return result;
                } else {
                    return "Error: Respuesta inesperada de Ollama.";
                }
            } else {
                return "Error al conectar con Ollama (Código: " + response.statusCode()
                        + "). Asegúrate de que Ollama esté corriendo.";
            }
        } catch (java.net.http.HttpTimeoutException e) {
            return "El modelo está tardando demasiado. Intenta con un prompt más corto o verifica que Ollama tenga suficiente memoria.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error de conexión con IA Local: " + e.getMessage()
                    + ". Verifica que Ollama esté ejecutándose (ollama serve).";
        }
    }
}
