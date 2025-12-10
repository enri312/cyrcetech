package com.cyrcetech.backend.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the type of device being serviced
 */
public enum DeviceType {
    NOTEBOOK("Notebook", "💻"),
    SMARTPHONE("Smartphone", "📱"),
    MONITOR("Monitor", "🖥️"),
    TABLET("Tablet", "📲"),
    CONSOLE("Consola", "🎮"),
    PRINTER("Impresora", "🖨️"),
    OTHER("Otro", "🔧");

    private final String displayName;
    private final String icon;

    DeviceType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getFormattedName() {
        return icon + " " + displayName;
    }

    @JsonCreator
    public static DeviceType fromDisplayName(String value) {
        if (value == null) {
            return null;
        }
        for (DeviceType type : DeviceType.values()) {
            if (type.displayName.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown DeviceType: " + value);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
