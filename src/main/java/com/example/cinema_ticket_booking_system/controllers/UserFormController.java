package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.UserModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class UserFormController implements Initializable {

    @FXML
    protected Label formTitleLabel;
    
    @FXML
    private ComboBox<String> roleComboBox;
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneNumberField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label errorLabel;
    
    // Callback interface to refresh the user table after adding/updating a user
    private Runnable refreshCallback;
    
    // For update mode, store the user being updated
    private UserModel userToUpdate;
    private boolean isUpdateMode = false;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the role combo box
        roleComboBox.setItems(FXCollections.observableArrayList("admin", "front_desk"));
        
        // Set default title for Add mode
        formTitleLabel.setText("Add New User");
        
        // Clear any error messages
        errorLabel.setText("");
    }
    
    /**
     * Set the user for update and populate form fields
     */
    public void setUserForUpdate(UserModel user) {
        this.userToUpdate = user;
        this.isUpdateMode = true;
        
        // Update the form title
        formTitleLabel.setText("Update User");
        
        // Populate form fields with user data
        roleComboBox.setValue(user.getRole());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        phoneNumberField.setText(user.getPhoneNumber());
        
        // For password, we don't populate it for security reasons
        // We'll handle this specially in the save method
        passwordField.setPromptText("Leave empty to keep current password");
    }
    
    @FXML
    private void handleSave(ActionEvent event) {
        // Clear previous error message
        errorLabel.setText("");
        
        // Validate input fields
        if (!validateFields()) {
            return;
        }
        
        try (Connection connection = SingletonConnection.getConnection()) {
            if (isUpdateMode) {
                updateUser(connection);
            } else {
                addNewUser(connection);
            }
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addNewUser(Connection connection) throws SQLException {
        // SQL for inserting a new user
        String sql = "INSERT INTO Users (role, first_name, last_name, username, email, phone_number, password, created_at, updated_at) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set parameters
            statement.setString(1, roleComboBox.getValue());
            statement.setString(2, firstNameField.getText().trim());
            statement.setString(3, lastNameField.getText().trim());
            statement.setString(4, usernameField.getText().trim());
            statement.setString(5, emailField.getText().trim());
            statement.setString(6, phoneNumberField.getText().trim());
            statement.setString(7, passwordField.getText()); // In a real app, hash this password
            
            // Set current timestamp for created_at and updated_at
            LocalDateTime now = LocalDateTime.now();
            statement.setTimestamp(8, java.sql.Timestamp.valueOf(now));
            statement.setTimestamp(9, java.sql.Timestamp.valueOf(now));
            
            // Execute the query
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                // User added successfully
                showInformationAlert("Success", "User Added", "The user has been added successfully.");
                
                // Refresh the user table in the parent view
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
                
                // Close the form
                closeForm();
            } else {
                errorLabel.setText("Failed to add user. Please try again.");
            }
        }
    }
    
    private void updateUser(Connection connection) throws SQLException {
        // SQL for updating a user, with or without password change
        String sql;
        if (passwordField.getText().isEmpty()) {
            // Don't update password if field is empty
            sql = "UPDATE Users SET role = ?, first_name = ?, last_name = ?, username = ?, " +
                  "email = ?, phone_number = ?, updated_at = ? WHERE id = ?";
        } else {
            // Include password in the update
            sql = "UPDATE Users SET role = ?, first_name = ?, last_name = ?, username = ?, " +
                  "email = ?, phone_number = ?, password = ?, updated_at = ? WHERE id = ?";
        }
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set common parameters
            statement.setString(1, roleComboBox.getValue());
            statement.setString(2, firstNameField.getText().trim());
            statement.setString(3, lastNameField.getText().trim());
            statement.setString(4, usernameField.getText().trim());
            statement.setString(5, emailField.getText().trim());
            statement.setString(6, phoneNumberField.getText().trim());
            
            LocalDateTime now = LocalDateTime.now();
            
            if (passwordField.getText().isEmpty()) {
                // Without password change
                statement.setTimestamp(7, java.sql.Timestamp.valueOf(now));
                statement.setInt(8, userToUpdate.getId());
            } else {
                // With password change
                statement.setString(7, passwordField.getText()); // In a real app, hash this password
                statement.setTimestamp(8, java.sql.Timestamp.valueOf(now));
                statement.setInt(9, userToUpdate.getId());
            }
            
            // Execute the query
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                // User updated successfully
                showInformationAlert("Success", "User Updated", "The user has been updated successfully.");
                
                // Refresh the user table in the parent view
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
                
                // Close the form
                closeForm();
            } else {
                errorLabel.setText("Failed to update user. The user may not exist anymore.");
            }
        }
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        closeForm();
    }
    
    // Validate all input fields
    private boolean validateFields() {
        StringBuilder errorMessages = new StringBuilder();
        
        if (roleComboBox.getValue() == null || roleComboBox.getValue().isEmpty()) {
            errorMessages.append("Please select a role.\n");
        }
        
        if (firstNameField.getText().trim().isEmpty()) {
            errorMessages.append("First name is required.\n");
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            errorMessages.append("Last name is required.\n");
        }
        
        if (usernameField.getText().trim().isEmpty()) {
            errorMessages.append("Username is required.\n");
        }
        
        if (emailField.getText().trim().isEmpty()) {
            errorMessages.append("Email is required.\n");
        } else if (!isValidEmail(emailField.getText().trim())) {
            errorMessages.append("Please enter a valid email address.\n");
        }
        
        // For new users, password is required. For updates, it's optional
        if (!isUpdateMode && passwordField.getText().isEmpty()) {
            errorMessages.append("Password is required.\n");
        } 
        
        // If password is provided, check length
        if (!passwordField.getText().isEmpty() && passwordField.getText().length() < 6) {
            errorMessages.append("Password must be at least 6 characters long.\n");
        }
        
        if (errorMessages.length() > 0) {
            errorLabel.setText(errorMessages.toString());
            return false;
        }
        
        return true;
    }
    
    // Basic email validation
    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    
    // Close the form window
    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    // Method to set the callback for refreshing the user table
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }
    
    // Show information alert
    private void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
