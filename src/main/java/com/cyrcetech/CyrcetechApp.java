package com.cyrcetech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CyrcetechApp extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Starting application...");
        scene = new Scene(loadFXML("view/LoginView"), 1000, 700);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Cyrcetech - Gestión de Reparaciones");
        stage.setResizable(true);
        System.out.println("Showing stage...");
        stage.show();
        System.out.println("Stage shown!");
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CyrcetechApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
