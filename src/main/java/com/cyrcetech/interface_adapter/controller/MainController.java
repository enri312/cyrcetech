package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.entity.Ticket;
import com.cyrcetech.entity.TicketStatus;
import com.cyrcetech.infrastructure.api.service.TicketApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    private Label pendingCountLabel;
    @FXML
    private Label readyCountLabel;
    @FXML
    private Label deliveredCountLabel;
    @FXML
    private VBox workshopTicketsContainer;
    @FXML
    private VBox pickupTicketsContainer;

    private final TicketApiService ticketApiService = new TicketApiService();

    @FXML
    public void initialize() {
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        try {
            List<Ticket> tickets = ticketApiService.getAllTickets();

            // Count by status
            long pending = tickets.stream()
                    .filter(t -> t.getStatus() == TicketStatus.PENDING || t.getStatus() == TicketStatus.DIAGNOSING)
                    .count();

            long ready = tickets.stream()
                    .filter(t -> t.getStatus() == TicketStatus.READY)
                    .count();

            long delivered = tickets.stream()
                    .filter(t -> t.getStatus() == TicketStatus.DELIVERED)
                    .count();

            // Update counters
            if (pendingCountLabel != null) {
                pendingCountLabel.setText(String.valueOf(pending));
            }
            if (readyCountLabel != null) {
                readyCountLabel.setText(String.valueOf(ready));
            }
            if (deliveredCountLabel != null) {
                deliveredCountLabel.setText(String.valueOf(delivered));
            }

            // Load workshop tickets (in progress: PENDING, DIAGNOSING, IN_PROGRESS,
            // WAITING_PARTS)
            List<Ticket> workshopTickets = tickets.stream()
                    .filter(t -> t.getStatus() != null && t.getStatus().isActive()
                            && t.getStatus() != TicketStatus.READY)
                    .collect(Collectors.toList());
            loadTicketsIntoContainer(workshopTicketsContainer, workshopTickets);

            // Load ready for pickup tickets
            List<Ticket> pickupTickets = tickets.stream()
                    .filter(t -> t.getStatus() == TicketStatus.READY)
                    .collect(Collectors.toList());
            loadTicketsIntoContainer(pickupTicketsContainer, pickupTickets);

        } catch (Exception e) {
            System.err.println("Error loading dashboard stats: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTicketsIntoContainer(VBox container, List<Ticket> tickets) {
        if (container == null)
            return;

        container.getChildren().clear();

        if (tickets.isEmpty()) {
            Label emptyLabel = new Label("No hay tickets");
            emptyLabel.setStyle("-fx-text-fill: #808090; -fx-font-style: italic;");
            container.getChildren().add(emptyLabel);
        } else {
            for (Ticket ticket : tickets) {
                HBox ticketCard = createTicketCard(ticket);
                container.getChildren().add(ticketCard);
            }
        }
    }

    private HBox createTicketCard(Ticket ticket) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: #1a1a2e; -fx-background-radius: 8; -fx-padding: 10;");

        // Ticket info
        VBox info = new VBox(3);

        String customerName = ticket.getCustomer() != null ? ticket.getCustomer().name() : "Sin cliente";
        Label nameLabel = new Label(customerName);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        String deviceInfo = ticket.getDeviceSummary();
        Label deviceLabel = new Label(deviceInfo);
        deviceLabel.setStyle("-fx-text-fill: #a0a0b0; -fx-font-size: 12px;");

        String statusText = ticket.getStatus() != null ? ticket.getStatus().getDisplayName() : "—";
        Label statusLabel = new Label(statusText);
        String statusColor = ticket.getStatus() != null ? ticket.getStatus().getColorCode() : "#808080";
        statusLabel.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-size: 11px;");

        info.getChildren().addAll(nameLabel, deviceLabel, statusLabel);
        card.getChildren().add(info);

        return card;
    }

    @FXML
    private void handleNewTicket(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/NewTicketView");
    }

    @FXML
    private void handleClients(ActionEvent event) {
        try {
            CyrcetechApp.setRoot("view/ClientView");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEquipment(ActionEvent event) {
        try {
            CyrcetechApp.setRoot("view/EquipmentView");
        } catch (IOException e) {
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
            CyrcetechApp.setRoot("view/TechnicalHistoryView");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/LoginView");
    }
}
