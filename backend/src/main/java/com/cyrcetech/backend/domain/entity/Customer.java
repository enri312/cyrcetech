package com.cyrcetech.backend.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Customer entity representing a client in the system.
 * Migrated from Java record to JPA entity for database persistence.
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "tax_id")
    private String taxId;

    private String address;

    private String phone;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private CustomerCategory category;

    @Column(name = "manual_category", nullable = false)
    private boolean manualCategory;

    public Customer() {
        this.registrationDate = LocalDate.now();
        this.category = CustomerCategory.NUEVO;
        this.manualCategory = false;
    }

    public Customer(String id, String name, String taxId, String address, String phone) {
        this();
        this.id = id;
        this.name = name;
        this.taxId = taxId;
        this.address = address;
        this.phone = phone;
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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public CustomerCategory getCategory() {
        return category;
    }

    public void setCategory(CustomerCategory category) {
        this.category = category;
    }

    /**
     * Calculates the seniority (days since registration).
     *
     * @return Number of days since registration
     */
    public long getSeniorityDays() {
        if (registrationDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(registrationDate, LocalDate.now());
    }

    /**
     * Returns formatted seniority string (e.g., "3 meses", "1 año 2 meses").
     *
     * @return Human-readable seniority string
     */
    public String getFormattedSeniority() {
        long days = getSeniorityDays();
        if (days < 30) {
            return days + " días";
        }
        long months = days / 30;
        if (months < 12) {
            return months + (months == 1 ? " mes" : " meses");
        }
        long years = months / 12;
        long remainingMonths = months % 12;
        if (remainingMonths == 0) {
            return years + (years == 1 ? " año" : " años");
        }
        return years + (years == 1 ? " año " : " años ") + remainingMonths + (remainingMonths == 1 ? " mes" : " meses");
    }

    public void updateCategory() {
        if (!manualCategory) {
            this.category = CustomerCategory.fromDays(getSeniorityDays());
        }
    }

    public boolean isManualCategory() {
        return manualCategory;
    }

    public void setManualCategory(boolean manualCategory) {
        this.manualCategory = manualCategory;
    }

    /**
     * Returns formatted phone number for display
     */
    public String getFormattedPhone() {
        if (phone == null || phone.isEmpty()) {
            return "";
        }
        // Simple formatting: (###) ###-#### for 10 digits
        String cleanPhone = phone.replaceAll("[\\s-]", "");
        if (cleanPhone.length() == 10) {
            return String.format("(%s) %s-%s",
                    cleanPhone.substring(0, 3),
                    cleanPhone.substring(3, 6),
                    cleanPhone.substring(6));
        }
        return phone;
    }

    /**
     * Normalizes phone number and sets registration date before persisting
     */
    @PrePersist
    private void prePersist() {
        if (phone != null) {
            phone = phone.replaceAll("[\\s-]", "");
        }
        if (registrationDate == null) {
            registrationDate = LocalDate.now();
        }
        if (category == null) {
            category = CustomerCategory.NUEVO;
        }
    }

    @PreUpdate
    private void preUpdate() {
        if (phone != null) {
            phone = phone.replaceAll("[\\s-]", "");
        }
        // Update category based on seniority
        updateCategory();
    }
}
