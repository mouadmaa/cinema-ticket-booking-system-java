package com.example.cinema_ticket_booking_system.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import com.example.cinema_ticket_booking_system.MainApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Button homeButton;

    @FXML
    private Button userButton;

    @FXML
    private AnchorPane contentPane;

    // Active button style
    private final String ACTIVE_BUTTON_STYLE = "-fx-background-color: #007bff; -fx-text-fill: white;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load the home view by default when the dashboard is opened
        try {
            loadHomeView();
        } catch (IOException e) {
            System.out.println("Error loading home view: " + e.getMessage());
        }
    }

    @FXML
    private void handleHomeButton() throws IOException {
        loadHomeView();
    }

    @FXML
    private void handleUserButton() throws IOException {
        loadUserView();
    }

    private void loadHomeView() throws IOException {
        resetButtonStyles();
        homeButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.getFxmlUrl("HomeView.fxml"));
        AnchorPane homeView = fxmlLoader.load();
        setContentPane(homeView);
    }
    
    private void loadUserView() throws IOException {
        resetButtonStyles();
        userButton.setStyle(ACTIVE_BUTTON_STYLE);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.getFxmlUrl("UserView.fxml"));
        AnchorPane userView = fxmlLoader.load();
        setContentPane(userView);
    }
    
    private void resetButtonStyles() {
        // Reset all buttons to the default style
        String DEFAULT_BUTTON_STYLE = "-fx-background-color: transparent; -fx-text-fill: white;";
        homeButton.setStyle(DEFAULT_BUTTON_STYLE);
        userButton.setStyle(DEFAULT_BUTTON_STYLE);
    }

    private void setContentPane(AnchorPane view) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(view);
        
        // Make the loaded view fill the entire contentPane
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
    }
}
