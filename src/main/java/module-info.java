module com.cyrcetech {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    
    // Java base module
    requires java.base;

    // Open packages for JavaFX reflection (FXML loading)
    opens com.cyrcetech to javafx.fxml;
    opens com.cyrcetech.controller to javafx.fxml;
    opens com.cyrcetech.model to javafx.base;
    
    // Export packages for public API
    exports com.cyrcetech;
    exports com.cyrcetech.controller;
    exports com.cyrcetech.model;
    exports com.cyrcetech.service;
}
