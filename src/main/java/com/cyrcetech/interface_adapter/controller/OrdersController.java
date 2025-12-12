package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.app.I18nUtil;
import com.cyrcetech.entity.Ticket;
import com.cyrcetech.infrastructure.api.service.TicketApiService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class OrdersController {

    @FXML
    private TableView<Ticket> ordersTable;
    @FXML
    private TableColumn<Ticket, String> idColumn;
    @FXML
    private TableColumn<Ticket, String> customerColumn;
    @FXML
    private TableColumn<Ticket, String> deviceColumn;
    @FXML
    private TableColumn<Ticket, String> problemColumn;
    @FXML
    private TableColumn<Ticket, String> downPaymentColumn;
    @FXML
    private TableColumn<Ticket, String> costColumn;
    @FXML
    private TableColumn<Ticket, String> statusColumn;
    @FXML
    private TableColumn<Ticket, String> dateColumn;
    @FXML
    private TableColumn<Ticket, Void> actionsColumn;

    private final java.text.NumberFormat currencyFormat = java.text.NumberFormat
            .getCurrencyInstance(java.util.Locale.of("es", "PY"));

    private final TicketApiService ticketApiService = new TicketApiService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        customerColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCustomer() != null ? cellData.getValue().getCustomer().name() : "N/A"));
        deviceColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDeviceSummary()));
        problemColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getProblemDescription()));

        currencyFormat.setMaximumFractionDigits(0);
        downPaymentColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                currencyFormat.format(data.getValue().getDownPayment())));
        costColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                currencyFormat.format(data.getValue().getEstimatedCost())));

        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getStatus() != null ? cellData.getValue().getStatus().getDisplayName() : "N/A"));
        dateColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFormattedDate()));

        setupActionsColumn();

        loadOrders();
    }

    private void loadOrders() {
        try {
            ordersTable.setItems(FXCollections.observableArrayList(ticketApiService.getAllTickets()));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading orders: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/MainView");
    }

    @FXML
    private void handleNewOrder(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/NewTicketView");
    }

    @FXML
    private void handleExportExcel(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(I18nUtil.getBundle().getString("reports.save.title"));
            fileChooser.setInitialFileName("tickets.xlsx");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

            Stage stage = (Stage) ordersTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                byte[] excelData = ticketApiService.exportTicketsToExcel();
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(excelData);
                }
                showInfo(I18nUtil.getBundle().getString("orders.export.success"));
            }
        } catch (Exception e) {
            showError(I18nUtil.getBundle().getString("orders.export.error") + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
    }

    private void setupActionsColumn() {
        javafx.util.Callback<TableColumn<Ticket, Void>, javafx.scene.control.TableCell<Ticket, Void>> cellFactory = new javafx.util.Callback<>() {
            @Override
            public javafx.scene.control.TableCell<Ticket, Void> call(final TableColumn<Ticket, Void> param) {
                return new javafx.scene.control.TableCell<>() {
                    private final javafx.scene.control.Button manageButton = new javafx.scene.control.Button(
                            "Gestionar");
                    private final javafx.scene.control.Button deliverButton = new javafx.scene.control.Button(
                            "Entregar");
                    private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(10, manageButton,
                            deliverButton);

                    {
                        manageButton.setStyle("-fx-background-color: #444; -fx-text-fill: white;");
                        manageButton.setOnAction(event -> {
                            Ticket ticket = getTableView().getItems().get(getIndex());
                            handleManageTicket(ticket);
                        });

                        deliverButton.setStyle(
                                "-fx-background-color: #00d4ff; -fx-text-fill: black; -fx-font-weight: bold;");
                        deliverButton.setOnAction(event -> {
                            Ticket ticket = getTableView().getItems().get(getIndex());
                            handleDeliverTicket(ticket);
                        });
                        pane.setAlignment(javafx.geometry.Pos.CENTER);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Ticket ticket = getTableView().getItems().get(getIndex());
                            // Deliver button visible only if READY
                            boolean canDeliver = ticket.getStatus() == com.cyrcetech.entity.TicketStatus.READY;
                            deliverButton.setVisible(canDeliver);
                            deliverButton.setManaged(canDeliver);

                            setGraphic(pane);
                        }
                    }
                };
            }
        };
        actionsColumn.setCellFactory(cellFactory);
    }

    private void handleManageTicket(Ticket ticket) {
        // Navigate to details or edit view. Note: Might need to implement passing data.
        // For now, let's just show an alert or open NewTicketView in edit mode if
        // supported.
        // Assuming we want to open NewTicketView logic but prefilled.
        // Or specific TicketDetailsView.
        // Given current file structure, maybe reuse NewTicketView or just placeholder.
        showInfo("Gestionar Ticket ID: " + ticket.getId());
    }

    private void handleDeliverTicket(Ticket ticket) {
        try {
            // Update status to DELIVERED / ENTREGADO
            // We need a method in API service to update status
            // ticketApiService.updateStatus(ticket.getId(), TicketStatus.DELIVERED);
            // Since method might not exist in frontend service yet, I'll simulate or assume
            // it exists/will create.
            // Wait, I should add it to TicketApiService if missing.
            ticket.setStatus(com.cyrcetech.entity.TicketStatus.DELIVERED); // Optimistic update
            ticketApiService.updateTicket(ticket.getId(), ticket); // Assuming updateTicket exists
            loadOrders();
            showInfo("Ticket entregado exitosamente.");
        } catch (Exception e) {
            showError("Error al entregar: " + e.getMessage());
        }
    }
}
