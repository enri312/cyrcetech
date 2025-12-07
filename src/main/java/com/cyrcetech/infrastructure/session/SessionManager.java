package com.cyrcetech.infrastructure.session;

/**
 * Singleton to manage user session and JWT token
 */
public class SessionManager {

    private static SessionManager instance;

    private String token;
    private String userId;
    private String email;
    private String role;
    private String fullName;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Set session data after successful login
     */
    public void setSession(String token, String userId, String email, String role, String fullName) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.fullName = fullName;
    }

    /**
     * Clear session data on logout
     */
    public void logout() {
        this.token = null;
        this.userId = null;
        this.email = null;
        this.role = null;
        this.fullName = null;
    }

    public boolean isAuthenticated() {
        return token != null && !token.isEmpty();
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    /**
     * Check if user has ADMIN role
     */
    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(role);
    }

    /**
     * Check if user has TECHNICIAN role
     */
    public boolean isTechnician() {
        return "ROLE_TECHNICIAN".equals(role);
    }

    /**
     * Check if user has USER role
     */
    public boolean isUser() {
        return "ROLE_USER".equals(role);
    }

    /**
     * Check if user can manage tickets (ADMIN or TECHNICIAN)
     */
    public boolean canManageTickets() {
        return isAdmin() || isTechnician();
    }

    /**
     * Check if user can manage users (ADMIN only)
     */
    public boolean canManageUsers() {
        return isAdmin();
    }
}
