package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.app.DependencyContainer;
import com.cyrcetech.entity.Customer;
import com.cyrcetech.usecase.CustomerService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.cyrcetech.app.I18nUtil;
import javafx.scene.control.ButtonType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientController {

    @FXML
    private TableView<Customer> clientTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> taxIdColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;

    private final CustomerService customerService = DependencyContainer.getCustomerService();

    private List<Customer> allCustomers;

    @FXML
    private TextField searchField;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        taxIdColumn.setCellValueFactory(new PropertyValueFactory<>("taxId"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadClients();
    }

    private void loadClients() {
        allCustomers = customerService.getAllCustomers();
        updateTableView(allCustomers);
    }

    private void updateTableView(List<Customer> customers) {
        clientTable.setItems(FXCollections.observableArrayList(customers));
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            updateTableView(allCustomers);
            return;
        }

        List<Customer> filtered = allCustomers.stream()
                .filter(customer -> customer.name().toLowerCase().contains(searchText) ||
                        customer.taxId().toLowerCase().contains(searchText) ||
                        customer.phone().toLowerCase().contains(searchText) ||
                        customer.address().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        updateTableView(filtered);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        CyrcetechApp.setRoot("view/MainView");
    }

    @FXML
    private void handleNewClient(ActionEvent event) {
        openClientForm(null);
    }

    @FXML
    private void handleEditClient(ActionEvent event) {
        Customer selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openClientForm(selected);
        } else {
            showWarning(I18nUtil.getBundle().getString("clients.warning.select"));
        }
    }

    @FXML
    private void handleDeleteClient(ActionEvent event) {
        Customer selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(I18nUtil.getBundle().getString("clients.delete.title"));
            alert.setHeaderText(null);
            alert.setContentText(I18nUtil.getBundle().getString("clients.delete.confirm"));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                customerService.deleteCustomer(selected.id());
                loadClients();
            }
        } else {
            showWarning(I18nUtil.getBundle().getString("clients.warning.select"));
        }
    }

    private void openClientForm(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cyrcetech/app/view/ClientFormView.fxml"));
            loader.setResources(I18nUtil.getBundle());
            Parent root = loader.load();

            ClientFormController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setOnSaveCallback(this::loadClients);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(customer == null ? I18nUtil.getBundle().getString("client.form.title.new")
                    : I18nUtil.getBundle().getString("client.form.title.edit"));
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
