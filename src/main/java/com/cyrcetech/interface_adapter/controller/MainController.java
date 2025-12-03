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
