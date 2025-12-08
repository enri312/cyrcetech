package com.cyrcetech.backend.dto.response;

import com.cyrcetech.backend.domain.entity.AuditAction;
import com.cyrcetech.backend.domain.entity.Role;

/**
 * DTO for audit log responses
 */
public class AuditLogResponse {

    private String id;
    private String userId;
    private String username;
    private Role userRole;
    private String userRoleDisplayName;
    private AuditAction action;
    private String actionDisplayName;
    private String entityType;
    private String entityId;
    private String timestamp;
    private String details;
    private String ipAddress;

    public AuditLogResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public String getUserRoleDisplayName() {
        return userRoleDisplayName;
    }

    public void setUserRoleDisplayName(String userRoleDisplayName) {
        this.userRoleDisplayName = userRoleDisplayName;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getActionDisplayName() {
        return actionDisplayName;
    }

    public void setActionDisplayName(String actionDisplayName) {
        this.actionDisplayName = actionDisplayName;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
