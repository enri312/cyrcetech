package com.cyrcetech.backend.domain.entity;

/**
 * Enum representing the types of audit actions that can be logged.
 */
public enum AuditAction {
    // Read operations
    LIST("Listar", "Lista de registros"),
    VIEW("Ver", "Ver detalle"),
    SEARCH("Buscar", "Búsqueda"),
    EXPORT_PDF("Exportar PDF", "Exportación a PDF"),
    EXPORT_EXCEL("Exportar Excel", "Exportación a Excel"),

    // Write operations
    CREATE("Crear", "Creación de registro"),
    UPDATE("Actualizar", "Actualización de registro"),
    DELETE("Eliminar", "Eliminación de registro"),

    // Auth operations
    LOGIN("Iniciar sesión", "Inicio de sesión"),
    LOGOUT("Cerrar sesión", "Cierre de sesión"),
    REGISTER("Registrar", "Registro de usuario");

    private final String displayName;
    private final String description;

    AuditAction(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
