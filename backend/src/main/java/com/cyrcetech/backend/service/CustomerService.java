package com.cyrcetech.backend.service;

import com.cyrcetech.backend.domain.entity.Customer;
import com.cyrcetech.backend.dto.request.CreateCustomerRequest;
import com.cyrcetech.backend.dto.request.UpdateCustomerRequest;
import com.cyrcetech.backend.dto.response.CustomerResponse;
import com.cyrcetech.backend.exception.ResourceNotFoundException;
import com.cyrcetech.backend.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Customer business logic
 */
@Service
@Transactional
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final CustomerPdfExportService pdfExportService;

    public CustomerService(CustomerRepository customerRepository, CustomerPdfExportService pdfExportService) {
        this.customerRepository = customerRepository;
        this.pdfExportService = pdfExportService;
    }

    /**
     * Get all customers
     */
    public List<CustomerResponse> getAllCustomers() {
        log.debug("Fetching all customers");
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get customer by ID
     */
    public CustomerResponse getCustomerById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        log.debug("Fetching customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return toResponse(customer);
    }

    /**
     * Create new customer
     */
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.debug("Creating new customer: {}", request.getName());

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setTaxId(request.getTaxId());
        customer.setAddress(request.getAddress());
        customer.setPhone(request.getPhone());

        Customer saved = customerRepository.save(customer);
        log.info("Customer created with id: {}", saved.getId());

        return toResponse(saved);
    }

    /**
     * Update existing customer
     */
    public CustomerResponse updateCustomer(String id, UpdateCustomerRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        log.debug("Updating customer with id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (request.getName() != null) {
            customer.setName(request.getName());
        }
        if (request.getTaxId() != null) {
            customer.setTaxId(request.getTaxId());
        }
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            customer.setPhone(request.getPhone());
        }

        Customer updated = customerRepository.save(customer);
        log.info("Customer updated: {}", id);

        return toResponse(updated);
    }

    /**
     * Delete customer
     */
    public void deleteCustomer(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        log.debug("Deleting customer with id: {}", id);

        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }

        customerRepository.deleteById(id);
        log.info("Customer deleted: {}", id);
    }

    /**
     * Search customers
     */
    public List<CustomerResponse> searchCustomers(String searchTerm) {
        if (searchTerm == null) {
            searchTerm = "";
        }
        log.debug("Searching customers with term: {}", searchTerm);
        return customerRepository.searchCustomers(searchTerm).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert Customer entity to CustomerResponse DTO
     */
    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setTaxId(customer.getTaxId());
        response.setAddress(customer.getAddress());
        response.setPhone(customer.getPhone());
        response.setFormattedPhone(customer.getFormattedPhone());
        response.setRegistrationDate(customer.getRegistrationDate() != null
                ? customer.getRegistrationDate().toString()
                : null);
        response.setCategory(customer.getCategory());
        response.setCategoryDisplayName(customer.getCategory() != null
                ? customer.getCategory().getDisplayName()
                : null);
        response.setSeniorityDays(customer.getSeniorityDays());
        response.setFormattedSeniority(customer.getFormattedSeniority());
        return response;
    }

    /**
     * Export all customers to PDF format.
     *
     * @return byte array containing the PDF file
     * @throws IOException if export fails
     */
    @Transactional(readOnly = true)
    public byte[] exportCustomersToPdf() throws IOException {
        log.info("Exporting all customers to PDF");
        List<Customer> customers = customerRepository.findAll();
        return pdfExportService.exportCustomersToPdf(customers);
    }
}
