package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;

public class ReportsController {

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/MainView");
    }

    @FXML
    private void handleOrdersReport(ActionEvent event) {
        showNotImplemented("Reporte de Órdenes");
    }

    @FXML
    private void handleIncomeReport(ActionEvent event) {
        showNotImplemented("Reporte de Ingresos");
    }

    @FXML
    private void handleCustomersReport(ActionEvent event) {
        showNotImplemented("Reporte de Clientes");
    }

    @FXML
    private void handleInventoryReport(ActionEvent event) {
        showNotImplemented("Reporte de Inventario");
    }

    private void showNotImplemented(String reportName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(com.cyrcetech.app.I18nUtil.getBundle().getString("reports.pending.title"));
        alert.setHeaderText(reportName);
        alert.setContentText(com.cyrcetech.app.I18nUtil.getBundle().getString("reports.pending.message"));
        alert.show();
    }
}
