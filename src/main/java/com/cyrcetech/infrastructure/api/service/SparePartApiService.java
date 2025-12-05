package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.entity.SparePart;
import com.cyrcetech.infrastructure.api.ApiClient;
import com.cyrcetech.infrastructure.api.ApiConfig;
import com.cyrcetech.infrastructure.api.dto.SparePartRequestDTO;
import com.cyrcetech.infrastructure.api.dto.SparePartResponseDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to interact with SparePart REST API
 */
public class SparePartApiService extends ApiClient {

    /**
     * Get all spare parts
     */
    public List<SparePart> getAllSpareParts() throws Exception {
        Type listType = new TypeToken<List<SparePartResponseDTO>>() {
        }.getType();
        String json = getString(ApiConfig.getSparePartsUrl());
        List<SparePartResponseDTO> dtos = getGson().fromJson(json, listType);

        List<SparePart> spareParts = new ArrayList<>();
        for (SparePartResponseDTO dto : dtos) {
            spareParts.add(toEntity(dto));
        }
        return spareParts;
    }

    /**
     * Get spare part by ID
     */
    public SparePart getSparePartById(String id) throws Exception {
        String url = ApiConfig.getSparePartsUrl() + "/" + id;
        SparePartResponseDTO dto = get(url, SparePartResponseDTO.class);
        return toEntity(dto);
    }

    /**
     * Create new spare part
     */
    public SparePart createSparePart(SparePart sparePart) throws Exception {
        SparePartRequestDTO request = toRequestDTO(sparePart);
        SparePartResponseDTO response = post(ApiConfig.getSparePartsUrl(), request, SparePartResponseDTO.class);
        return toEntity(response);
    }

    /**
     * Update existing spare part
     */
    public SparePart updateSparePart(String id, SparePart sparePart) throws Exception {
        String url = ApiConfig.getSparePartsUrl() + "/" + id;
        SparePartRequestDTO request = toRequestDTO(sparePart);
        SparePartResponseDTO response = put(url, request, SparePartResponseDTO.class);
        return toEntity(response);
    }

    /**
     * Delete spare part
     */
    public void deleteSparePart(String id) throws Exception {
        String url = ApiConfig.getSparePartsUrl() + "/" + id;
        delete(url);
    }

    /**
     * Search spare parts
     */
    public List<SparePart> searchSpareParts(String searchTerm) throws Exception {
        String url = ApiConfig.getSparePartsUrl() + "/search?q=" + searchTerm;
        Type listType = new TypeToken<List<SparePartResponseDTO>>() {
        }.getType();
        String json = getString(url);
        List<SparePartResponseDTO> dtos = getGson().fromJson(json, listType);

        List<SparePart> spareParts = new ArrayList<>();
        for (SparePartResponseDTO dto : dtos) {
            spareParts.add(toEntity(dto));
        }
        return spareParts;
    }

    /**
     * Convert DTO to Entity (Record)
     */
    private SparePart toEntity(SparePartResponseDTO dto) {
        return new SparePart(
                dto.getId(),
                dto.getName(),
                dto.getPrice(),
                dto.getStock(),
                dto.getProvider());
    }

    /**
     * Convert Entity (Record) to Request DTO
     */
    private SparePartRequestDTO toRequestDTO(SparePart sparePart) {
        return new SparePartRequestDTO(
                sparePart.name(),
                sparePart.price(),
                sparePart.stock(),
                sparePart.provider());
    }
}
