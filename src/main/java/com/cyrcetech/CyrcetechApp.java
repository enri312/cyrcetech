package com.cyrcetech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Main JavaFX application class for Cyrcetech.
 * Handles application initialization and scene management.
 */
public class CyrcetechApp extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        
        System.out.println("Starting Cyrcetech application...");
        
        // Load initial view
        Parent root = loadFXML("view/LoginView");
        scene = new Scene(root, 1000, 700);
        
        // Load CSS stylesheet
        URL cssResource = getClass().getResource("styles.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
            System.out.println("CSS loaded successfully");
        } else {
            System.err.println("Warning: styles.css not found");
        }
        
        // Configure stage
        stage.setScene(scene);
        stage.setTitle("Cyrcetech - Gestión de Reparaciones");
        stage.setResizable(true);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        System.out.println("Showing stage...");
        stage.show();
        System.out.println("Application started successfully!");
    }

    /**
     * Changes the current view to a new FXML file
     * @param fxml Path to FXML file (without .fxml extension)
     * @throws IOException if FXML file cannot be loaded
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Loads an FXML file and returns its root node
     * @param fxml Path to FXML file (without .fxml extension)
     * @return Parent node from FXML
     * @throws IOException if FXML file cannot be loaded
     */
    private static Parent loadFXML(String fxml) throws IOException {
        String fxmlPath = fxml + ".fxml";
        URL fxmlResource = CyrcetechApp.class.getResource(fxmlPath);
        
        if (fxmlResource == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }
        
        System.out.println("Loading FXML: " + fxmlPath);
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);
        
        try {
            Parent root = fxmlLoader.load();
            System.out.println("FXML loaded successfully: " + fxmlPath);
            return root;
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Returns the primary stage
     * @return Primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
