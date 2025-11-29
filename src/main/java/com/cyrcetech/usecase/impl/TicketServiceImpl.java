package com.cyrcetech.usecase.impl;

import com.cyrcetech.entity.Ticket;
import com.cyrcetech.usecase.TicketService;
import java.util.ArrayList;
import java.util.List;

public class TicketServiceImpl implements TicketService {
    private final List<Ticket> tickets = new ArrayList<>();

    @Override
    public void createTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return new ArrayList<>(tickets);
    }

    @Override
    public Ticket getTicketById(String id) {
        return tickets.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateTicket(Ticket ticket) {
        // In a real DB, we'd update. Here we just replace in list if needed,
        // but since we return references in getAllTickets, modifications might already
        // be visible.
        // For safety/clarity in this in-memory impl:
        int index = -1;
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getId().equals(ticket.getId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            tickets.set(index, ticket);
        }
    }
}
