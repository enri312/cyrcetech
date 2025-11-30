package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.entity.Equipment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class EquipmentController {

    @FXML
    private TableView<Equipment> equipmentTable;
    @FXML
    private TableColumn<Equipment, String> typeColumn;
    @FXML
    private TableColumn<Equipment, String> brandColumn;
    @FXML
    private TableColumn<Equipment, String> modelColumn;
    @FXML
    private TableColumn<Equipment, String> serialColumn;
    @FXML
    private TableColumn<Equipment, String> conditionColumn;

    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("deviceType"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        serialColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("physicalCondition"));

        // For now, we can't easily list all equipment without a method in service/DAO.
        // EquipmentDAO has findByCustomerId but not findAll.
        // Let's leave it empty or implement findAll later.
        // Or just show an alert saying "Select a client to view equipment" if we were
        // doing that flow.
        // But for this view, we probably want all equipment.
        // I'll leave it empty for now to avoid compilation errors if findAll is
        // missing.
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/MainView");
    }

    @FXML
    private void handleNewEquipment(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(com.cyrcetech.app.I18nUtil.getBundle().getString("equipment.pending"));
        alert.show();
    }
}
