package com.cyrcetech.entity;

import java.util.Objects;

/**
 * Represents a customer's equipment/device.
 * Immutable record with validation.
 */
public record Equipment(
        String id,
        String brand,
        String model,
        DeviceType deviceType,
        String serialNumber,
        String physicalCondition,
        String customerId) {

    public Equipment {
        Objects.requireNonNull(brand, "Brand cannot be null");
        Objects.requireNonNull(model, "Model cannot be null");
        Objects.requireNonNull(deviceType, "Device type cannot be null");
        Objects.requireNonNull(customerId, "Customer ID cannot be null");

        if (brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be blank");
        }
        if (model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be blank");
        }
    }

    /**
     * Creates an empty equipment (for form initialization)
     */
    public static Equipment empty() {
        return new Equipment("", "", "", DeviceType.OTHER, "", "", "");
    }

    /**
     * Returns a summary of the device
     */
    public String getSummary() {
        return String.format("%s %s %s", deviceType.getDisplayName(), brand, model);
    }
}
