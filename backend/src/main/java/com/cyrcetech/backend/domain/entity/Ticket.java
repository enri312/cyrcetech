package com.cyrcetech.backend.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Ticket/Service Order entity
 */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @Column(name = "problem_description", columnDefinition = "TEXT")
    private String problemDescription;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    @Column(name = "amount_paid")
    private double amountPaid;

    @Column(name = "down_payment")
    private double downPayment;

    @Column(name = "estimated_cost")
    private double estimatedCost;

    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "ai_diagnosis", columnDefinition = "TEXT")
    private String aiDiagnosis;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Ticket() {
        this.dateCreated = LocalDate.now();
        this.status = TicketStatus.PENDING;
        this.amountPaid = 0.0;
        this.downPayment = 0.0;
        this.estimatedCost = 0.0;
    }

    public Ticket(Customer customer, Equipment equipment, String problemDescription) {
        this();
        this.customer = customer;
        this.equipment = equipment;
        this.problemDescription = problemDescription;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (dateCreated == null) {
            dateCreated = LocalDate.now();
        }
        if (status == null) {
            status = TicketStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
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
        this.customer = customer;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Business methods
    public double getRemainingBalance() {
        return Math.max(0, estimatedCost - amountPaid);
    }

    public boolean isFullyPaid() {
        return amountPaid >= estimatedCost;
    }

    public String getFormattedDate() {
        if (dateCreated == null) {
            return "";
        }
        return dateCreated.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getDeviceSummary() {
        if (equipment != null) {
            return equipment.getSummary();
        }
        return "No Device";
    }
}
