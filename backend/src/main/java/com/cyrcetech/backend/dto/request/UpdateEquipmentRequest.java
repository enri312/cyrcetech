package com.cyrcetech.backend.dto.request;

import com.cyrcetech.backend.domain.entity.DeviceType;

/**
 * DTO for updating equipment
 */
public class UpdateEquipmentRequest {

    private String brand;
    private String model;
    private DeviceType deviceType;
    private String serialNumber;
    private String physicalCondition;

    // Constructors
    public UpdateEquipmentRequest() {
    }

    public UpdateEquipmentRequest(String brand, String model, DeviceType deviceType,
            String serialNumber, String physicalCondition) {
        this.brand = brand;
        this.model = model;
        this.deviceType = deviceType;
        this.serialNumber = serialNumber;
        this.physicalCondition = physicalCondition;
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
}
