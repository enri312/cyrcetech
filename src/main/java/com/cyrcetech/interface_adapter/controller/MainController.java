package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainController {

    @FXML
    private void handleNewTicket(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/NewTicketView");
    }

    @FXML
    private void handleClients(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/ClientView");
    }

    @FXML
    private void handleEquipment(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/EquipmentView");
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
    private void handleLogout(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/LoginView");
    }
}
