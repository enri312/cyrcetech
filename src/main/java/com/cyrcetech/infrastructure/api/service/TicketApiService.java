package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.entity.Customer;
import com.cyrcetech.entity.Equipment;
import com.cyrcetech.entity.Ticket;
import com.cyrcetech.infrastructure.api.ApiClient;
import com.cyrcetech.infrastructure.api.ApiConfig;
import com.cyrcetech.infrastructure.api.dto.TicketRequestDTO;
import com.cyrcetech.infrastructure.api.dto.TicketResponseDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to interact with Ticket REST API
 */
public class TicketApiService extends ApiClient {

    private final CustomerApiService customerApiService = new CustomerApiService();
    private final EquipmentApiService equipmentApiService = new EquipmentApiService();

    /**
     * Get all tickets
     */
    public List<Ticket> getAllTickets() throws Exception {
        Type listType = new TypeToken<List<TicketResponseDTO>>() {
        }.getType();
        String json = getString(ApiConfig.getTicketsUrl());
        List<TicketResponseDTO> dtos = getGson().fromJson(json, listType);

        List<Ticket> tickets = new ArrayList<>();
        for (TicketResponseDTO dto : dtos) {
            tickets.add(toEntity(dto));
        }
        return tickets;
    }

    /**
     * Get ticket by ID
     */
    public Ticket getTicketById(String id) throws Exception {
        String url = ApiConfig.getTicketsUrl() + "/" + id;
        TicketResponseDTO dto = get(url, TicketResponseDTO.class);
        return toEntity(dto);
    }

    /**
     * Get active tickets
     */
    public List<Ticket> getActiveTickets() throws Exception {
        String url = ApiConfig.getTicketsUrl() + "/active";
        Type listType = new TypeToken<List<TicketResponseDTO>>() {
        }.getType();
        String json = getString(url);
        List<TicketResponseDTO> dtos = getGson().fromJson(json, listType);

        List<Ticket> tickets = new ArrayList<>();
        for (TicketResponseDTO dto : dtos) {
            tickets.add(toEntity(dto));
        }
        return tickets;
    }

    /**
     * Create new ticket
     */
    public Ticket createTicket(Ticket ticket) throws Exception {
        TicketRequestDTO request = toRequestDTO(ticket);
        TicketResponseDTO response = post(ApiConfig.getTicketsUrl(), request, TicketResponseDTO.class);
        return toEntity(response);
    }

    /**
     * Update existing ticket
     */
    public Ticket updateTicket(String id, Ticket ticket) throws Exception {
        String url = ApiConfig.getTicketsUrl() + "/" + id;
        TicketRequestDTO request = toRequestDTO(ticket);
        TicketResponseDTO response = put(url, request, TicketResponseDTO.class);
        return toEntity(response);
    }

    /**
     * Delete ticket
     */
    public void deleteTicket(String id) throws Exception {
        String url = ApiConfig.getTicketsUrl() + "/" + id;
        delete(url);
    }

    /**
     * Search tickets
     */
    public List<Ticket> searchTickets(String searchTerm) throws Exception {
        String url = ApiConfig.getTicketsUrl() + "/search?q=" + searchTerm;
        Type listType = new TypeToken<List<TicketResponseDTO>>() {
        }.getType();
        String json = getString(url);
        List<TicketResponseDTO> dtos = getGson().fromJson(json, listType);

        List<Ticket> tickets = new ArrayList<>();
        for (TicketResponseDTO dto : dtos) {
            tickets.add(toEntity(dto));
        }
        return tickets;
    }

    /**
     * Convert DTO to Entity
     * Note: This involves fetching Customer and Equipment details, which might be
     * slow for large lists.
     * Optimization: In a real app, we might want to cache these or use a lighter
     * entity for lists.
     */
    private Ticket toEntity(TicketResponseDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setId(dto.getId());
        ticket.setProblemDescription(dto.getProblemDescription());
        ticket.setObservations(dto.getObservations());
        ticket.setStatus(dto.getStatus());
        ticket.setAmountPaid(dto.getAmountPaid());
        ticket.setEstimatedCost(dto.getEstimatedCost());
        ticket.setAiDiagnosis(dto.getAiDiagnosis());

        if (dto.getDateCreated() != null) {
            try {
                ticket.setDateCreated(LocalDate.parse(dto.getDateCreated()));
            } catch (Exception e) {
                // Fallback or log error
                ticket.setDateCreated(LocalDate.now());
            }
        }

        // Fetch related entities (Customer and Equipment)
        // For performance in lists, we might want to lazy load or use placeholders if
        // possible.
        // For now, we'll fetch them to ensure the UI works as expected.
        try {
            if (dto.getCustomerId() != null) {
                Customer customer = customerApiService.getCustomerById(dto.getCustomerId());
                ticket.setCustomer(customer);
            }
            if (dto.getEquipmentId() != null) {
                Equipment equipment = equipmentApiService.getEquipmentById(dto.getEquipmentId());
                ticket.setEquipment(equipment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error gracefully?
        }

        return ticket;
    }

    /**
     * Convert Entity to Request DTO
     */
    private TicketRequestDTO toRequestDTO(Ticket ticket) {
        return new TicketRequestDTO(
                ticket.getCustomer() != null ? ticket.getCustomer().id() : null,
                ticket.getEquipment() != null ? ticket.getEquipment().id() : null,
                ticket.getProblemDescription(),
                ticket.getObservations(),
                ticket.getEstimatedCost(),
                ticket.getAiDiagnosis(),
                ticket.getStatus(),
                ticket.getAmountPaid());
    }
}
