package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.dto.request.CreateInvoiceRequest;
import com.cyrcetech.backend.dto.response.InvoiceResponse;
import com.cyrcetech.backend.service.InvoiceService;
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
class InvoiceControllerUnitTest {

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    @Test
    void getAllInvoices_ShouldReturnList() {
        InvoiceResponse response = new InvoiceResponse();
        response.setId("1");
        when(invoiceService.getAllInvoices()).thenReturn(Arrays.asList(response));

        ResponseEntity<List<InvoiceResponse>> result = invoiceController.getAllInvoices();

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        verify(invoiceService).getAllInvoices();
    }

    @Test
    void createInvoice_ShouldReturnCreated() {
        CreateInvoiceRequest request = new CreateInvoiceRequest();
        request.setInvoiceNumber("INV-001");
        request.setSubtotal(100.0);
        request.setTotalAmount(110.0);

        InvoiceResponse response = new InvoiceResponse();
        response.setId("new");
        when(invoiceService.createInvoice(request)).thenReturn(response);

        ResponseEntity<InvoiceResponse> result = invoiceController.createInvoice(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("new", result.getBody().getId());
        verify(invoiceService).createInvoice(request);
    }
}
