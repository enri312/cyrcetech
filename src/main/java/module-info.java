module com.cyrcetech {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires org.apache.pdfbox;
    requires transitive java.sql;
    requires java.desktop;
    requires java.net.http;
    requires com.google.gson;
    requires org.postgresql.jdbc;

    opens com.cyrcetech.app to javafx.fxml;
    opens com.cyrcetech.usecase to javafx.fxml;
    opens com.cyrcetech.infrastructure to javafx.fxml;
    opens com.cyrcetech.interface_adapter.controller to javafx.fxml;
    opens com.cyrcetech.infrastructure.api.dto to com.google.gson;

    exports com.cyrcetech.app;
    exports com.cyrcetech.usecase;
    exports com.cyrcetech.entity;
    exports com.cyrcetech.infrastructure;
    exports com.cyrcetech.interface_adapter.controller;
}
