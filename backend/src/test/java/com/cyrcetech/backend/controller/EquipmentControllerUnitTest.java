package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.domain.entity.DeviceType;
import com.cyrcetech.backend.dto.request.CreateEquipmentRequest;
import com.cyrcetech.backend.dto.response.EquipmentResponse;
import com.cyrcetech.backend.service.EquipmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentControllerUnitTest {

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentController equipmentController;

    @Test
    void getAllEquipment_ShouldReturnList() {
        EquipmentResponse response = new EquipmentResponse();
        response.setId("1");
        when(equipmentService.getAllEquipment()).thenReturn(Arrays.asList(response));

        ResponseEntity<List<EquipmentResponse>> result = equipmentController.getAllEquipment();

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        verify(equipmentService).getAllEquipment();
    }

    @Test
    void createEquipment_ShouldReturnCreated() {
        CreateEquipmentRequest request = new CreateEquipmentRequest("Brand", "Model", DeviceType.NOTEBOOK, "SN", "Good",
                "cust-1");
        EquipmentResponse response = new EquipmentResponse();
        response.setId("new");
        when(equipmentService.createEquipment(request)).thenReturn(response);

        ResponseEntity<EquipmentResponse> result = equipmentController.createEquipment(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("new", result.getBody().getId());
        verify(equipmentService).createEquipment(request);
    }
}
