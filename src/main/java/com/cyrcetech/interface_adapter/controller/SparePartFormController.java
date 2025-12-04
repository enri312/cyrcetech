package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.I18nUtil;
import com.cyrcetech.entity.SparePart;
import com.cyrcetech.infrastructure.api.service.SparePartApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SparePartFormController {

    @FXML
    private Label titleLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField providerField;

    private final SparePartApiService sparePartApiService = new SparePartApiService();
    private SparePart existingSparePart;
    private Runnable onSaveCallback;

    public void setSparePart(SparePart sparePart) {
        this.existingSparePart = sparePart;
        if (sparePart != null) {
            titleLabel.setText(I18nUtil.getBundle().getString("sparePart.form.title.edit"));
            nameField.setText(sparePart.name());
            priceField.setText(String.valueOf(sparePart.price()));
            stockField.setText(String.valueOf(sparePart.stock()));
            providerField.setText(sparePart.provider());
        } else {
            titleLabel.setText(I18nUtil.getBundle().getString("sparePart.form.title.new"));
        }
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (validateInput()) {
            String id = existingSparePart != null ? existingSparePart.id() : "";

            try {
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());

                SparePart sparePart = new SparePart(
                        id,
                        nameField.getText(),
                        price,
                        stock,
                        providerField.getText());

                if (existingSparePart != null) {
                    sparePartApiService.updateSparePart(existingSparePart.id(), sparePart);
                } else {
                    sparePartApiService.createSparePart(sparePart);
                }

                if (onSaveCallback != null) {
                    onSaveCallback.run();
                }
                closeWindow();
            } catch (NumberFormatException e) {
                showError(I18nUtil.getBundle().getString("sparePart.form.error.numeric"));
            } catch (Exception e) {
                showError("Error saving spare part: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty() || priceField.getText().isEmpty() || stockField.getText().isEmpty()) {
            showError(I18nUtil.getBundle().getString("sparePart.form.error.required"));
            return false;
        }

        try {
            Double.parseDouble(priceField.getText());
            Integer.parseInt(stockField.getText());
        } catch (NumberFormatException e) {
            showError(I18nUtil.getBundle().getString("sparePart.form.error.numeric"));
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
