package com.cyrcetech.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a service ticket for a customer's device.
 * Mutable class to allow status updates throughout the service lifecycle.
 */
public class Ticket {
    private String id;
    private Customer customer;
    private DeviceType deviceType;
    private String brand;
    private String model;
    private String serialNumber;
    private String physicalCondition;
    private String problemDescription;
    private String observations;
    private TicketStatus status;
    private double amountPaid;
    private double estimatedCost;
    private LocalDate dateCreated;
    private String aiDiagnosis;

    public Ticket() {
        this.dateCreated = LocalDate.now();
        this.status = TicketStatus.PENDING;
    }

    // Getters and Setters with validation
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        Objects.requireNonNull(customer, "Customer cannot be null");
        this.customer = customer;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPhysicalCondition() {
        return physicalCondition;
    }

    public void setPhysicalCondition(String physicalCondition) {
        this.physicalCondition = physicalCondition;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        Objects.requireNonNull(status, "Status cannot be null");
        this.status = status;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        if (amountPaid < 0) {
            throw new IllegalArgumentException("Amount paid cannot be negative");
        }
        this.amountPaid = amountPaid;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost) {
        if (estimatedCost < 0) {
            throw new IllegalArgumentException("Estimated cost cannot be negative");
        }
        this.estimatedCost = estimatedCost;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAiDiagnosis() {
        return aiDiagnosis;
    }

    public void setAiDiagnosis(String aiDiagnosis) {
        this.aiDiagnosis = aiDiagnosis;
    }

    // Utility methods

    /**
     * Returns the remaining balance to be paid
     */
    public double getRemainingBalance() {
        return Math.max(0, estimatedCost - amountPaid);
    }

    /**
     * Checks if the ticket is fully paid
     */
    public boolean isFullyPaid() {
        return amountPaid >= estimatedCost;
    }

    /**
     * Returns formatted date
     */
    public String getFormattedDate() {
        if (dateCreated == null) {
            return "";
        }
        return dateCreated.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Returns a summary of the device
     */
    public String getDeviceSummary() {
        StringBuilder sb = new StringBuilder();
        if (deviceType != null) {
            sb.append(deviceType.getDisplayName());
        }
        if (brand != null && !brand.isBlank()) {
            sb.append(" ").append(brand);
        }
        if (model != null && !model.isBlank()) {
            sb.append(" ").append(model);
        }
        return sb.toString();
    }
}
