package com.cyrcetech.backend.dto.response;

import com.cyrcetech.backend.domain.entity.DeviceType;

/**
 * DTO for equipment response
 */
public class EquipmentResponse {

    private String id;
    private String brand;
    private String model;
    private DeviceType deviceType;
    private String serialNumber;
    private String physicalCondition;
    private String customerId;
    private String customerName;
    private String summary;

    // Constructors
    public EquipmentResponse() {
    }

    public EquipmentResponse(String id, String brand, String model, DeviceType deviceType,
            String serialNumber, String physicalCondition, String customerId,
            String customerName, String summary) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.deviceType = deviceType;
        this.serialNumber = serialNumber;
        this.physicalCondition = physicalCondition;
        this.customerId = customerId;
        this.customerName = customerName;
        this.summary = summary;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
