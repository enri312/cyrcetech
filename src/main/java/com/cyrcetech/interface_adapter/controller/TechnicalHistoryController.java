package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.entity.Ticket;
import com.cyrcetech.infrastructure.api.service.TicketApiService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TechnicalHistoryController {

    @FXML
    private TableView<Ticket> historyTable;
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
    @FXML
    private TextField searchField;

    private final TicketApiService ticketApiService = new TicketApiService();
    private List<Ticket> allTickets;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCustomer() != null ? cellData.getValue().getCustomer().name() : "N/A"));
        deviceColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDeviceSummary()));
        problemColumn.setCellValueFactory(new PropertyValueFactory<>("problemDescription"));
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getStatus() != null ? cellData.getValue().getStatus().getDisplayName() : "N/A"));
        dateColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFormattedDate()));

        loadHistory();
    }

    private void loadHistory() {
        try {
            allTickets = ticketApiService.getAllTickets();
            historyTable.setItems(FXCollections.observableArrayList(allTickets));
        } catch (Exception e) {
            showError("Error loading tickets: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            historyTable.setItems(FXCollections.observableArrayList(allTickets));
        } else {
            List<Ticket> filtered = allTickets.stream()
                    .filter(ticket -> ticket.getId().toLowerCase().contains(searchText)
                            || (ticket.getCustomer() != null
                                    && ticket.getCustomer().name().toLowerCase().contains(searchText))
                            || ticket.getDeviceSummary().toLowerCase().contains(searchText)
                            || ticket.getProblemDescription().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            historyTable.setItems(FXCollections.observableArrayList(filtered));
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            System.out.println("DEBUG: handleBack called from TechnicalHistory");
            CyrcetechApp.setRoot("view/MainView");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to navigate back to MainView");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadHistory();
        searchField.clear();
    }
}
