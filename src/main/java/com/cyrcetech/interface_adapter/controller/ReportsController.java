package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.cyrcetech.infrastructure.PDFReportService;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;

public class ReportsController {

    private final PDFReportService pdfReportService = new PDFReportService();

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/MainView");
    }

    @FXML
    private void handleOrdersReport(ActionEvent event) {
        generateReport("Reporte_Ordenes.pdf", () -> {
            try {
                File file = showSaveDialog("Reporte_Ordenes.pdf");
                if (file != null) {
                    pdfReportService.generateOrdersReport(file);
                    showSuccess(file);
                }
            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void handleIncomeReport(ActionEvent event) {
        // Income report requires billing module - show not implemented
        showNotImplemented("Reporte de Ingresos");
    }

    @FXML
    private void handleCustomersReport(ActionEvent event) {
        generateReport("Reporte_Clientes.pdf", () -> {
            try {
                File file = showSaveDialog("Reporte_Clientes.pdf");
                if (file != null) {
                    pdfReportService.generateCustomersReport(file);
                    showSuccess(file);
                }
            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void handleInventoryReport(ActionEvent event) {
        generateReport("Reporte_Inventario.pdf", () -> {
            try {
                File file = showSaveDialog("Reporte_Inventario.pdf");
                if (file != null) {
                    pdfReportService.generateInventoryReport(file);
                    showSuccess(file);
                }
            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }

    private void generateReport(String defaultFileName, Runnable reportGenerator) {
        reportGenerator.run();
    }

    private File showSaveDialog(String defaultFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(com.cyrcetech.app.I18nUtil.getBundle().getString("reports.save.title"));
        fileChooser.setInitialFileName(defaultFileName);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        return fileChooser.showSaveDialog(new Stage());
    }

    private void showSuccess(File file) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(
                com.cyrcetech.app.I18nUtil.getBundle().getString("reports.success") + "\n" + file.getAbsolutePath());
        alert.showAndWait();

        // Try to open the PDF
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            // Silently ignore if can't open
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(com.cyrcetech.app.I18nUtil.getBundle().getString("reports.error") + "\n" + message);
        alert.show();
    }

    private void showNotImplemented(String reportName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(com.cyrcetech.app.I18nUtil.getBundle().getString("reports.pending.title"));
        alert.setHeaderText(reportName);
        alert.setContentText(com.cyrcetech.app.I18nUtil.getBundle().getString("reports.pending.message"));
        alert.show();
    }
}
