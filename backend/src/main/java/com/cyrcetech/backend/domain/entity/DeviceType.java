package com.cyrcetech.backend.domain.entity;

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

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getFormattedName() {
        return icon + " " + displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
