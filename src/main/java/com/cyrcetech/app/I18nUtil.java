package com.cyrcetech.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility class for internationalization (i18n) support.
 * Manages locale switching and resource bundle loading.
 */
public class I18nUtil {

    private static Locale currentLocale = Locale.of("es"); // Default to Spanish
    private static final String BUNDLE_BASE_NAME = "messages";

    /**
     * Get the current locale.
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Set the current locale.
     */
    public static void setLocale(Locale locale) {
        currentLocale = locale;
    }

    /**
     * Get the resource bundle for the current locale.
     */
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle(BUNDLE_BASE_NAME, currentLocale);
    }

    /**
     * Load an FXML file with the current resource bundle.
     */
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                I18nUtil.class.getResource(fxml + ".fxml"),
                getBundle());
        return fxmlLoader.load();
    }

    /**
     * Switch to English locale.
     */
    public static void switchToEnglish() {
        setLocale(Locale.of("en"));
    }

    /**
     * Switch to Spanish locale.
     */
    public static void switchToSpanish() {
        setLocale(Locale.of("es"));
    }

    /**
     * Get the current language code (e.g., "EN", "ES").
     */
    public static String getCurrentLanguageCode() {
        return currentLocale.getLanguage().toUpperCase();
    }
}
