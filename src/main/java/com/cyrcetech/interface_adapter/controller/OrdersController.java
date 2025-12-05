package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.entity.Ticket;
import com.cyrcetech.infrastructure.api.service.TicketApiService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
            // Optionally show error alert here
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
}
