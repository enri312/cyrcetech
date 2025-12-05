package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.entity.Ticket;
import com.cyrcetech.entity.TicketStatus;
import com.cyrcetech.infrastructure.api.service.TicketApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private Label pendingCountLabel;
    @FXML
    private Label readyCountLabel;
    @FXML
    private Label deliveredCountLabel;

    private final TicketApiService ticketApiService = new TicketApiService();

    @FXML
    public void initialize() {
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        try {
            List<Ticket> tickets = ticketApiService.getAllTickets();

            long pending = tickets.stream()
                    .filter(t -> t.getStatus() == TicketStatus.PENDING || t.getStatus() == TicketStatus.DIAGNOSING)
                    .count();

            long ready = tickets.stream()
                    .filter(t -> t.getStatus() == TicketStatus.READY)
                    .count();

            long delivered = tickets.stream()
                    .filter(t -> t.getStatus() == TicketStatus.DELIVERED)
                    .count();

            if (pendingCountLabel != null) {
                pendingCountLabel.setText(String.valueOf(pending));
            }
            if (readyCountLabel != null) {
                readyCountLabel.setText(String.valueOf(ready));
            }
            if (deliveredCountLabel != null) {
                deliveredCountLabel.setText(String.valueOf(delivered));
            }
        } catch (Exception e) {
            System.err.println("Error loading dashboard stats: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewTicket(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/NewTicketView");
    }

    @FXML
    private void handleClients(ActionEvent event) {
        try {
            System.out.println("DEBUG: handleClients called");
            CyrcetechApp.setRoot("view/ClientView");
            System.out.println("DEBUG: ClientView loaded successfully");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load ClientView");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEquipment(ActionEvent event) {
        try {
            System.out.println("DEBUG: handleEquipment called");
            CyrcetechApp.setRoot("view/EquipmentView");
            System.out.println("DEBUG: EquipmentView loaded successfully");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load EquipmentView");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrders(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/OrdersView");
    }

    @FXML
    private void handleSpareParts(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/SparePartsView");
    }

    @FXML
    private void handleTechnicalHistory(ActionEvent event) {
        try {
            System.out.println("DEBUG: handleTechnicalHistory called");
            CyrcetechApp.setRoot("view/TechnicalHistoryView");
            System.out.println("DEBUG: TechnicalHistoryView loaded successfully");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load TechnicalHistoryView");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/LoginView");
    }
}
