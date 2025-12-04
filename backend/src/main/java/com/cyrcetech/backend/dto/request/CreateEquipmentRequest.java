package com.cyrcetech.backend.dto.request;

import com.cyrcetech.backend.domain.entity.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating new equipment
 */
public class CreateEquipmentRequest {

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Device type is required")
    private DeviceType deviceType;

    private String serialNumber;

    private String physicalCondition;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    // Constructors
    public CreateEquipmentRequest() {
    }

    public CreateEquipmentRequest(String brand, String model, DeviceType deviceType,
            String serialNumber, String physicalCondition, String customerId) {
        this.brand = brand;
        this.model = model;
        this.deviceType = deviceType;
        this.serialNumber = serialNumber;
        this.physicalCondition = physicalCondition;
        this.customerId = customerId;
    }

    // Getters and Setters
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
