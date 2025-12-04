package com.cyrcetech.backend.dto.request;

import com.cyrcetech.backend.domain.entity.TicketStatus;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO for updating ticket
 */
public class UpdateTicketRequest {

    private String problemDescription;
    private String observations;
    private TicketStatus status;

    @PositiveOrZero(message = "Amount paid must be zero or positive")
    private Double amountPaid;

    @PositiveOrZero(message = "Estimated cost must be zero or positive")
    private Double estimatedCost;

    private String aiDiagnosis;

    // Constructors
    public UpdateTicketRequest() {
    }

    // Getters and Setters
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

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
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
}
