package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.dto.request.CreateSparePartRequest;
import com.cyrcetech.backend.dto.response.SparePartResponse;
import com.cyrcetech.backend.service.SparePartService;
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
class SparePartControllerUnitTest {

    @Mock
    private SparePartService sparePartService;

    @InjectMocks
    private SparePartController sparePartController;

    @Test
    void getAllSpareParts_ShouldReturnList() {
        SparePartResponse response = new SparePartResponse();
        response.setId("1");
        when(sparePartService.getAllSpareParts()).thenReturn(Arrays.asList(response));

        ResponseEntity<List<SparePartResponse>> result = sparePartController.getAllSpareParts();

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        verify(sparePartService).getAllSpareParts();
    }

    @Test
    void createSparePart_ShouldReturnCreated() {
        CreateSparePartRequest request = new CreateSparePartRequest("Part A", 10.0, 5, "Provider X");
        SparePartResponse response = new SparePartResponse();
        response.setId("new");
        when(sparePartService.createSparePart(request)).thenReturn(response);

        ResponseEntity<SparePartResponse> result = sparePartController.createSparePart(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("new", result.getBody().getId());
        verify(sparePartService).createSparePart(request);
    }
}
