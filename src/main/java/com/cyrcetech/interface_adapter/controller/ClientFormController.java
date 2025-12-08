package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.I18nUtil;
import com.cyrcetech.entity.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClientFormController {

    @FXML
    private Label titleLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField taxIdField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;

    private final com.cyrcetech.infrastructure.api.service.CustomerApiService customerApiService = new com.cyrcetech.infrastructure.api.service.CustomerApiService();
    private Customer existingCustomer;
    private Runnable onSaveCallback;

    public void setCustomer(Customer customer) {
        this.existingCustomer = customer;
        if (customer != null) {
            titleLabel.setText(I18nUtil.getBundle().getString("client.form.title.edit"));
            nameField.setText(customer.name());
            taxIdField.setText(customer.taxId());
            phoneField.setText(customer.phone());
            addressField.setText(customer.address());
        } else {
            titleLabel.setText(I18nUtil.getBundle().getString("client.form.title.new"));
        }
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (validateInput()) {
            String id = existingCustomer != null ? existingCustomer.id() : "";
            Customer customer = new Customer(
                    id,
                    nameField.getText(),
                    taxIdField.getText(),
                    addressField.getText(),
                    phoneField.getText());

            try {
                if (existingCustomer != null) {
                    customerApiService.updateCustomer(existingCustomer.id(), customer);
                } else {
                    customerApiService.createCustomer(customer);
                }
                if (onSaveCallback != null) {
                    onSaveCallback.run();
                }
                closeWindow();
            } catch (Exception e) {
                showError("Error saving client: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty() || taxIdField.getText().isEmpty()) {
            showError(I18nUtil.getBundle().getString("client.form.error.required"));
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
