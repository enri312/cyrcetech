package com.cyrcetech.controller;

import com.cyrcetech.CyrcetechApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainController {

    @FXML
    private void handleNewTicket(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/NewTicketView");
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/LoginView");
    }
}
