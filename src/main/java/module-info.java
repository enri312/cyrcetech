module com.cyrcetech {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.cyrcetech to javafx.fxml;
    opens com.cyrcetech.controller to javafx.fxml;
    
    exports com.cyrcetech;
    exports com.cyrcetech.controller;
    exports com.cyrcetech.model;
    exports com.cyrcetech.service;
}
