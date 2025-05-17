package com.example.cinema_ticket_booking_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Load FXML from the Java directory instead of resources
            URL fxmlUrl = getFxmlUrl("DashboardView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
    
            // Add material design CSS
            scene.getStylesheets().add(getCssUrl("material-design.css"));
            
            // No need for special registration for FontAwesomeFX - it works automatically
            // when included in the classpath and properly imported in FXML files
            
            stage.setTitle("Cinema Ticket Booking Dashboard");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Show error in a more user-friendly way
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Error Starting Application");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch();
    }
    
    // Helper method to load FXML files from Java folder
    public static URL getFxmlUrl(String fxmlFileName) {
        try {
            return new File("src/main/java/com/example/cinema_ticket_booking_system/views/" + fxmlFileName).toURI().toURL();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Helper method to load CSS files
    public static String getCssUrl(String cssFileName) {
        try {
            File cssFile = new File("src/main/resources/com/example/cinema_ticket_booking_system/css/" + cssFileName);
            if (!cssFile.exists()) {
                // Create directory if it doesn't exist
                cssFile.getParentFile().mkdirs();
            }
            return cssFile.toURI().toURL().toExternalForm();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
