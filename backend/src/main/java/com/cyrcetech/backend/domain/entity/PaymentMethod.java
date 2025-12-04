package com.cyrcetech.backend.domain.entity;

/**
 * Payment method enum
 */
public enum PaymentMethod {
    CASH("Efectivo"),
    CARD("Tarjeta"),
    TRANSFER("Transferencia"),
    OTHER("Otro");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
