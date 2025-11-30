package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.entity.SparePart;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class SparePartsController {

    @FXML
    private TableView<SparePart> sparePartsTable;
    @FXML
    private TableColumn<SparePart, String> nameColumn;
    @FXML
    private TableColumn<SparePart, Double> priceColumn;
    @FXML
    private TableColumn<SparePart, Integer> stockColumn;
    @FXML
    private TableColumn<SparePart, String> providerColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        providerColumn.setCellValueFactory(new PropertyValueFactory<>("provider"));

        // For now, empty list - SparePartService not yet implemented
        sparePartsTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/MainView");
    }

    @FXML
    private void handleNewSparePart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(com.cyrcetech.app.I18nUtil.getBundle().getString("spareParts.pending"));
        alert.show();
    }
}
