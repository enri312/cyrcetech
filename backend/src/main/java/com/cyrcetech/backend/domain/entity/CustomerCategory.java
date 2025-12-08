package com.cyrcetech.backend.domain.entity;

/**
 * Customer category based on seniority/registration time.
 * Categories are assigned based on how long the customer has been registered.
 */
public enum CustomerCategory {
    NUEVO("Nuevo", "Cliente nuevo", 0, 30),
    REGULAR("Regular", "Cliente regular", 31, 180),
    VIP("VIP", "Cliente VIP", 181, 365),
    ESPECIAL("Especial", "Cliente especial", 366, Integer.MAX_VALUE);

    private final String displayName;
    private final String description;
    private final int minDays;
    private final int maxDays;

    CustomerCategory(String displayName, String description, int minDays, int maxDays) {
        this.displayName = displayName;
        this.description = description;
        this.minDays = minDays;
        this.maxDays = maxDays;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getMinDays() {
        return minDays;
    }

    public int getMaxDays() {
        return maxDays;
    }

    /**
     * Determines the appropriate category based on the number of days since registration.
     *
     * @param daysSinceRegistration Number of days since the customer registered
     * @return The appropriate CustomerCategory
     */
    public static CustomerCategory fromDays(long daysSinceRegistration) {
        for (CustomerCategory category : values()) {
            if (daysSinceRegistration >= category.minDays && daysSinceRegistration <= category.maxDays) {
                return category;
            }
        }
        return NUEVO; // Default
    }
}
