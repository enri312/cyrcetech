package com.cyrcetech.entity;

public enum PaymentStatus {
    PENDING("Pendiente"),
    PARTIAL("Parcial"),
    PAID("Pagado");

    private final String displayName;

    PaymentStatus(String displayName) {
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
