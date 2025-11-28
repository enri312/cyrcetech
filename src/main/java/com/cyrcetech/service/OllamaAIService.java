package com.cyrcetech.service;

public class OllamaAIService implements AIService {
    @Override
    public String getDiagnosis(String deviceType, String problemDescription) {
        // Mock implementation for now, as requested to focus on structure/forms
        // In a real scenario, this would make an HTTP request to localhost:11434
        return "Sugerencia de IA (Mock): Revisar conexiones internas y voltaje para " + deviceType
                + ". Problema reportado: " + problemDescription;
    }
}
