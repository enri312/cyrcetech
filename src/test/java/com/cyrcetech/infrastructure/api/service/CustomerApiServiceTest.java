package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.entity.Customer;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for CustomerApiService.
 * Requires backend running at http://localhost:8080
 */
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerApiServiceTest {

    private static CustomerApiService customerApiService;
    private static String createdCustomerId;

    @BeforeAll
    static void setUp() {
        customerApiService = new CustomerApiService();
    }

    @Test
    @Order(1)
    @DisplayName("Should get all customers")
    void testGetAllCustomers() {
        try {
            List<Customer> customers = customerApiService.getAllCustomers();
            assertNotNull(customers);
            System.out.println("✓ Found " + customers.size() + " customers");
        } catch (Exception e) {
            fail("Failed to get customers: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Should create a new customer")
    void testCreateCustomer() {
        try {
            Customer newCustomer = new Customer(
                    null,
                    "Test Customer " + System.currentTimeMillis(),
                    "0981-TEST-123",
                    "Test Address 123",
                    "12345678-9");

            Customer created = customerApiService.createCustomer(newCustomer);
            assertNotNull(created);
            assertNotNull(created.id());
            createdCustomerId = created.id();
            System.out.println("✓ Created customer with ID: " + createdCustomerId);
        } catch (Exception e) {
            fail("Failed to create customer: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Should get customer by ID")
    void testGetCustomerById() {
        Assumptions.assumeTrue(createdCustomerId != null, "Requires created customer");

        try {
            Customer customer = customerApiService.getCustomerById(createdCustomerId);
            assertNotNull(customer);
            assertEquals(createdCustomerId, customer.id());
            System.out.println("✓ Retrieved customer: " + customer.name());
        } catch (Exception e) {
            fail("Failed to get customer by ID: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Should update customer")
    void testUpdateCustomer() {
        Assumptions.assumeTrue(createdCustomerId != null, "Requires created customer");

        try {
            Customer updated = new Customer(
                    createdCustomerId,
                    "Updated Test Customer",
                    "0981-UPDATED",
                    "Updated Address 456",
                    "87654321-0");

            Customer result = customerApiService.updateCustomer(createdCustomerId, updated);
            assertNotNull(result);
            assertEquals("Updated Test Customer", result.name());
            System.out.println("✓ Updated customer: " + result.name());
        } catch (Exception e) {
            fail("Failed to update customer: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Should delete customer")
    void testDeleteCustomer() {
        Assumptions.assumeTrue(createdCustomerId != null, "Requires created customer");

        try {
            customerApiService.deleteCustomer(createdCustomerId);
            System.out.println("✓ Deleted customer with ID: " + createdCustomerId);
        } catch (Exception e) {
            fail("Failed to delete customer: " + e.getMessage());
        }
    }
}
