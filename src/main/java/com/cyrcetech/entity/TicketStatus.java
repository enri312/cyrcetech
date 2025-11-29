package com.cyrcetech.entity;

/**
 * Represents the status of a service ticket.
 */
public enum TicketStatus {
    PENDING("Pendiente", "#FFC107"),
    DIAGNOSING("Diagnosticando", "#2196F3"),
    IN_PROGRESS("En Progreso", "#FF9800"),
    WAITING_PARTS("Esperando Repuestos", "#9C27B0"),
    READY("Listo", "#4CAF50"),
    DELIVERED("Entregado", "#00BCD4"),
    CANCELLED("Cancelado", "#F44336");

    private final String displayName;
    private final String colorCode;

    TicketStatus(String displayName, String colorCode) {
        this.displayName = displayName;
        this.colorCode = colorCode;
    }

    /**
     * Returns the display name in Spanish
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the color code for UI display
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * Checks if this status represents an active ticket
     */
    public boolean isActive() {
        return this != DELIVERED && this != CANCELLED;
    }

    /**
     * Checks if this status represents a completed ticket
     */
    public boolean isCompleted() {
        return this == DELIVERED || this == CANCELLED;
    }

    /**
     * Checks if work can be done on this ticket
     */
    public boolean canWork() {
        return this == DIAGNOSING || this == IN_PROGRESS;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
