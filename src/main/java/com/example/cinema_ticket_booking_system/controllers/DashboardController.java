package com.example.cinema_ticket_booking_system.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import com.example.cinema_ticket_booking_system.HelloApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Button homeButton;

    @FXML
    private Button adminButton;

    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load home view by default when dashboard is opened
        try {
            loadHomeView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHomeButton() throws IOException {
        loadHomeView();
    }

    @FXML
    private void handleAdminButton() throws IOException {
        loadAdminView();
    }

    private void loadHomeView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/HomeView.fxml"));
        AnchorPane homeView = fxmlLoader.load();
        setContentPane(homeView);
    }

    private void loadAdminView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/AdminView.fxml"));
        AnchorPane adminView = fxmlLoader.load();
        setContentPane(adminView);
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
