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
    private Equipment equipment; // Replaces individual device fields
    private String problemDescription;
    private String observations;
    private TicketStatus status;
    private double amountPaid;
    private double downPayment;
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

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        Objects.requireNonNull(equipment, "Equipment cannot be null");
        this.equipment = equipment;
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
        this.amountPaid = amountPaid;
    }

    public double getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(double downPayment) {
        if (downPayment < 0) {
            throw new IllegalArgumentException("Down payment cannot be negative");
        }
        this.downPayment = downPayment;
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
        if (equipment != null) {
            return equipment.getSummary();
        }
        return "No Device";
    }
}
