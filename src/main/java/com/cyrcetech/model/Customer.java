package com.cyrcetech.model;

public class Customer {
    private String id;
    private String name;
    private String taxId;
    private String address;
    private String phone;

    public Customer(String id, String name, String taxId, String address, String phone) {
        this.id = id;
        this.name = name;
        this.taxId = taxId;
        this.address = address;
        this.phone = phone;
    }

    public Customer() {
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

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
