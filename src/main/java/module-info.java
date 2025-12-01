module com.cyrcetech {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    // Java modules
    requires java.net.http;
    requires java.sql;
    requires java.desktop;

    // External libraries
    requires com.google.gson;
    requires org.postgresql.jdbc;
    requires org.apache.pdfbox;

    // Open packages for JavaFX reflection (FXML loading)
    opens com.cyrcetech.app to javafx.fxml;
    opens com.cyrcetech.interface_adapter.controller to javafx.fxml;
    opens com.cyrcetech.entity to javafx.base;

    // Export packages for public API
    exports com.cyrcetech.app;
    exports com.cyrcetech.interface_adapter.controller;
    exports com.cyrcetech.entity;
    exports com.cyrcetech.usecase;
    exports com.cyrcetech.usecase.impl;
    exports com.cyrcetech.infrastructure.ai;
}
