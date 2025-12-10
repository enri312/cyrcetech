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
    private TableColumn<Ticket, String> statusColumn;
    @FXML
    private TableColumn<Ticket, String> dateColumn;

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
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getStatus() != null ? cellData.getValue().getStatus().getDisplayName() : "N/A"));
        dateColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFormattedDate()));

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
        alert.show();
    }
}
