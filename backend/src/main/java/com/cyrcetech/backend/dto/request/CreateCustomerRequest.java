package com.cyrcetech.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating a new customer
 */
public class CreateCustomerRequest {

    @NotBlank(message = "Customer name is required")
    private String name;

    private String taxId;

    private String address;

    private String phone;

    public CreateCustomerRequest() {
    }

    public CreateCustomerRequest(String name, String taxId, String address, String phone) {
        this.name = name;
        this.taxId = taxId;
        this.address = address;
        this.phone = phone;
    }

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
}
