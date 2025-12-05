package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.entity.Equipment;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for EquipmentApiService.
 * Requires backend running at http://localhost:8080
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EquipmentApiServiceTest {

    private static EquipmentApiService equipmentApiService;
    private static String createdEquipmentId;
    private static String testCustomerId;

    @BeforeAll
    static void setUp() {
        equipmentApiService = new EquipmentApiService();

        // Create a test customer first (equipment requires a customer)
        try {
            CustomerApiService customerApiService = new CustomerApiService();
            var customer = new com.cyrcetech.entity.Customer(
                    null, "Equipment Test Customer", "equip@test.com",
                    "0981-EQUIP", "Test Address", "11111111-1");
            var created = customerApiService.createCustomer(customer);
            testCustomerId = created.id();
            System.out.println("Created test customer: " + testCustomerId);
        } catch (Exception e) {
            System.err.println("Warning: Could not create test customer: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDown() {
        // Clean up test customer
        if (testCustomerId != null) {
            try {
                new CustomerApiService().deleteCustomer(testCustomerId);
                System.out.println("Cleaned up test customer");
            } catch (Exception e) {
                System.err.println("Warning: Could not delete test customer: " + e.getMessage());
            }
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should get all equipment")
    void testGetAllEquipment() {
        try {
            List<Equipment> equipment = equipmentApiService.getAllEquipment();
            assertNotNull(equipment);
            System.out.println("✓ Found " + equipment.size() + " equipment items");
        } catch (Exception e) {
            fail("Failed to get equipment: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Should create new equipment")
    void testCreateEquipment() {
        Assumptions.assumeTrue(testCustomerId != null, "Requires test customer");

        try {
            Equipment newEquipment = new Equipment(
                    null,
                    testCustomerId,
                    "LAPTOP",
                    "Dell",
                    "XPS 15",
                    "SN-TEST-" + System.currentTimeMillis(),
                    "Test accessories",
                    "Good condition");

            Equipment created = equipmentApiService.createEquipment(newEquipment);
            assertNotNull(created);
            assertNotNull(created.id());
            createdEquipmentId = created.id();
            System.out.println("✓ Created equipment with ID: " + createdEquipmentId);
        } catch (Exception e) {
            fail("Failed to create equipment: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Should get equipment by ID")
    void testGetEquipmentById() {
        Assumptions.assumeTrue(createdEquipmentId != null, "Requires created equipment");

        try {
            Equipment equipment = equipmentApiService.getEquipmentById(createdEquipmentId);
            assertNotNull(equipment);
            assertEquals(createdEquipmentId, equipment.id());
            System.out.println("✓ Retrieved equipment: " + equipment.brand() + " " + equipment.model());
        } catch (Exception e) {
            fail("Failed to get equipment by ID: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Should delete equipment")
    void testDeleteEquipment() {
        Assumptions.assumeTrue(createdEquipmentId != null, "Requires created equipment");

        try {
            equipmentApiService.deleteEquipment(createdEquipmentId);
            System.out.println("✓ Deleted equipment with ID: " + createdEquipmentId);
        } catch (Exception e) {
            fail("Failed to delete equipment: " + e.getMessage());
        }
    }
}
