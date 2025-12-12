package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.entity.Invoice;
import com.cyrcetech.entity.PaymentMethod;
import com.cyrcetech.entity.PaymentStatus;
import com.cyrcetech.infrastructure.api.service.InvoiceApiService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class InvoiceFormController {

    private final InvoiceApiService invoiceApiService = new InvoiceApiService();
    private Invoice currentInvoice;
    private Runnable onSaveCallback;

    @FXML
    private Label titleLabel;
    @FXML
    private TextField ticketIdField;
    @FXML
    private TextField invoiceNumberField;
    @FXML
    private DatePicker issueDatePicker;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private TextField subtotalField;
    @FXML
    private TextField taxAmountField;
    @FXML
    private TextField totalAmountField;
    @FXML
    private TextField paidAmountField;
    @FXML
    private ComboBox<PaymentStatus> paymentStatusCombo;
    @FXML
    private ComboBox<PaymentMethod> paymentMethodCombo;
    @FXML
    private DatePicker paymentDatePicker;
    @FXML
    private TextArea notesArea;

    @FXML
    public void initialize() {
        paymentStatusCombo.setItems(FXCollections.observableArrayList(PaymentStatus.values()));
        paymentMethodCombo.setItems(FXCollections.observableArrayList(PaymentMethod.values()));

        // Auto-calculate total when subtotal or tax changes
        subtotalField.textProperty().addListener((obs, old, newVal) -> calculateTotal());
        taxAmountField.textProperty().addListener((obs, old, newVal) -> calculateTotal());

        // Generate invoice number
        invoiceNumberField.setText(invoiceApiService.generateInvoiceNumber());
        issueDatePicker.setValue(LocalDate.now());
        paymentStatusCombo.setValue(PaymentStatus.PENDING);
    }

    public void setInvoice(Invoice invoice) {
        this.currentInvoice = invoice;
        if (invoice != null && !invoice.id().isEmpty()) {
            ticketIdField.setText(invoice.ticketId());
            invoiceNumberField.setText(invoice.invoiceNumber());
            issueDatePicker.setValue(invoice.issueDate());
            dueDatePicker.setValue(invoice.dueDate());
            subtotalField.setText(String.valueOf(invoice.subtotal()));
            taxAmountField.setText(String.valueOf(invoice.taxAmount()));
            totalAmountField.setText(String.valueOf(invoice.totalAmount()));
            paidAmountField.setText(String.valueOf(invoice.paidAmount()));
            paymentStatusCombo.setValue(invoice.paymentStatus());
            paymentMethodCombo.setValue(invoice.paymentMethod());
            paymentDatePicker.setValue(invoice.paymentDate());
            notesArea.setText(invoice.notes());
        }
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    private void calculateTotal() {
        try {
            double subtotal = Double.parseDouble(subtotalField.getText().isEmpty() ? "0" : subtotalField.getText());
            double tax = Double.parseDouble(taxAmountField.getText().isEmpty() ? "0" : taxAmountField.getText());
            totalAmountField.setText(String.format("%.2f", subtotal + tax));
        } catch (NumberFormatException e) {
            // Ignore invalid input
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        try {
            double subtotal = Double.parseDouble(subtotalField.getText());
            double taxAmount = Double.parseDouble(taxAmountField.getText());
            double totalAmount = Double.parseDouble(totalAmountField.getText());
            double paidAmount = Double
                    .parseDouble(paidAmountField.getText().isEmpty() ? "0" : paidAmountField.getText());

            Invoice invoice = new Invoice(
                    currentInvoice != null ? currentInvoice.id() : "",
                    ticketIdField.getText(),
                    invoiceNumberField.getText(),
                    issueDatePicker.getValue(),
                    dueDatePicker.getValue(),
                    subtotal,
                    taxAmount,
                    totalAmount,
                    paidAmount,
                    paymentStatusCombo.getValue(),
                    paymentDatePicker.getValue(),
                    paymentMethodCombo.getValue(),
                    notesArea.getText());

            if (currentInvoice == null || currentInvoice.id().isEmpty()) {
                invoiceApiService.createInvoice(invoice);
            } else {
                invoiceApiService.updateInvoice(currentInvoice.id(), invoice);
            }

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            closeWindow();
        } catch (Exception e) {
            showError("Error al guardar la factura: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private boolean validateInput() {
        if (ticketIdField.getText().trim().isEmpty()) {
            showError("Por favor ingrese el ID del ticket");
            return false;
        }
        if (issueDatePicker.getValue() == null) {
            showError("Por favor seleccione la fecha de emisión");
            return false;
        }
        try {
            Double.parseDouble(subtotalField.getText());
            Double.parseDouble(taxAmountField.getText());
        } catch (NumberFormatException e) {
            showError("Los montos deben ser valores numéricos válidos");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void closeWindow() {
        Stage stage = (Stage) ticketIdField.getScene().getWindow();
        stage.close();
    }
}
