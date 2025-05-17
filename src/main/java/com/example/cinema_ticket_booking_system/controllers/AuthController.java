package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.MainApplication;
import com.example.cinema_ticket_booking_system.models.UserModel;
import com.example.cinema_ticket_booking_system.services.AuthService;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class AuthController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    private final AuthService userService = new AuthService();

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password.");
            return;
        }

        // Disable login button to prevent multiple clicks
        loginButton.setDisable(true);

        // Authenticate user
        Optional<UserModel> authenticatedUser = userService.authenticateUser(email, password);

        if (authenticatedUser.isPresent()) {
            // User authenticated successfully
            try {
                loadDashboard(authenticatedUser.get());
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error loading dashboard. Please try again.");
            }
        } else {
            // Authentication failed
            showError("Invalid email or password. Please try again.");
            
            // Re-enable login button after a short delay
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> loginButton.setDisable(false));
            pause.play();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        
        // Re-enable login button
        loginButton.setDisable(false);
    }

    private void loadDashboard(UserModel user) throws IOException {
        // Load the dashboard view
        FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("DashboardView.fxml"));
        Parent dashboardView = loader.load();
        
        // Get the controller and pass the authenticated user
        DashboardController dashboardController = loader.getController();
        dashboardController.setCurrentUser(user);
        
        // Get the current stage from the login button
        Stage stage = (Stage) loginButton.getScene().getWindow();
        
        // Create new scene with dashboard view
        Scene scene = new Scene(dashboardView, 1200, 700);
        
        // Add material design CSS
        scene.getStylesheets().add(MainApplication.getCssUrl("material-design.css"));
        
        // Set the new scene
        stage.setScene(scene);
        stage.setTitle("Cinema Ticket Booking Dashboard");
        stage.centerOnScreen();
    }
}
