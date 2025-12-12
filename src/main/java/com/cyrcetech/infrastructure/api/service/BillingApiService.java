package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.entity.Invoice;
import com.cyrcetech.infrastructure.api.ApiClient;
import com.cyrcetech.infrastructure.api.ApiConfig;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

/**
 * Service to interact with Billing REST API
 */
public class BillingApiService extends ApiClient {

    private final String BASE_URL = ApiConfig.getBaseUrl() + "/billing";

    /**
     * Get daily billing report
     */
    public List<Invoice> getDailyBilling(LocalDate date) throws Exception {
        String url = BASE_URL + "/daily/" + date.toString();
        Type listType = new TypeToken<List<Invoice>>() {
        }.getType();
        String json = getString(url);
        return getGson().fromJson(json, listType);
    }

    /**
     * Get monthly billing report
     */
    public List<Invoice> getMonthlyBilling(int year, int month) throws Exception {
        String url = BASE_URL + "/monthly/" + year + "/" + month;
        Type listType = new TypeToken<List<Invoice>>() {
        }.getType();
        String json = getString(url);
        return getGson().fromJson(json, listType);
    }

    /**
     * Get yearly billing report
     */
    public List<Invoice> getYearlyBilling(int year) throws Exception {
        String url = BASE_URL + "/yearly/" + year;
        Type listType = new TypeToken<List<Invoice>>() {
        }.getType();
        String json = getString(url);
        return getGson().fromJson(json, listType);
    }
}
