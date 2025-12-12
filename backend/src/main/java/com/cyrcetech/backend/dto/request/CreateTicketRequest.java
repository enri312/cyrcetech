package com.cyrcetech.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO for creating new ticket
 */
public class CreateTicketRequest {

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Equipment ID is required")
    private String equipmentId;

    @NotBlank(message = "Problem description is required")
    private String problemDescription;

    private String observations;

    @PositiveOrZero(message = "Estimated cost must be zero or positive")
    private Double estimatedCost;

    private String aiDiagnosis;

    // Constructors
    public CreateTicketRequest() {
    }

    public CreateTicketRequest(String customerId, String equipmentId, String problemDescription) {
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.problemDescription = problemDescription;
        this.estimatedCost = 0.0;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
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

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getAiDiagnosis() {
        return aiDiagnosis;
    }

    public void setAiDiagnosis(String aiDiagnosis) {
        this.aiDiagnosis = aiDiagnosis;
    }

    @PositiveOrZero(message = "Amount paid must be zero or positive")
    private Double amountPaid;

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }
}
