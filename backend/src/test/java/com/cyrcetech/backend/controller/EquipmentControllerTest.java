package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.domain.entity.DeviceType;
import com.cyrcetech.backend.dto.request.CreateEquipmentRequest;
import com.cyrcetech.backend.dto.request.UpdateEquipmentRequest;
import com.cyrcetech.backend.dto.response.EquipmentResponse;
import com.cyrcetech.backend.service.EquipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipmentController.class)
public class EquipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipmentService equipmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllEquipment_ShouldReturnList() throws Exception {
        EquipmentResponse equipment1 = new EquipmentResponse();
        equipment1.setId("1");
        equipment1.setBrand("Dell");

        List<EquipmentResponse> equipmentList = Arrays.asList(equipment1);

        when(equipmentService.getAllEquipment()).thenReturn(equipmentList);

        mockMvc.perform(get("/api/equipment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].brand").value("Dell"));
    }

    @Test
    void getEquipmentById_ShouldReturnEquipment() throws Exception {
        EquipmentResponse equipment = new EquipmentResponse();
        equipment.setId("1");
        equipment.setBrand("HP");

        when(equipmentService.getEquipmentById("1")).thenReturn(equipment);

        mockMvc.perform(get("/api/equipment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("HP"));
    }

    @Test
    void createEquipment_ShouldReturnCreated() throws Exception {
        CreateEquipmentRequest request = new CreateEquipmentRequest();
        request.setBrand("Apple");
        request.setModel("MacBook Pro");
        request.setDeviceType(DeviceType.NOTEBOOK);
        request.setCustomerId("cust-1");

        EquipmentResponse response = new EquipmentResponse();
        response.setId("new-id");
        response.setBrand("Apple");

        when(equipmentService.createEquipment(any(CreateEquipmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/equipment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Apple"));
    }

    @Test
    void updateEquipment_ShouldReturnUpdated() throws Exception {
        UpdateEquipmentRequest request = new UpdateEquipmentRequest();
        request.setBrand("Apple Updated");

        EquipmentResponse response = new EquipmentResponse();
        response.setId("1");
        response.setBrand("Apple Updated");

        when(equipmentService.updateEquipment(eq("1"), any(UpdateEquipmentRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/equipment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Apple Updated"));
    }

    @Test
    void deleteEquipment_ShouldReturnNoContent() throws Exception {
        doNothing().when(equipmentService).deleteEquipment("1");

        mockMvc.perform(delete("/api/equipment/1"))
                .andExpect(status().isNoContent());
    }
}
