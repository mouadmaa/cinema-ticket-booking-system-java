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
        // Configure capacity spinner (10-500 seats as per DB constraint)
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 500, 100, 5);
        capacitySpinner.setValueFactory(valueFactory);
        
        // Clear any error message
        errorLabel.setText("");
        
        // Style the buttons
        styleButtons();
    }
    
    private void styleButtons() {
        // Style the save button
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView saveIcon = 
                new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.SAVE);
        saveIcon.setFill(javafx.scene.paint.Color.WHITE);
        saveButton.setGraphic(saveIcon);
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 2;");
        
        // Style the cancel button
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView cancelIcon = 
                new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TIMES);
        cancelIcon.setFill(javafx.scene.paint.Color.WHITE);
        cancelButton.setGraphic(cancelIcon);
        cancelButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-background-radius: 2;");
    }
    
    public void setHallForUpdate(HallModel hall) {
        this.hallToUpdate = hall;
        this.isUpdateMode = true;
        
        // Populate form fields with existing hall data
        nameField.setText(hall.getName());
        capacitySpinner.getValueFactory().setValue(hall.getCapacity());
        
        // Update button text to reflect update operation
        saveButton.setText("Update");
    }
    
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }
    
    @FXML
    private void handleSaveButton(ActionEvent event) {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        // Get data from form fields
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
        // SQL for inserting a new hall
        String sql = "INSERT INTO Halls (name, capacity) VALUES (?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set parameters
            statement.setString(1, name);
            statement.setInt(2, capacity);
            
            // Execute the query
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                // Hall added successfully
                showInformationAlert("Success", "Hall Added", "The hall has been added successfully.");
                
                // Refresh the hall table in the parent view
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
                
                // Close the form
                closeForm();
            } else {
                errorLabel.setText("Failed to add hall. Please try again.");
            }
        }
    }
    
    private void updateHall(Connection connection, String name, int capacity) throws SQLException {
        // SQL for updating an existing hall
        String sql = "UPDATE Halls SET name = ?, capacity = ? WHERE id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set parameters
            statement.setString(1, name);
            statement.setInt(2, capacity);
            statement.setInt(3, hallToUpdate.getId());
            
            // Execute the query
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                // Hall updated successfully
                showInformationAlert("Success", "Hall Updated", "The hall has been updated successfully.");
                
                // Refresh the hall table in the parent view
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
                
                // Close the form
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
        // Reset error message
        errorLabel.setText("");
        
        // Validate hall name
        if (nameField.getText().trim().isEmpty()) {
            errorLabel.setText("Hall name is required");
            nameField.requestFocus();
            return false;
        }
        
        // Validate hall name length
        if (nameField.getText().trim().length() > 50) {
            errorLabel.setText("Hall name cannot exceed 50 characters");
            nameField.requestFocus();
            return false;
        }
        
        // All validations passed
        return true;
    }
    
    private void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        // Create custom header with icon
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView infoIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.INFO_CIRCLE);
        infoIcon.setSize("28");
        infoIcon.setFill(javafx.scene.paint.Color.valueOf("#2196F3"));
        
        // Set header with icon and text
        javafx.scene.layout.HBox headerBox = new javafx.scene.layout.HBox(10, infoIcon, new Label(header));
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        // Set dialog header content
        alert.getDialogPane().setHeader(headerBox);
        
        // Apply some styling
        alert.getDialogPane().setStyle("-fx-background-color: white;");
        alert.getDialogPane().getStyleClass().add("alert");
        
        alert.showAndWait();
    }
}
