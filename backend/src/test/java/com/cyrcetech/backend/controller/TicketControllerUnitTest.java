package com.cyrcetech.backend.controller;

import com.cyrcetech.backend.dto.request.CreateTicketRequest;
import com.cyrcetech.backend.dto.response.TicketResponse;
import com.cyrcetech.backend.service.TicketService;
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
class TicketControllerUnitTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @Test
    void getAllTickets_ShouldReturnList() {
        TicketResponse response = new TicketResponse();
        response.setId("1");
        when(ticketService.getAllTickets()).thenReturn(Arrays.asList(response));

        ResponseEntity<List<TicketResponse>> result = ticketController.getAllTickets();

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        verify(ticketService).getAllTickets();
    }

    @Test
    void createTicket_ShouldReturnCreated() {
        CreateTicketRequest request = new CreateTicketRequest("cust-1", "eq-1", "Broken screen");
        TicketResponse response = new TicketResponse();
        response.setId("new");
        when(ticketService.createTicket(request)).thenReturn(response);

        ResponseEntity<TicketResponse> result = ticketController.createTicket(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("new", result.getBody().getId());
        verify(ticketService).createTicket(request);
    }
}
