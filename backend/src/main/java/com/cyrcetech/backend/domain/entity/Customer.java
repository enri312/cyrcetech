package com.cyrcetech.backend.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Customer entity representing a client in the system.
 * Migrated from Java record to JPA entity for database persistence.
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "tax_id")
    private String taxId;

    private String address;

    private String phone;

    public Customer() {
    }

    public Customer(String id, String name, String taxId, String address, String phone) {
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
     * Normalizes phone number before persisting
     */
    @PrePersist
    @PreUpdate
    private void normalizePhone() {
        if (phone != null) {
            phone = phone.replaceAll("[\\s-]", "");
        }
    }
}
