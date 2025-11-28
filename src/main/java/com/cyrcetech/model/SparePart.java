package com.cyrcetech.model;

public class SparePart {
    private String id;
    private String name;
    private double price;
    private int stock;
    private String provider;

    public SparePart(String id, String name, double price, int stock, String provider) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.provider = provider;
    }

    public SparePart() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
