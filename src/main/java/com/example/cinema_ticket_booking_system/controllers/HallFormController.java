package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.HallModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HallFormController implements Initializable {
    
    @FXML
    private TextField nameField;
    
    @FXML
    private Spinner<Integer> capacitySpinner;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label errorLabel;
    
    private HallModel hallToUpdate;
    private boolean isUpdateMode = false;
    private Runnable refreshCallback;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 500, 100, 5);
        capacitySpinner.setValueFactory(valueFactory);

        errorLabel.setText("");

        styleButtons();
    }
    
    private void styleButtons() {
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView saveIcon = 
                new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.SAVE);
        saveIcon.setFill(javafx.scene.paint.Color.WHITE);
        saveButton.setGraphic(saveIcon);
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 2;");

        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView cancelIcon = 
                new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TIMES);
        cancelIcon.setFill(javafx.scene.paint.Color.WHITE);
        cancelButton.setGraphic(cancelIcon);
        cancelButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-background-radius: 2;");
    }
    
    public void setHallForUpdate(HallModel hall) {
        this.hallToUpdate = hall;
        this.isUpdateMode = true;

        nameField.setText(hall.getName());
        capacitySpinner.getValueFactory().setValue(hall.getCapacity());

        saveButton.setText("Update");
    }
    
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }
    
    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        String name = nameField.getText().trim();
        int capacity = capacitySpinner.getValue();
        
        try (Connection connection = SingletonConnection.getConnection()) {
            if (isUpdateMode) {
                updateHall(connection, name, capacity);
            } else {
                addHall(connection, name, capacity);
            }
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addHall(Connection connection, String name, int capacity) throws SQLException {
        String sql = "INSERT INTO Halls (name, capacity) VALUES (?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, capacity);

            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                showInformationAlert("Success", "Hall Added", "The hall has been added successfully.");

                if (refreshCallback != null) {
                    refreshCallback.run();
                }

                closeForm();
            } else {
                errorLabel.setText("Failed to add hall. Please try again.");
            }
        }
    }
    
    private void updateHall(Connection connection, String name, int capacity) throws SQLException {
        String sql = "UPDATE Halls SET name = ?, capacity = ? WHERE id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, capacity);
            statement.setInt(3, hallToUpdate.getId());

            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                showInformationAlert("Success", "Hall Updated", "The hall has been updated successfully.");

                if (refreshCallback != null) {
                    refreshCallback.run();
                }

                closeForm();
            } else {
                errorLabel.setText("Failed to update hall. The hall may not exist anymore.");
            }
        }
    }
    
    @FXML
    private void handleCancelButton(ActionEvent event) {
        closeForm();
    }
    
    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private boolean validateInput() {
        errorLabel.setText("");

        if (nameField.getText().trim().isEmpty()) {
            errorLabel.setText("Hall name is required");
            nameField.requestFocus();
            return false;
        }

        if (nameField.getText().trim().length() > 50) {
            errorLabel.setText("Hall name cannot exceed 50 characters");
            nameField.requestFocus();
            return false;
        }

        return true;
    }
    
    private void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView infoIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.INFO_CIRCLE);
        infoIcon.setSize("28");
        infoIcon.setFill(javafx.scene.paint.Color.valueOf("#2196F3"));

        javafx.scene.layout.HBox headerBox = new javafx.scene.layout.HBox(10, infoIcon, new Label(header));
        headerBox.setAlignment(Pos.CENTER_LEFT);

        alert.getDialogPane().setHeader(headerBox);

        alert.getDialogPane().setStyle("-fx-background-color: white;");
        alert.getDialogPane().getStyleClass().add("alert");
        
        alert.showAndWait();
    }
}
