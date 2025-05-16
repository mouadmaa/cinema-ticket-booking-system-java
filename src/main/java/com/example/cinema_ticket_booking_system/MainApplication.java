package com.example.cinema_ticket_booking_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load FXML from the Java directory instead of resources
        URL fxmlUrl = new File("src/main/java/com/example/cinema_ticket_booking_system/views/Dashboard.fxml").toURI().toURL();
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);

        // Add BootstrapFX stylesheet
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        stage.setTitle("Cinema Ticket Booking Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
    // Helper method to load FXML files from Java folder
    public static URL getFxmlUrl(String fxmlFileName) {
        try {
            return new File("src/main/java/com/example/cinema_ticket_booking_system/views/" + fxmlFileName).toURI().toURL();
        } catch (IOException e) {
            System.out.println("Error loading home view: " + e.getMessage());
            return null;
        }
    }
}
