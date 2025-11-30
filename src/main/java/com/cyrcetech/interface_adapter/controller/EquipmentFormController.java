package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.DependencyContainer;
import com.cyrcetech.app.I18nUtil;
import com.cyrcetech.entity.Customer;
import com.cyrcetech.entity.DeviceType;
import com.cyrcetech.entity.Equipment;
import com.cyrcetech.usecase.CustomerService;
import com.cyrcetech.usecase.EquipmentService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class EquipmentFormController {

    @FXML
    private Label titleLabel;
    @FXML
    private ComboBox<Customer> customerComboBox;
    @FXML
    private ComboBox<DeviceType> typeComboBox;
    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField serialField;
    @FXML
    private TextField conditionField;

    private final EquipmentService equipmentService = DependencyContainer.getEquipmentService();
    private final CustomerService customerService = DependencyContainer.getCustomerService();
    private Equipment existingEquipment;
    private Runnable onSaveCallback;

    @FXML
    public void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList(DeviceType.values()));

        customerComboBox.setItems(FXCollections.observableArrayList(customerService.getAllCustomers()));
        customerComboBox.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return customer != null ? customer.name() + " (" + customer.taxId() + ")" : "";
            }

            @Override
            public Customer fromString(String string) {
                return null; // Not needed for read-only combo
            }
        });
    }

    public void setEquipment(Equipment equipment) {
        this.existingEquipment = equipment;
        if (equipment != null) {
            titleLabel.setText(I18nUtil.getBundle().getString("equipment.form.title.edit"));
            brandField.setText(equipment.brand());
            modelField.setText(equipment.model());
            serialField.setText(equipment.serialNumber());
            conditionField.setText(equipment.physicalCondition());
            typeComboBox.setValue(equipment.deviceType());

            // Find and select the customer
            Customer customer = customerService.getCustomerById(equipment.customerId());
            customerComboBox.setValue(customer);
            // Disable customer change on edit if desired, or leave enabled.
            // customerComboBox.setDisable(true);
        } else {
            titleLabel.setText(I18nUtil.getBundle().getString("equipment.form.title.new"));
        }
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (validateInput()) {
            String id = existingEquipment != null ? existingEquipment.id() : "";
            Customer selectedCustomer = customerComboBox.getValue();

            Equipment equipment = new Equipment(
                    id,
                    brandField.getText(),
                    modelField.getText(),
                    typeComboBox.getValue(),
                    serialField.getText(),
                    conditionField.getText(),
                    selectedCustomer.id());

            try {
                if (existingEquipment != null) {
                    equipmentService.updateEquipment(equipment);
                } else {
                    equipmentService.createEquipment(equipment);
                }
                if (onSaveCallback != null) {
                    onSaveCallback.run();
                }
                closeWindow();
            } catch (Exception e) {
                showError("Error saving equipment: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private boolean validateInput() {
        if (customerComboBox.getValue() == null || typeComboBox.getValue() == null ||
                brandField.getText().isEmpty() || modelField.getText().isEmpty()) {
            showError(I18nUtil.getBundle().getString("equipment.form.error.required"));
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
        Stage stage = (Stage) brandField.getScene().getWindow();
        stage.close();
    }
}
