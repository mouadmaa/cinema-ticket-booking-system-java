package com.example.cinema_ticket_booking_system.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import com.example.cinema_ticket_booking_system.MainApplication;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

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
    private final String ACTIVE_BUTTON_STYLE = "-fx-background-color: #1E88E5; -fx-text-fill: white;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Apply drop shadow effect to the content pane
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(3.0);
        shadow.setOffsetX(0.0);
        shadow.setColor(javafx.scene.paint.Color.color(0, 0, 0, 0.3));
        contentPane.setEffect(shadow);
        
        // Set up FontAwesome icons for buttons
        setupButtonIcons();
        
        // Load the user view by default when the dashboard is opened
        try {
            loadUserView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupButtonIcons() {
        // Create a home icon
        FontAwesomeIconView homeIcon = new FontAwesomeIconView(FontAwesomeIcon.HOME);
        homeIcon.setSize("18");
        homeIcon.setFill(Color.WHITE);
        homeButton.setGraphic(homeIcon);
        homeButton.setGraphicTextGap(10);

        // Create a user icon
        FontAwesomeIconView userIcon = new FontAwesomeIconView(FontAwesomeIcon.USERS);
        userIcon.setSize("18");
        userIcon.setFill(Color.WHITE);
        userButton.setGraphic(userIcon);
        userButton.setGraphicTextGap(10);
        
        // Apply styling to buttons
        homeButton.getStyleClass().add("dashboard-button");
        userButton.getStyleClass().add("dashboard-button");
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
        String DEFAULT_BUTTON_STYLE = "-fx-background-color: #263238; -fx-text-fill: white;";
        homeButton.setStyle(DEFAULT_BUTTON_STYLE);
        userButton.setStyle(DEFAULT_BUTTON_STYLE);
    }

    private void setContentPane(AnchorPane view) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(view);
        
        // Apply drop shadow effect to the loaded view
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(2.0);
        shadow.setOffsetX(0.0);
        shadow.setColor(javafx.scene.paint.Color.color(0, 0, 0, 0.2));
        view.setEffect(shadow);
        
        // Make the loaded view fill the entire contentPane
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
    }
}
