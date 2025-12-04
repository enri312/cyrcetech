package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.app.I18nUtil;
import com.cyrcetech.entity.DeviceType;
import com.cyrcetech.entity.Equipment;
import com.cyrcetech.infrastructure.api.service.EquipmentApiService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EquipmentController {

    private final EquipmentApiService equipmentApiService = new EquipmentApiService();

    private List<Equipment> allEquipment;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<DeviceType> typeFilter;

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

        // Initialize type filter
        typeFilter.setItems(FXCollections.observableArrayList(DeviceType.values()));

        loadEquipment();
    }

    private void loadEquipment() {
        try {
            allEquipment = equipmentApiService.getAllEquipment();
            applyFilters();
        } catch (Exception e) {
            showError("Error loading equipment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyFilters() {
        List<Equipment> filtered = allEquipment;

        // Apply type filter
        if (typeFilter.getValue() != null) {
            filtered = filtered.stream()
                    .filter(eq -> eq.deviceType() == typeFilter.getValue())
                    .collect(Collectors.toList());
        }

        // Apply search filter
        String searchText = searchField != null ? searchField.getText() : "";
        if (!searchText.trim().isEmpty()) {
            String search = searchText.toLowerCase().trim();
            filtered = filtered.stream()
                    .filter(eq -> eq.brand().toLowerCase().contains(search) ||
                            eq.model().toLowerCase().contains(search) ||
                            eq.serialNumber().toLowerCase().contains(search))
                    .collect(Collectors.toList());
        }

        equipmentTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void handleSearch() {
        applyFilters();
    }

    @FXML
    private void handleFilter() {
        applyFilters();
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/MainView");
    }

    @FXML
    private void handleNewEquipment(ActionEvent event) {
        openEquipmentForm(null);
    }

    @FXML
    private void handleEditEquipment(ActionEvent event) {
        Equipment selected = equipmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openEquipmentForm(selected);
        } else {
            showWarning(I18nUtil.getBundle().getString("equipment.warning.select"));
        }
    }

    @FXML
    private void handleDeleteEquipment(ActionEvent event) {
        Equipment selected = equipmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(I18nUtil.getBundle().getString("equipment.delete.title"));
            alert.setHeaderText(null);
            alert.setContentText(I18nUtil.getBundle().getString("equipment.delete.confirm"));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    equipmentApiService.deleteEquipment(selected.id());
                    loadEquipment();
                } catch (Exception e) {
                    showError("Error deleting equipment: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            showWarning(I18nUtil.getBundle().getString("equipment.warning.select"));
        }
    }

    private void openEquipmentForm(Equipment equipment) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/cyrcetech/app/view/EquipmentFormView.fxml"));
            loader.setResources(I18nUtil.getBundle());
            Parent root = loader.load();

            EquipmentFormController controller = loader.getController();
            controller.setEquipment(equipment);
            controller.setOnSaveCallback(this::loadEquipment);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(equipment == null ? I18nUtil.getBundle().getString("equipment.form.title.new")
                    : I18nUtil.getBundle().getString("equipment.form.title.edit"));
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
