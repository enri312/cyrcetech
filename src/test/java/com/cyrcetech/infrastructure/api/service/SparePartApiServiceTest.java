package com.cyrcetech.infrastructure.api.service;

import com.cyrcetech.entity.SparePart;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for SparePartApiService.
 * Requires backend running at http://localhost:8080
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SparePartApiServiceTest {

    private static SparePartApiService sparePartApiService;
    private static String createdSparePartId;

    @BeforeAll
    static void setUp() {
        sparePartApiService = new SparePartApiService();
    }

    @Test
    @Order(1)
    @DisplayName("Should get all spare parts")
    void testGetAllSpareParts() {
        try {
            List<SparePart> spareParts = sparePartApiService.getAllSpareParts();
            assertNotNull(spareParts);
            System.out.println("✓ Found " + spareParts.size() + " spare parts");
        } catch (Exception e) {
            fail("Failed to get spare parts: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Should create a new spare part")
    void testCreateSparePart() {
        try {
            SparePart newPart = new SparePart(
                    null,
                    "Test Part " + System.currentTimeMillis(),
                    150000.0, // price in Guaraníes
                    25, // stock
                    5, // min stock
                    "Test Provider",
                    "Compatible with test devices");

            SparePart created = sparePartApiService.createSparePart(newPart);
            assertNotNull(created);
            assertNotNull(created.id());
            createdSparePartId = created.id();
            System.out.println("✓ Created spare part with ID: " + createdSparePartId);
        } catch (Exception e) {
            fail("Failed to create spare part: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Should get spare part by ID")
    void testGetSparePartById() {
        Assumptions.assumeTrue(createdSparePartId != null, "Requires created spare part");

        try {
            SparePart part = sparePartApiService.getSparePartById(createdSparePartId);
            assertNotNull(part);
            assertEquals(createdSparePartId, part.id());
            System.out.println("✓ Retrieved spare part: " + part.name());
        } catch (Exception e) {
            fail("Failed to get spare part by ID: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Should update spare part")
    void testUpdateSparePart() {
        Assumptions.assumeTrue(createdSparePartId != null, "Requires created spare part");

        try {
            SparePart updated = new SparePart(
                    createdSparePartId,
                    "Updated Test Part",
                    200000.0,
                    30,
                    10,
                    "Updated Provider",
                    "Updated compatibility");

            SparePart result = sparePartApiService.updateSparePart(createdSparePartId, updated);
            assertNotNull(result);
            assertEquals("Updated Test Part", result.name());
            System.out.println("✓ Updated spare part: " + result.name());
        } catch (Exception e) {
            fail("Failed to update spare part: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Should delete spare part")
    void testDeleteSparePart() {
        Assumptions.assumeTrue(createdSparePartId != null, "Requires created spare part");

        try {
            sparePartApiService.deleteSparePart(createdSparePartId);
            System.out.println("✓ Deleted spare part with ID: " + createdSparePartId);
        } catch (Exception e) {
            fail("Failed to delete spare part: " + e.getMessage());
        }
    }
}
