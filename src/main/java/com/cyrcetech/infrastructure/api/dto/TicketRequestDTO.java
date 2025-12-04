package com.cyrcetech.infrastructure.api.dto;

import com.cyrcetech.entity.TicketStatus;

/**
 * DTO for creating/updating Ticket via API
 */
public class TicketRequestDTO {
    private String customerId;
    private String equipmentId;
    private String problemDescription;
    private String observations;
    private Double estimatedCost;
    private String aiDiagnosis;
    private TicketStatus status;
    private Double amountPaid;

    public TicketRequestDTO() {
    }

    public TicketRequestDTO(String customerId, String equipmentId, String problemDescription,
            String observations, Double estimatedCost, String aiDiagnosis,
            TicketStatus status, Double amountPaid) {
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.problemDescription = problemDescription;
        this.observations = observations;
        this.estimatedCost = estimatedCost;
        this.aiDiagnosis = aiDiagnosis;
        this.status = status;
        this.amountPaid = amountPaid;
    }

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

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }
}
