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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

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

        // Style date column with monospace font
        dateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-text-fill: #808090;");
                }
            }
        });

        userColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        // Style user column - bold white text
        userColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                }
            }
        });

        actionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAction()));

        // Style action column with glass-style badges
        actionColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    // Glass-style badge with border like React version
                    String baseStyle = "-fx-alignment: CENTER; -fx-padding: 4 8; -fx-background-radius: 4; -fx-border-radius: 4; -fx-font-size: 11px;";
                    if (item.contains("LOGIN") || item.contains("LOGOUT") || item.contains("SYSTEM")) {
                        setStyle(baseStyle
                                + "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-width: 1; -fx-text-fill: #a0a0b0;");
                    } else if (item.contains("DELETE")) {
                        setStyle(baseStyle
                                + "-fx-background-color: rgba(248, 113, 113, 0.15); -fx-border-color: rgba(248, 113, 113, 0.3); -fx-border-width: 1; -fx-text-fill: #f87171;");
                    } else if (item.contains("CREATE")) {
                        setStyle(baseStyle
                                + "-fx-background-color: rgba(96, 165, 250, 0.15); -fx-border-color: rgba(96, 165, 250, 0.3); -fx-border-width: 1; -fx-text-fill: #60a5fa;");
                    } else if (item.contains("UPDATE")) {
                        setStyle(baseStyle
                                + "-fx-background-color: rgba(251, 191, 36, 0.15); -fx-border-color: rgba(251, 191, 36, 0.3); -fx-border-width: 1; -fx-text-fill: #fbbf24;");
                    } else if (item.contains("EXPORT")) {
                        setStyle(baseStyle
                                + "-fx-background-color: rgba(167, 139, 250, 0.15); -fx-border-color: rgba(167, 139, 250, 0.3); -fx-border-width: 1; -fx-text-fill: #a78bfa;");
                    } else if (item.contains("VIEW") || item.contains("LIST") || item.contains("SEARCH")) {
                        setStyle(baseStyle
                                + "-fx-background-color: rgba(74, 222, 128, 0.15); -fx-border-color: rgba(74, 222, 128, 0.3); -fx-border-width: 1; -fx-text-fill: #4ade80;");
                    } else {
                        setStyle(baseStyle
                                + "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-width: 1; -fx-text-fill: #808090;");
                    }
                }
            }
        });

        detailsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDetails()));

        // Style details column - light gray text
        detailsColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #b0b0c0;");
                }
            }
        });

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
