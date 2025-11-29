package com.cyrcetech.model;

import java.util.Objects;

/**
 * Represents a customer in the system.
 * Immutable record with validation.
 */
public record Customer(
    String id,
    String name,
    String taxId,
    String address,
    String phone
) {
    /**
     * Compact constructor with validation
     */
    public Customer {
        Objects.requireNonNull(name, "Customer name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }
        
        // Normalize phone (remove spaces and dashes for consistency)
        if (phone != null) {
            phone = phone.replaceAll("[\\s-]", "");
        }
    }
    
    /**
     * Creates an empty customer (for form initialization)
     */
    public static Customer empty() {
        return new Customer("", "", "", "", "");
    }
    
    /**
     * Returns formatted phone number for display
     */
    public String getFormattedPhone() {
        if (phone == null || phone.isEmpty()) {
            return "";
        }
        // Simple formatting: (XXX) XXX-XXXX for 10 digits
        if (phone.length() == 10) {
            return String.format("(%s) %s-%s", 
                phone.substring(0, 3),
                phone.substring(3, 6),
                phone.substring(6));
        }
        return phone;
    }
}
