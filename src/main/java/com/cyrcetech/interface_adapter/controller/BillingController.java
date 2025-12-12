package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;

import com.cyrcetech.entity.Invoice;
import com.cyrcetech.infrastructure.api.service.BillingApiService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class BillingController {

    @FXML
    private ComboBox<String> reportTypeCombo;
    @FXML
    private DatePicker datePicker;
    @FXML
    private HBox dayFilterBox;
    @FXML
    private HBox monthFilterBox;
    @FXML
    private ComboBox<String> monthCombo;
    @FXML
    private ComboBox<Integer> yearCombo;

    @FXML
    private Label totalInvoicedLabel;
    @FXML
    private Label totalCollectedLabel;
    @FXML
    private Label pendingBalanceLabel;

    @FXML
    private TableView<Invoice> invoicesTable;
    @FXML
    private TableColumn<Invoice, String> numberColumn;
    @FXML
    private TableColumn<Invoice, String> dateColumn;
    @FXML
    private TableColumn<Invoice, String> totalColumn;
    @FXML
    private TableColumn<Invoice, String> paidColumn;
    @FXML
    private TableColumn<Invoice, String> balanceColumn;
    @FXML
    private TableColumn<Invoice, String> statusColumn;
    @FXML
    private TableColumn<Invoice, String> methodColumn;

    private final BillingApiService billingApiService = new BillingApiService();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(
            Locale.of("es", "PY"));

    @FXML
    public void initialize() {
        // Enforce 0 decimals
        currencyFormat.setMaximumFractionDigits(0);

        setupFilters();
        setupTable();

        // Default to daily report for today
        reportTypeCombo.setValue("Diario");
        datePicker.setValue(LocalDate.now());
        handleSearch(null);
    }

    private void setupFilters() {
        reportTypeCombo.setItems(FXCollections.observableArrayList("Diario", "Mensual", "Anual"));
        reportTypeCombo.setOnAction(e -> updateFilterVisibility());

        // Months
        for (Month month : Month.values()) {
            monthCombo.getItems().add(month.getDisplayName(TextStyle.FULL, Locale.of("es", "ES")));
        }
        monthCombo.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);

        // Years
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= currentYear - 5; i--) {
            yearCombo.getItems().add(i);
        }
        yearCombo.getSelectionModel().selectFirst();
    }

    private void updateFilterVisibility() {
        String type = reportTypeCombo.getValue();
        if ("Diario".equals(type)) {
            dayFilterBox.setVisible(true);
            dayFilterBox.setManaged(true);
            monthFilterBox.setVisible(false);
            monthFilterBox.setManaged(false);
        } else if ("Mensual".equals(type)) {
            dayFilterBox.setVisible(false);
            dayFilterBox.setManaged(false);
            monthFilterBox.setVisible(true);
            monthFilterBox.setManaged(true);
            monthCombo.setVisible(true); // Show Month
        } else { // Anual
            dayFilterBox.setVisible(false);
            dayFilterBox.setManaged(false);
            monthFilterBox.setVisible(true);
            monthFilterBox.setManaged(true);
            monthCombo.setVisible(false); // Hide Month, only Year
        }
    }

    private void setupTable() {
        numberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().invoiceNumber()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().issueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        totalColumn
                .setCellValueFactory(data -> new SimpleStringProperty(formatCurrency(data.getValue().totalAmount())));
        paidColumn.setCellValueFactory(data -> new SimpleStringProperty(formatCurrency(data.getValue().paidAmount())));
        balanceColumn.setCellValueFactory(data -> new SimpleStringProperty(formatCurrency(data.getValue().balance())));

        statusColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().paymentStatus().getDisplayName()));
        methodColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().paymentMethod() != null ? data.getValue().paymentMethod().toString() : "-"));
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        try {
            List<Invoice> invoices;
            String type = reportTypeCombo.getValue();

            if ("Diario".equals(type)) {
                if (datePicker.getValue() == null) {
                    showError("Seleccione una fecha");
                    return;
                }
                invoices = billingApiService.getDailyBilling(datePicker.getValue());
            } else if ("Mensual".equals(type)) {
                int year = yearCombo.getValue();
                int month = monthCombo.getSelectionModel().getSelectedIndex() + 1;
                invoices = billingApiService.getMonthlyBilling(year, month);
            } else {
                int year = yearCombo.getValue();
                invoices = billingApiService.getYearlyBilling(year);
            }

            updateSummary(invoices);
            invoicesTable.setItems(FXCollections.observableArrayList(invoices));

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error al cargar reporte: " + e.getMessage());
        }
    }

    private void updateSummary(List<Invoice> invoices) {
        double totalInvoiced = invoices.stream().mapToDouble(Invoice::totalAmount).sum();
        double totalCollected = invoices.stream().mapToDouble(Invoice::paidAmount).sum();
        double pending = totalInvoiced - totalCollected;

        totalInvoicedLabel.setText(formatCurrency(totalInvoiced));
        totalCollectedLabel.setText(formatCurrency(totalCollected));
        pendingBalanceLabel.setText(formatCurrency(pending));
    }

    private String formatCurrency(double amount) {
        return currencyFormat.format(amount);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/MainView");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
