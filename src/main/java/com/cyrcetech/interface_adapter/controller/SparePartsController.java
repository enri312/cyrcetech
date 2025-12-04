package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.app.I18nUtil;
import com.cyrcetech.entity.SparePart;
import com.cyrcetech.infrastructure.api.service.SparePartApiService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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

public class SparePartsController {

    private final SparePartApiService sparePartApiService = new SparePartApiService();

    private List<SparePart> allSpareParts;

    @FXML
    private TextField searchField;
    @FXML
    private CheckBox lowStockFilter;

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

        loadSpareParts();
    }

    private void loadSpareParts() {
        try {
            allSpareParts = sparePartApiService.getAllSpareParts();
            applyFilters();
        } catch (Exception e) {
            showError("Error loading spare parts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyFilters() {
        List<SparePart> filtered = allSpareParts;

        // Apply low stock filter
        if (lowStockFilter.isSelected()) {
            filtered = filtered.stream()
                    .filter(SparePart::isLowStock)
                    .collect(Collectors.toList());
        }

        // Apply search filter
        String searchText = searchField != null ? searchField.getText() : "";
        if (!searchText.trim().isEmpty()) {
            String search = searchText.toLowerCase().trim();
            filtered = filtered.stream()
                    .filter(part -> part.name().toLowerCase().contains(search) ||
                            (part.provider() != null && part.provider().toLowerCase().contains(search)))
                    .collect(Collectors.toList());
        }

        sparePartsTable.setItems(FXCollections.observableArrayList(filtered));
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
    private void handleNewSparePart(ActionEvent event) {
        openSparePartForm(null);
    }

    @FXML
    private void handleEditSparePart(ActionEvent event) {
        SparePart selected = sparePartsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openSparePartForm(selected);
        } else {
            showWarning(I18nUtil.getBundle().getString("sparePart.warning.select"));
        }
    }

    @FXML
    private void handleDeleteSparePart(ActionEvent event) {
        SparePart selected = sparePartsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(I18nUtil.getBundle().getString("sparePart.delete.title"));
            alert.setHeaderText(null);
            alert.setContentText(I18nUtil.getBundle().getString("sparePart.delete.confirm"));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    sparePartApiService.deleteSparePart(selected.id());
                    loadSpareParts();
                } catch (Exception e) {
                    showError("Error deleting spare part: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            showWarning(I18nUtil.getBundle().getString("sparePart.warning.select"));
        }
    }

    private void openSparePartForm(SparePart sparePart) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/cyrcetech/app/view/SparePartFormView.fxml"));
            loader.setResources(I18nUtil.getBundle());
            Parent root = loader.load();

            SparePartFormController controller = loader.getController();
            controller.setSparePart(sparePart);
            controller.setOnSaveCallback(this::loadSpareParts);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(sparePart == null ? I18nUtil.getBundle().getString("sparePart.form.title.new")
                    : I18nUtil.getBundle().getString("sparePart.form.title.edit"));
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
