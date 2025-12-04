package com.cyrcetech.infrastructure.api.dto;

import com.cyrcetech.entity.DeviceType;

/**
 * DTO for creating/updating Equipment via API
 */
public class EquipmentRequestDTO {
    private String brand;
    private String model;
    private DeviceType deviceType;
    private String serialNumber;
    private String physicalCondition;
    private String customerId;

    public EquipmentRequestDTO() {
    }

    public EquipmentRequestDTO(String brand, String model, DeviceType deviceType, String serialNumber,
            String physicalCondition, String customerId) {
        this.brand = brand;
        this.model = model;
        this.deviceType = deviceType;
        this.serialNumber = serialNumber;
        this.physicalCondition = physicalCondition;
        this.customerId = customerId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPhysicalCondition() {
        return physicalCondition;
    }

    public void setPhysicalCondition(String physicalCondition) {
        this.physicalCondition = physicalCondition;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
