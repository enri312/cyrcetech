package com.cyrcetech.backend.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO for updating spare part
 */
public class UpdateSparePartRequest {

    private String name;

    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;

    @PositiveOrZero(message = "Stock must be zero or positive")
    private Integer stock;

    private String provider;

    // Constructors
    public UpdateSparePartRequest() {
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
