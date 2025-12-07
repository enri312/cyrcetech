package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.dto.request.CreateCustomerRequest;
import com.cyrcetech.backend.dto.response.CustomerResponse;
import com.cyrcetech.backend.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerUnitTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void getAllCustomers_ShouldReturnList() {
        CustomerResponse response = new CustomerResponse();
        response.setId("1");
        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(response));

        ResponseEntity<List<CustomerResponse>> result = customerController.getAllCustomers();

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        verify(customerService).getAllCustomers();
    }

    @Test
    void createCustomer_ShouldReturnCreated() {
        CreateCustomerRequest request = new CreateCustomerRequest("John Doe", "123", "Address", "555-0100");
        CustomerResponse response = new CustomerResponse();
        response.setId("new");
        when(customerService.createCustomer(request)).thenReturn(response);

        ResponseEntity<CustomerResponse> result = customerController.createCustomer(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("new", result.getBody().getId());
        verify(customerService).createCustomer(request);
    }
}
