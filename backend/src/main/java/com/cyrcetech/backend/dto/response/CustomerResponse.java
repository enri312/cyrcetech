package com.cyrcetech.backend.dto.response;

import com.cyrcetech.backend.domain.entity.CustomerCategory;

/**
 * DTO for customer responses
 */
public class CustomerResponse {

    private String id;
    private String name;
    private String taxId;
    private String address;
    private String phone;
    private String formattedPhone;
    private String registrationDate;
    private CustomerCategory category;
    private String categoryDisplayName;
    private long seniorityDays;
    private String formattedSeniority;

    public CustomerResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFormattedPhone() {
        return formattedPhone;
    }

    public void setFormattedPhone(String formattedPhone) {
        this.formattedPhone = formattedPhone;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public CustomerCategory getCategory() {
        return category;
    }

    public void setCategory(CustomerCategory category) {
        this.category = category;
    }

    public String getCategoryDisplayName() {
        return categoryDisplayName;
    }

    public void setCategoryDisplayName(String categoryDisplayName) {
        this.categoryDisplayName = categoryDisplayName;
    }

    public long getSeniorityDays() {
        return seniorityDays;
    }

    public void setSeniorityDays(long seniorityDays) {
        this.seniorityDays = seniorityDays;
    }

    public String getFormattedSeniority() {
        return formattedSeniority;
    }

    public void setFormattedSeniority(String formattedSeniority) {
        this.formattedSeniority = formattedSeniority;
    }
}
