package com.cyrcetech.entity;

import java.util.Objects;

/**
 * Represents a spare part in the inventory.
 * Immutable record with validation.
 */
public record SparePart(
        String id,
        String name,
        double price,
        int stock,
        String provider) {
    /**
     * Compact constructor with validation
     */
    public SparePart {
        Objects.requireNonNull(name, "Spare part name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Spare part name cannot be blank");
        }

        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }

    /**
     * Creates an empty spare part (for form initialization)
     */
    public static SparePart empty() {
        return new SparePart("", "", 0.0, 0, "");
    }

    /**
     * Checks if the spare part is in stock
     */
    public boolean isInStock() {
        return stock > 0;
    }

    /**
     * Checks if stock is low (less than 5 units)
     */
    public boolean isLowStock() {
        return stock > 0 && stock < 5;
    }

    /**
     * Returns formatted price with currency
     */
    public String getFormattedPrice() {
        return String.format("₲%.2f", price);
    }
}
