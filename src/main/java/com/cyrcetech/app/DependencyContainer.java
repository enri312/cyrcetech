package com.cyrcetech.app;

import com.cyrcetech.infrastructure.ai.OllamaAIService;
import com.cyrcetech.usecase.AIService;
import com.cyrcetech.usecase.TicketService;
import com.cyrcetech.usecase.impl.TicketServiceImpl;

/**
 * Simple Dependency Injection Container.
 * Manages singleton instances of services.
 */
public class DependencyContainer {

    private static final TicketService ticketService = new TicketServiceImpl();
    private static final AIService aiService = new OllamaAIService();

    public static TicketService getTicketService() {
        return ticketService;
    }

    public static AIService getAiService() {
        return aiService;
    }
}
