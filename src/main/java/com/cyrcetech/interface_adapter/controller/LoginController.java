package com.cyrcetech.interface_adapter.controller;

import com.cyrcetech.app.CyrcetechApp;
import com.cyrcetech.app.I18nUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label languageLabel;

    @FXML
    private Label eyeIcon;

    private boolean passwordVisible = false;

    @FXML
    public void initialize() {
        // Update language label to show current language
        updateLanguageLabel();

        // Bind password fields to keep them synchronized
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());

        // Initial state: password field visible, text field hidden
        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);
        passwordField.setManaged(true);
        passwordField.setVisible(true);
        if (eyeIcon != null) {
            eyeIcon.setText("🔒"); // Lock icon for hidden
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("CENV".equals(username) && "8994C".equals(password)) {
            CyrcetechApp.setRoot("view/MainView");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(I18nUtil.getBundle().getString("login.error.title"));
            alert.setHeaderText(null);
            alert.setContentText(I18nUtil.getBundle().getString("login.error.message"));
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLanguageSwitch(ActionEvent event) throws IOException {
        // Toggle between EN and ES
        if (I18nUtil.getCurrentLocale().getLanguage().equals("es")) {
            I18nUtil.switchToEnglish();
        } else {
            I18nUtil.switchToSpanish();
        }

        // Reload the login view with the new language
        CyrcetechApp.setRoot("view/LoginView");
    }

    @FXML
    private void handleTogglePassword(ActionEvent event) {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            // Show password
            passwordTextField.setManaged(true);
            passwordTextField.setVisible(true);
            passwordField.setManaged(false);
            passwordField.setVisible(false);
            eyeIcon.setText("👁️"); // Eye open (not crossed)
        } else {
            // Hide password
            passwordField.setManaged(true);
            passwordField.setVisible(true);
            passwordTextField.setManaged(false);
            passwordTextField.setVisible(false);
            eyeIcon.setText("🔒"); // Lock icon for hidden
        }
    }

    private void updateLanguageLabel() {
        if (languageLabel != null) {
            languageLabel.setText(I18nUtil.getCurrentLanguageCode());
        }
    }
}
