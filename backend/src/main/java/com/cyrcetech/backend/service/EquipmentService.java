package com.cyrcetech.backend.service;

import com.cyrcetech.backend.domain.entity.Customer;
import com.cyrcetech.backend.domain.entity.DeviceType;
import com.cyrcetech.backend.domain.entity.Equipment;
import com.cyrcetech.backend.dto.request.CreateEquipmentRequest;
import com.cyrcetech.backend.dto.request.UpdateEquipmentRequest;
import com.cyrcetech.backend.dto.response.EquipmentResponse;
import com.cyrcetech.backend.exception.ResourceNotFoundException;
import com.cyrcetech.backend.repository.CustomerRepository;
import com.cyrcetech.backend.repository.EquipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service layer for Equipment business logic
 */
@Service
@Transactional
public class EquipmentService {

    private static final Logger log = LoggerFactory.getLogger(EquipmentService.class);

    private final EquipmentRepository equipmentRepository;
    private final CustomerRepository customerRepository;

    public EquipmentService(EquipmentRepository equipmentRepository, CustomerRepository customerRepository) {
        this.equipmentRepository = equipmentRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Get all equipment
     */
    public List<EquipmentResponse> getAllEquipment() {
        log.debug("Fetching all equipment");
        return equipmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get equipment by ID
     */
    public EquipmentResponse getEquipmentById(String id) {
        log.debug("Fetching equipment with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        return toResponse(equipment);
    }

    /**
     * Get equipment by customer ID
     */
    public List<EquipmentResponse> getEquipmentByCustomerId(String customerId) {
        log.debug("Fetching equipment for customer: {}", customerId);
        return equipmentRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get equipment by device type
     */
    public List<EquipmentResponse> getEquipmentByDeviceType(DeviceType deviceType) {
        log.debug("Fetching equipment by device type: {}", deviceType);
        return equipmentRepository.findByDeviceType(deviceType).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create new equipment
     */
    public EquipmentResponse createEquipment(CreateEquipmentRequest request) {
        log.debug("Creating new equipment: {} {}", request.getBrand(), request.getModel());

        // Validate customer exists
        String customerId = Objects.requireNonNull(request.getCustomerId(), "Customer ID cannot be null");
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Equipment equipment = new Equipment();
        equipment.setBrand(request.getBrand());
        equipment.setModel(request.getModel());
        equipment.setDeviceType(request.getDeviceType());
        equipment.setSerialNumber(request.getSerialNumber());
        equipment.setPhysicalCondition(request.getPhysicalCondition());
        equipment.setCustomer(customer);

        Equipment saved = equipmentRepository.save(equipment);
        log.info("Equipment created with id: {}", saved.getId());

        return toResponse(saved);
    }

    /**
     * Update existing equipment
     */
    public EquipmentResponse updateEquipment(String id, UpdateEquipmentRequest request) {
        log.debug("Updating equipment with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));

        if (request.getBrand() != null) {
            equipment.setBrand(request.getBrand());
        }
        if (request.getModel() != null) {
            equipment.setModel(request.getModel());
        }
        if (request.getDeviceType() != null) {
            equipment.setDeviceType(request.getDeviceType());
        }
        if (request.getSerialNumber() != null) {
            equipment.setSerialNumber(request.getSerialNumber());
        }
        if (request.getPhysicalCondition() != null) {
            equipment.setPhysicalCondition(request.getPhysicalCondition());
        }

        Equipment updated = equipmentRepository.save(Objects.requireNonNull(equipment));
        log.info("Equipment updated: {}", id);

        return toResponse(updated);
    }

    /**
     * Delete equipment
     */
    public void deleteEquipment(String id) {
        log.debug("Deleting equipment with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");

        if (!equipmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipment not found with id: " + id);
        }

        equipmentRepository.deleteById(id);
        log.info("Equipment deleted: {}", id);
    }

    /**
     * Search equipment
     */
    public List<EquipmentResponse> searchEquipment(String searchTerm) {
        log.debug("Searching equipment with term: {}", searchTerm);
        return equipmentRepository.searchEquipment(searchTerm).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert Equipment entity to EquipmentResponse DTO
     */
    private EquipmentResponse toResponse(Equipment equipment) {
        EquipmentResponse response = new EquipmentResponse();
        response.setId(equipment.getId());
        response.setBrand(equipment.getBrand());
        response.setModel(equipment.getModel());
        response.setDeviceType(equipment.getDeviceType());
        response.setSerialNumber(equipment.getSerialNumber());
        response.setPhysicalCondition(equipment.getPhysicalCondition());
        response.setCustomerId(equipment.getCustomer().getId());
        response.setCustomerName(equipment.getCustomer().getName());
        response.setSummary(equipment.getSummary());
        return response;
    }
}
