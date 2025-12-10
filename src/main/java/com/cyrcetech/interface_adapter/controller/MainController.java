package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.entity.Ticket;
import com.cyrcetech.entity.TicketStatus;
import com.cyrcetech.infrastructure.api.service.TicketApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.cyrcetech.infrastructure.session.SessionManager;
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

    @FXML
    private Button homeButton;
    @FXML
    private Button newOrderButton;
    @FXML
    private Button clientsButton;
    @FXML
    private Button equipmentButton;
    @FXML
    private Button ordersButton;
    @FXML
    private Button sparePartsButton;
    @FXML
    private Button technicalHistoryButton;
    @FXML
    private Button auditButton;

    private final TicketApiService ticketApiService = new TicketApiService();

    @FXML
    public void initialize() {
        applyRolePermissions();
        loadDashboardStats();
    }

    private void applyRolePermissions() {
        // Audit button is only for admins
        setButtonVisible(auditButton, SessionManager.getInstance().isAdmin());

        if (SessionManager.getInstance().isUser()) {
            // 📞 Usuario/User (Atención Cliente)
            // ✅ Registrar clientes y equipos
            // ✅ Crear órdenes al recibir equipos
            // ✅ Entregar equipos (marcar como entregado)
            // ❌ NO ve reportes financieros / historial técnico
            setButtonVisible(clientsButton, true); // Registrar clientes
            setButtonVisible(equipmentButton, true); // Registrar equipos
            setButtonVisible(sparePartsButton, false); // NO ve inventario técnico
            setButtonVisible(technicalHistoryButton, false); // NO ve reportes
        } else if (SessionManager.getInstance().isTechnician()) {
            // 🔧 Técnico/Technician (Reparador)
            // ✅ Ver inventario y alertas
            // ✅ Agregar repuestos a sus órdenes
            // ❌ NO ve finanzas / NO gestiona usuarios
            setButtonVisible(clientsButton, true); // Ver clientes
            setButtonVisible(equipmentButton, true); // Ver equipos
            setButtonVisible(sparePartsButton, true); // Ver inventario
            setButtonVisible(technicalHistoryButton, true); // Ver historial técnico
        }
        // 👔 Admin sees everything (default) - including Audit
    }

    private void setButtonVisible(Button button, boolean visible) {
        if (button != null) {
            button.setVisible(visible);
            button.setManaged(visible);
        }
    }

    @FXML
    private javafx.scene.chart.PieChart statusChart;

    private void loadDashboardStats() {
        try {
            List<Ticket> tickets = ticketApiService.getAllTickets();

            // Count by status
            long pending = tickets.stream()
                    .filter(t -> t.getStatus() == TicketStatus.PENDING || t.getStatus() == TicketStatus.DIAGNOSING)
                    .count();

            long inProgress = tickets.stream()
                    .filter(t -> t.getStatus() == TicketStatus.IN_PROGRESS
                            || t.getStatus() == TicketStatus.WAITING_PARTS)
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

            // Update Chart
            if (statusChart != null) {
                statusChart.getData().clear();
                statusChart.getData().addAll(
                        new javafx.scene.chart.PieChart.Data("Pendientes (" + pending + ")", pending),
                        new javafx.scene.chart.PieChart.Data("En Progreso (" + inProgress + ")", inProgress),
                        new javafx.scene.chart.PieChart.Data("Listos (" + ready + ")", ready),
                        new javafx.scene.chart.PieChart.Data("Entregados (" + delivered + ")", delivered));
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
    private void handleAudit(ActionEvent event) {
        try {
            CyrcetechApp.setRoot("view/AuditView");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/LoginView");
    }
}
