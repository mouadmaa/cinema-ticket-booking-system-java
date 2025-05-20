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

        loginButton.setDisable(true);

        Optional<UserModel> authenticatedUser = userService.authenticateUser(email, password);

        if (authenticatedUser.isPresent()) {
            try {
                loadDashboard(authenticatedUser.get());
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error loading dashboard. Please try again.");
            }
        } else {
            showError("Invalid email or password. Please try again.");

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> loginButton.setDisable(false));
            pause.play();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);

        loginButton.setDisable(false);
    }

    private void loadDashboard(UserModel user) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("DashboardView.fxml"));
        Parent dashboardView = loader.load();

        DashboardController dashboardController = loader.getController();
        dashboardController.setCurrentUser(user);

        Stage stage = (Stage) loginButton.getScene().getWindow();

        Scene scene = new Scene(dashboardView, 1200, 700);

        scene.getStylesheets().add(MainApplication.getCssUrl("material-design.css"));

        stage.setScene(scene);
        stage.setTitle("Cinema Ticket Booking Dashboard");
        stage.centerOnScreen();
    }
}
