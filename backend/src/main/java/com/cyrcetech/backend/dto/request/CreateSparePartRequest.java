package com.cyrcetech.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO for creating new spare part
 */
public class CreateSparePartRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;

    @PositiveOrZero(message = "Stock must be zero or positive")
    private Integer stock;

    private String provider;

    // Constructors
    public CreateSparePartRequest() {
    }

    public CreateSparePartRequest(String name, double price, int stock, String provider) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.provider = provider;
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
