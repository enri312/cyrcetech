package com.cyrcetech.infrastructure.api.dto;

/**
 * DTO for creating/updating SparePart via API
 */
public class SparePartRequestDTO {
    private String name;
    private Double price;
    private Integer stock;
    private String provider;

    public SparePartRequestDTO() {
    }

    public SparePartRequestDTO(String name, Double price, Integer stock, String provider) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.provider = provider;
    }

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
