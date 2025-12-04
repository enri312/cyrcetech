package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.entity.DeviceType;
import com.cyrcetech.entity.Equipment;
import com.cyrcetech.infrastructure.api.ApiClient;
import com.cyrcetech.infrastructure.api.ApiConfig;
import com.cyrcetech.infrastructure.api.dto.EquipmentRequestDTO;
import com.cyrcetech.infrastructure.api.dto.EquipmentResponseDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to interact with Equipment REST API
 */
public class EquipmentApiService extends ApiClient {

    /**
     * Get all equipment
     */
    public List<Equipment> getAllEquipment() throws Exception {
        Type listType = new TypeToken<List<EquipmentResponseDTO>>() {
        }.getType();
        String json = get(ApiConfig.getEquipmentUrl(), String.class);
        List<EquipmentResponseDTO> dtos = getGson().fromJson(json, listType);

        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentResponseDTO dto : dtos) {
            equipmentList.add(toEntity(dto));
        }
        return equipmentList;
    }

    /**
     * Get equipment by ID
     */
    public Equipment getEquipmentById(String id) throws Exception {
        String url = ApiConfig.getEquipmentUrl() + "/" + id;
        EquipmentResponseDTO dto = get(url, EquipmentResponseDTO.class);
        return toEntity(dto);
    }

    /**
     * Get equipment by Customer ID
     */
    public List<Equipment> getEquipmentByCustomer(String customerId) throws Exception {
        String url = ApiConfig.getEquipmentUrl() + "/customer/" + customerId;
        Type listType = new TypeToken<List<EquipmentResponseDTO>>() {
        }.getType();
        String json = get(url, String.class);
        List<EquipmentResponseDTO> dtos = getGson().fromJson(json, listType);

        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentResponseDTO dto : dtos) {
            equipmentList.add(toEntity(dto));
        }
        return equipmentList;
    }

    /**
     * Create new equipment
     */
    public Equipment createEquipment(Equipment equipment) throws Exception {
        EquipmentRequestDTO request = toRequestDTO(equipment);
        EquipmentResponseDTO response = post(ApiConfig.getEquipmentUrl(), request, EquipmentResponseDTO.class);
        return toEntity(response);
    }

    /**
     * Update existing equipment
     */
    public Equipment updateEquipment(String id, Equipment equipment) throws Exception {
        String url = ApiConfig.getEquipmentUrl() + "/" + id;
        EquipmentRequestDTO request = toRequestDTO(equipment);
        EquipmentResponseDTO response = put(url, request, EquipmentResponseDTO.class);
        return toEntity(response);
    }

    /**
     * Delete equipment
     */
    public void deleteEquipment(String id) throws Exception {
        String url = ApiConfig.getEquipmentUrl() + "/" + id;
        delete(url);
    }

    /**
     * Search equipment
     */
    public List<Equipment> searchEquipment(String searchTerm) throws Exception {
        String url = ApiConfig.getEquipmentUrl() + "/search?q=" + searchTerm;
        Type listType = new TypeToken<List<EquipmentResponseDTO>>() {
        }.getType();
        String json = get(url, String.class);
        List<EquipmentResponseDTO> dtos = getGson().fromJson(json, listType);

        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentResponseDTO dto : dtos) {
            equipmentList.add(toEntity(dto));
        }
        return equipmentList;
    }

    /**
     * Convert DTO to Entity
     */
    private Equipment toEntity(EquipmentResponseDTO dto) {
        return new Equipment(
                dto.getId(),
                dto.getBrand(),
                dto.getModel(),
                dto.getDeviceType(),
                dto.getSerialNumber(),
                dto.getPhysicalCondition(),
                dto.getCustomerId());
    }

    /**
     * Convert Entity to Request DTO
     */
    private EquipmentRequestDTO toRequestDTO(Equipment equipment) {
        return new EquipmentRequestDTO(
                equipment.brand(),
                equipment.model(),
                equipment.deviceType(),
                equipment.serialNumber(),
                equipment.physicalCondition(),
                equipment.customerId());
    }
}
