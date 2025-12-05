package com.cyrcetech.app;

import com.cyrcetech.infrastructure.ai.OllamaAIService;
import com.cyrcetech.usecase.AIService;

/**
 * Simple Dependency Injection Container.
 * Manages singleton instances of services.
 * 
 * Note: Most services have been migrated to REST API.
 * Only AIService remains as it connects to local Ollama instance.
 */
public class DependencyContainer {

    private static final AIService aiService = new OllamaAIService();

    public static AIService getAiService() {
        return aiService;
    }
}
