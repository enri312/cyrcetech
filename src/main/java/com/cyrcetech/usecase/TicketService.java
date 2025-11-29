package com.cyrcetech.usecase;

import com.cyrcetech.entity.Ticket;
import java.util.List;

public interface TicketService {
    void createTicket(Ticket ticket);

    List<Ticket> getAllTickets();

    Ticket getTicketById(String id);

    void updateTicket(Ticket ticket);
}
