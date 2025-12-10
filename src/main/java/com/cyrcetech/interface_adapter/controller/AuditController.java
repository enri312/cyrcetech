package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.infrastructure.api.dto.AuditLogDTO;
import com.cyrcetech.infrastructure.api.service.AuditApiService;
import com.cyrcetech.infrastructure.session.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for the Audit View - Admin only
 */
public class AuditController {

    @FXML
    private TableView<AuditLogDTO> auditTable;

    @FXML
    private TableColumn<AuditLogDTO, String> dateColumn;

    @FXML
    private TableColumn<AuditLogDTO, String> userColumn;

    @FXML
    private TableColumn<AuditLogDTO, String> actionColumn;

    @FXML
    private TableColumn<AuditLogDTO, String> detailsColumn;

    @FXML
    private Label statusLabel;

    @FXML
    private Label userInfoLabel;

    private final AuditApiService auditApiService = new AuditApiService();
    private final ObservableList<AuditLogDTO> auditLogs = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        // Set user info
        String fullName = SessionManager.getInstance().getFullName();
        String role = SessionManager.getInstance().getRole();
        if (userInfoLabel != null && fullName != null) {
            userInfoLabel.setText(fullName + " (" + formatRole(role) + ")");
        }

        // Configure table columns
        dateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getTimestamp() != null) {
                return new SimpleStringProperty(cellData.getValue().getTimestamp().format(DATE_FORMATTER));
            }
            return new SimpleStringProperty("");
        });

        userColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        actionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAction()));

        // Style action column with colored badges
        actionColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    // Style based on action type
                    String baseStyle = "-fx-alignment: CENTER; -fx-padding: 5 10; -fx-background-radius: 5;";
                    if (item.contains("LOGIN") || item.contains("LOGOUT")) {
                        setStyle(baseStyle + "-fx-background-color: #2d4a3e; -fx-text-fill: #4ade80;");
                    } else if (item.contains("DELETE")) {
                        setStyle(baseStyle + "-fx-background-color: #4a2d2d; -fx-text-fill: #f87171;");
                    } else if (item.contains("CREATE")) {
                        setStyle(baseStyle + "-fx-background-color: #2d3a4a; -fx-text-fill: #60a5fa;");
                    } else if (item.contains("UPDATE")) {
                        setStyle(baseStyle + "-fx-background-color: #4a3d2d; -fx-text-fill: #fbbf24;");
                    } else if (item.contains("EXPORT")) {
                        setStyle(baseStyle + "-fx-background-color: #3d2d4a; -fx-text-fill: #a78bfa;");
                    } else {
                        setStyle(baseStyle + "-fx-background-color: #2d2d3a; -fx-text-fill: #a0a0b0;");
                    }
                }
            }
        });

        detailsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDetails()));

        auditTable.setItems(auditLogs);

        // Load data
        loadAuditLogs();
    }

    private void loadAuditLogs() {
        statusLabel.setText("Cargando registros...");
        try {
            List<AuditLogDTO> logs = auditApiService.getAllLogs();
            auditLogs.clear();
            auditLogs.addAll(logs);
            statusLabel.setText("Mostrando " + logs.size() + " registros");
        } catch (Exception e) {
            statusLabel.setText("Error al cargar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String formatRole(String role) {
        if (role == null)
            return "";
        return switch (role) {
            case "ROLE_ADMIN" -> "Administrador";
            case "ROLE_TECHNICIAN" -> "Técnico";
            case "ROLE_USER" -> "Usuario";
            default -> role;
        };
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/MainView");
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadAuditLogs();
    }
}
