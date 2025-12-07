package com.cyrcetech.backend.service;

import com.cyrcetech.backend.domain.entity.SparePart;
import com.cyrcetech.backend.dto.request.CreateSparePartRequest;
import com.cyrcetech.backend.dto.request.UpdateSparePartRequest;
import com.cyrcetech.backend.dto.response.SparePartResponse;
import com.cyrcetech.backend.exception.ResourceNotFoundException;
import com.cyrcetech.backend.repository.SparePartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service layer for SparePart business logic
 */
@Service
@Transactional
public class SparePartService {

    private static final Logger log = LoggerFactory.getLogger(SparePartService.class);

    private final SparePartRepository sparePartRepository;

    public SparePartService(SparePartRepository sparePartRepository) {
        this.sparePartRepository = sparePartRepository;
    }

    /**
     * Get all spare parts
     */
    public List<SparePartResponse> getAllSpareParts() {
        log.debug("Fetching all spare parts");
        return sparePartRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get spare part by ID
     */
    public SparePartResponse getSparePartById(String id) {
        log.debug("Fetching spare part with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");
        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spare part not found with id: " + id));
        return toResponse(sparePart);
    }

    /**
     * Get spare parts with low stock
     */
    public List<SparePartResponse> getLowStockSpareParts() {
        log.debug("Fetching spare parts with low stock");
        return sparePartRepository.findLowStock().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get spare parts out of stock
     */
    public List<SparePartResponse> getOutOfStockSpareParts() {
        log.debug("Fetching spare parts out of stock");
        return sparePartRepository.findOutOfStock().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get spare parts in stock
     */
    public List<SparePartResponse> getInStockSpareParts() {
        log.debug("Fetching spare parts in stock");
        return sparePartRepository.findInStock().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create new spare part
     */
    public SparePartResponse createSparePart(CreateSparePartRequest request) {
        log.debug("Creating new spare part: {}", request.getName());

        SparePart sparePart = new SparePart();
        sparePart.setName(request.getName());
        sparePart.setPrice(request.getPrice() != null ? request.getPrice() : 0.0);
        sparePart.setStock(request.getStock() != null ? request.getStock() : 0);
        sparePart.setProvider(request.getProvider());

        SparePart saved = sparePartRepository.save(sparePart);
        log.info("Spare part created with id: {}", saved.getId());

        return toResponse(saved);
    }

    /**
     * Update existing spare part
     */
    public SparePartResponse updateSparePart(String id, UpdateSparePartRequest request) {
        log.debug("Updating spare part with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");

        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spare part not found with id: " + id));

        if (request.getName() != null) {
            sparePart.setName(request.getName());
        }
        if (request.getPrice() != null) {
            sparePart.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            sparePart.setStock(request.getStock());
        }
        if (request.getProvider() != null) {
            sparePart.setProvider(request.getProvider());
        }

        SparePart updated = sparePartRepository.save(Objects.requireNonNull(sparePart));
        log.info("Spare part updated: {}", id);

        return toResponse(updated);
    }

    /**
     * Delete spare part
     */
    public void deleteSparePart(String id) {
        log.debug("Deleting spare part with id: {}", id);
        Objects.requireNonNull(id, "Id cannot be null");

        if (!sparePartRepository.existsById(id)) {
            throw new ResourceNotFoundException("Spare part not found with id: " + id);
        }

        sparePartRepository.deleteById(id);
        log.info("Spare part deleted: {}", id);
    }

    /**
     * Search spare parts
     */
    public List<SparePartResponse> searchSpareParts(String searchTerm) {
        log.debug("Searching spare parts with term: {}", searchTerm);
        return sparePartRepository.searchSpareParts(searchTerm).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert SparePart entity to SparePartResponse DTO
     */
    private SparePartResponse toResponse(SparePart sparePart) {
        SparePartResponse response = new SparePartResponse();
        response.setId(sparePart.getId());
        response.setName(sparePart.getName());
        response.setPrice(sparePart.getPrice());
        response.setFormattedPrice(sparePart.getFormattedPrice());
        response.setStock(sparePart.getStock());
        response.setProvider(sparePart.getProvider());
        response.setInStock(sparePart.isInStock());
        response.setLowStock(sparePart.isLowStock());
        return response;
    }
}
