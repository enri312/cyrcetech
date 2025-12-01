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
    private static final com.cyrcetech.usecase.CustomerService customerService = new com.cyrcetech.usecase.impl.CustomerServiceImpl();
    private static final com.cyrcetech.usecase.EquipmentService equipmentService = new com.cyrcetech.usecase.impl.EquipmentServiceImpl();
    private static final com.cyrcetech.usecase.SparePartService sparePartService = new com.cyrcetech.usecase.impl.SparePartServiceImpl();

    public static TicketService getTicketService() {
        return ticketService;
    }

    public static AIService getAiService() {
        return aiService;
    }

    public static com.cyrcetech.usecase.CustomerService getCustomerService() {
        return customerService;
    }

    public static com.cyrcetech.usecase.EquipmentService getEquipmentService() {
        return equipmentService;
    }

    public static com.cyrcetech.usecase.SparePartService getSparePartService() {
        return sparePartService;
    }
}
