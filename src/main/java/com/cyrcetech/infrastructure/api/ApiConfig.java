package com.cyrcetech.infrastructure.api;

/**
 * Configuration for REST API client
 */
public class ApiConfig {

    private static final String BASE_URL = "http://localhost:8080/api";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getCustomersUrl() {
        return BASE_URL + "/customers";
    }

    public static String getEquipmentUrl() {
        return BASE_URL + "/equipment";
    }

    public static String getTicketsUrl() {
        return BASE_URL + "/tickets";
    }

    public static String getSparePartsUrl() {
        return BASE_URL + "/spare-parts";
    }

    public static String getInvoicesUrl() {
        return BASE_URL + "/invoices";
    }

    public static String getAuthUrl() {
        return BASE_URL + "/auth";
    }
}
