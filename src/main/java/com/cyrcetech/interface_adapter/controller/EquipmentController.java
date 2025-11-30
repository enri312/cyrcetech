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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.cyrcetech.app.I18nUtil;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import com.cyrcetech.app.DependencyContainer;
import com.cyrcetech.usecase.EquipmentService;
import javafx.collections.FXCollections;

public class EquipmentController {

    private final EquipmentService equipmentService = DependencyContainer.getEquipmentService();

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

        loadEquipment();
    }

    private void loadEquipment() {
        equipmentTable.setItems(FXCollections.observableArrayList(equipmentService.getAllEquipment()));
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
                equipmentService.deleteEquipment(selected.id());
                loadEquipment();
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
}
