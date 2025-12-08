package com.cyrcetech.backend.service;

import com.cyrcetech.backend.domain.entity.Customer;
import com.cyrcetech.backend.domain.entity.Equipment;
import com.cyrcetech.backend.domain.entity.Ticket;
import com.cyrcetech.backend.domain.entity.TicketStatus;
import com.cyrcetech.backend.dto.request.CreateTicketRequest;
import com.cyrcetech.backend.dto.request.UpdateTicketRequest;
import com.cyrcetech.backend.dto.response.TicketResponse;
import com.cyrcetech.backend.exception.ResourceNotFoundException;
import com.cyrcetech.backend.repository.CustomerRepository;
import com.cyrcetech.backend.repository.EquipmentRepository;
import com.cyrcetech.backend.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service layer for Ticket business logic
 */
@Service
@Transactional
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final EquipmentRepository equipmentRepository;
    private final WebhookService webhookService;
    private final ExcelExportService excelExportService;

    public TicketService(TicketRepository ticketRepository,
            CustomerRepository customerRepository,
            EquipmentRepository equipmentRepository,
            WebhookService webhookService,
            ExcelExportService excelExportService) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.equipmentRepository = equipmentRepository;
        this.webhookService = webhookService;
        this.excelExportService = excelExportService;
    }

    /**
     * Get all tickets
     */
    public List<TicketResponse> getAllTickets() {
        log.debug("Fetching all tickets");
        return ticketRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get ticket by ID
     */
    public TicketResponse getTicketById(String id) {
        log.debug("Fetching ticket with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return toResponse(ticket);
    }

    /**
     * Get tickets by customer ID
     */
    public List<TicketResponse> getTicketsByCustomerId(String customerId) {
        log.debug("Fetching tickets for customer: {}", customerId);
        return ticketRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get tickets by equipment ID
     */
    public List<TicketResponse> getTicketsByEquipmentId(String equipmentId) {
        log.debug("Fetching tickets for equipment: {}", equipmentId);
        return ticketRepository.findByEquipmentId(equipmentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get tickets by status
     */
    public List<TicketResponse> getTicketsByStatus(TicketStatus status) {
        log.debug("Fetching tickets with status: {}", status);
        return ticketRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get active tickets
     */
    public List<TicketResponse> getActiveTickets() {
        log.debug("Fetching active tickets");
        return ticketRepository.findActiveTickets().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create new ticket
     */
    public TicketResponse createTicket(CreateTicketRequest request) {
        log.debug("Creating new ticket for customer: {}", request.getCustomerId());

        // Validate customer exists
        String customerId = Objects.requireNonNull(request.getCustomerId(), "Customer ID cannot be null");
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        // Validate equipment exists
        String equipmentId = Objects.requireNonNull(request.getEquipmentId(), "Equipment ID cannot be null");
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipment not found with id: " + request.getEquipmentId()));

        Ticket ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setEquipment(equipment);
        ticket.setProblemDescription(request.getProblemDescription());
        ticket.setObservations(request.getObservations());
        ticket.setAiDiagnosis(request.getAiDiagnosis());

        if (request.getEstimatedCost() != null) {
            ticket.setEstimatedCost(request.getEstimatedCost());
        }

        Ticket saved = ticketRepository.save(ticket);
        log.info("Ticket created with id: {}", saved.getId());

        // Notify n8n
        if (webhookService != null) {
            webhookService.notifyTicketCreated(toResponse(saved));
        }

        return toResponse(saved);
    }

    /**
     * Update existing ticket
     */
    public TicketResponse updateTicket(String id, UpdateTicketRequest request) {
        log.debug("Updating ticket with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        if (request.getProblemDescription() != null) {
            ticket.setProblemDescription(request.getProblemDescription());
        }
        if (request.getObservations() != null) {
            ticket.setObservations(request.getObservations());
        }
        if (request.getStatus() != null) {
            ticket.setStatus(request.getStatus());
        }
        if (request.getAmountPaid() != null) {
            ticket.setAmountPaid(request.getAmountPaid());
        }
        if (request.getEstimatedCost() != null) {
            ticket.setEstimatedCost(request.getEstimatedCost());
        }
        if (request.getAiDiagnosis() != null) {
            ticket.setAiDiagnosis(request.getAiDiagnosis());
        }

        Ticket updated = ticketRepository.save(Objects.requireNonNull(ticket));
        log.info("Ticket updated: {}", id);

        return toResponse(updated);
    }

    /**
     * Delete ticket
     */
    public void deleteTicket(String id) {
        log.debug("Deleting ticket with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");

        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found with id: " + id);
        }

        ticketRepository.deleteById(id);
        log.info("Ticket deleted: {}", id);
    }

    /**
     * Search tickets
     */
    public List<TicketResponse> searchTickets(String searchTerm) {
        log.debug("Searching tickets with term: {}", searchTerm);
        return ticketRepository.searchTickets(searchTerm).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Export all tickets to Excel format.
     *
     * @return byte array containing the Excel file
     * @throws IOException if export fails
     */
    @Transactional(readOnly = true)
    public byte[] exportTicketsToExcel() throws IOException {
        log.info("Exporting all tickets to Excel");
        List<Ticket> tickets = ticketRepository.findAll();
        return excelExportService.exportTicketsToExcel(tickets);
    }

    /**
     * Convert Ticket entity to TicketResponse DTO
     */
    private TicketResponse toResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setCustomerId(ticket.getCustomer().getId());
        response.setCustomerName(ticket.getCustomer().getName());
        response.setEquipmentId(ticket.getEquipment().getId());
        response.setEquipmentSummary(ticket.getEquipment().getSummary());
        response.setProblemDescription(ticket.getProblemDescription());
        response.setObservations(ticket.getObservations());
        response.setStatus(ticket.getStatus());
        response.setStatusDisplayName(ticket.getStatus().getDisplayName());
        response.setStatusColorCode(ticket.getStatus().getColorCode());
        response.setAmountPaid(ticket.getAmountPaid());
        response.setEstimatedCost(ticket.getEstimatedCost());
        response.setRemainingBalance(ticket.getRemainingBalance());
        response.setFullyPaid(ticket.isFullyPaid());
        response.setDateCreated(ticket.getFormattedDate());
        response.setAiDiagnosis(ticket.getAiDiagnosis());
        return response;
    }
}
