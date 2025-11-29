package com.cyrcetech.infrastructure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            String sql = loadSchema();
            // Split by semicolon to execute multiple statements if needed,
            // or just execute if the driver supports it.
            // Postgres JDBC usually supports executing multiple statements in one go if
            // allowed,
            // but splitting is safer for simple scripts.
            for (String statement : sql.split(";")) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }
            System.out.println("Database initialized successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    private static String loadSchema() {
        try (InputStream is = DatabaseInitializer.class.getResourceAsStream("/schema.sql");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Could not load schema.sql", e);
        }
    }
}
