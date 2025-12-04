package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.entity.Customer;
import com.cyrcetech.infrastructure.api.ApiClient;
import com.cyrcetech.infrastructure.api.ApiConfig;
import com.cyrcetech.infrastructure.api.dto.CustomerRequestDTO;
import com.cyrcetech.infrastructure.api.dto.CustomerResponseDTO;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to interact with Customer REST API
 */
public class CustomerApiService extends ApiClient {

    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() throws Exception {
        Type listType = new TypeToken<List<CustomerResponseDTO>>() {
        }.getType();
        String json = get(ApiConfig.getCustomersUrl(), String.class);
        List<CustomerResponseDTO> dtos = getGson().fromJson(json, listType);

        List<Customer> customers = new ArrayList<>();
        for (CustomerResponseDTO dto : dtos) {
            customers.add(toEntity(dto));
        }
        return customers;
    }

    /**
     * Get customer by ID
     */
    public Customer getCustomerById(String id) throws Exception {
        String url = ApiConfig.getCustomersUrl() + "/" + id;
        CustomerResponseDTO dto = get(url, CustomerResponseDTO.class);
        return toEntity(dto);
    }

    /**
     * Create new customer
     */
    public Customer createCustomer(Customer customer) throws Exception {
        CustomerRequestDTO request = toRequestDTO(customer);
        CustomerResponseDTO response = post(ApiConfig.getCustomersUrl(), request, CustomerResponseDTO.class);
        return toEntity(response);
    }

    /**
     * Update existing customer
     */
    public Customer updateCustomer(String id, Customer customer) throws Exception {
        String url = ApiConfig.getCustomersUrl() + "/" + id;
        CustomerRequestDTO request = toRequestDTO(customer);
        CustomerResponseDTO response = put(url, request, CustomerResponseDTO.class);
        return toEntity(response);
    }

    /**
     * Delete customer
     */
    public void deleteCustomer(String id) throws Exception {
        String url = ApiConfig.getCustomersUrl() + "/" + id;
        delete(url);
    }

    /**
     * Search customers
     */
    public List<Customer> searchCustomers(String searchTerm) throws Exception {
        String url = ApiConfig.getCustomersUrl() + "/search?q=" + searchTerm;
        Type listType = new TypeToken<List<CustomerResponseDTO>>() {
        }.getType();
        String json = get(url, String.class);
        List<CustomerResponseDTO> dtos = getGson().fromJson(json, listType);

        List<Customer> customers = new ArrayList<>();
        for (CustomerResponseDTO dto : dtos) {
            customers.add(toEntity(dto));
        }
        return customers;
    }

    /**
     * Convert DTO to Entity
     */
    private Customer toEntity(CustomerResponseDTO dto) {
        return new Customer(
                dto.getId(),
                dto.getName(),
                dto.getTaxId(),
                dto.getAddress(),
                dto.getPhone());
    }

    /**
     * Convert Entity to Request DTO
     */
    private CustomerRequestDTO toRequestDTO(Customer customer) {
        return new CustomerRequestDTO(
                customer.name(),
                customer.taxId(),
                customer.address(),
                customer.phone());
    }
}
