package com.cyrcetech.infrastructure.api.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DTO for Audit Log entries from backend API
 */
public class AuditLogDTO {
    private String id;
    private String username;
    private String action; // Backend sends enum name like "LOGIN", "LIST"
    private String entityType;
    private String entityId;
    private String details;
    private String timestamp; // Backend sends as String ISO format

    // Transient for parsed timestamp
    private transient LocalDateTime parsedTimestamp;

    public AuditLogDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTimestampRaw() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        this.parsedTimestamp = null; // Reset parsed
    }

    /**
     * Get timestamp as LocalDateTime, parsing from String if needed
     */
    public LocalDateTime getTimestamp() {
        if (parsedTimestamp == null && timestamp != null && !timestamp.isEmpty()) {
            try {
                parsedTimestamp = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception e) {
                // Try alternative format
                try {
                    parsedTimestamp = LocalDateTime.parse(timestamp.replace(" ", "T"));
                } catch (Exception e2) {
                    System.err.println("Could not parse timestamp: " + timestamp);
                }
            }
        }
        return parsedTimestamp;
    }
}
