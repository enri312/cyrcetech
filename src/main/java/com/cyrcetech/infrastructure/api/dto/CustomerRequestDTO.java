package com.cyrcetech.infrastructure.api.dto;

/**
 * DTO for creating/updating Customer via API
 */
public class CustomerRequestDTO {
    private String name;
    private String taxId;
    private String address;
    private String phone;

    // Constructors
    public CustomerRequestDTO() {
    }

    public CustomerRequestDTO(String name, String taxId, String address, String phone, boolean manualCategory) {
        this.name = name;
        this.taxId = taxId;
        this.address = address;
        this.phone = phone;
        this.manualCategory = manualCategory;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private boolean manualCategory;

    public boolean isManualCategory() {
        return manualCategory;
    }

    public void setManualCategory(boolean manualCategory) {
        this.manualCategory = manualCategory;
    }
}
