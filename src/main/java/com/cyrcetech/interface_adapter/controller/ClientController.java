package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.app.DependencyContainer;
import com.cyrcetech.entity.Customer;
import com.cyrcetech.usecase.CustomerService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class ClientController {

    @FXML
    private TableView<Customer> clientTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> taxIdColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;

    private final CustomerService customerService = DependencyContainer.getCustomerService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        taxIdColumn.setCellValueFactory(new PropertyValueFactory<>("taxId"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadClients();
    }

    private void loadClients() {
        clientTable.setItems(FXCollections.observableArrayList(customerService.getAllCustomers()));
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/MainView");
    }

    @FXML
    private void handleNewClient(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Funcionalidad de crear cliente desde aquí pendiente.");
        alert.show();
    }
}
