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

    private Runnable refreshCallback;

    private UserModel userToUpdate;
    private boolean isUpdateMode = false;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleComboBox.setItems(FXCollections.observableArrayList("admin", "front_desk"));

        formTitleLabel.setText("Add User");

        errorLabel.setText("");
    }
    
    /**
     * Set the user for update and populate form fields
     */
    public void setUserForUpdate(UserModel user) {
        this.userToUpdate = user;
        this.isUpdateMode = true;

        formTitleLabel.setText("Update User");

        roleComboBox.setValue(user.getRole());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        phoneNumberField.setText(user.getPhoneNumber());

        passwordField.setPromptText("Leave empty to keep current password");
    }
    
    @FXML
    private void handleSave(ActionEvent event) {
        errorLabel.setText("");

        if (!validateFields()) {
            return;
        }
        
        try (Connection connection = SingletonConnection.getConnection()) {
            if (isUpdateMode) {
                updateUser(connection);
            } else {
                addUser(connection);
            }
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addUser(Connection connection) throws SQLException {
        String sql = "INSERT INTO Users (role, first_name, last_name, username, email, phone_number, password, created_at, updated_at) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roleComboBox.getValue());
            statement.setString(2, firstNameField.getText().trim());
            statement.setString(3, lastNameField.getText().trim());
            statement.setString(4, usernameField.getText().trim());
            statement.setString(5, emailField.getText().trim());
            statement.setString(6, phoneNumberField.getText().trim());
            statement.setString(7, passwordField.getText());

            LocalDateTime now = LocalDateTime.now();
            statement.setTimestamp(8, java.sql.Timestamp.valueOf(now));
            statement.setTimestamp(9, java.sql.Timestamp.valueOf(now));

            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                showInformationAlert("Success", "User Added", "The user has been added successfully.");

                if (refreshCallback != null) {
                    refreshCallback.run();
                }

                closeForm();
            } else {
                errorLabel.setText("Failed to add user. Please try again.");
            }
        }
    }
    
    private void updateUser(Connection connection) throws SQLException {
        String sql;
        if (passwordField.getText().isEmpty()) {
            sql = "UPDATE Users SET role = ?, first_name = ?, last_name = ?, username = ?, " +
                  "email = ?, phone_number = ?, updated_at = ? WHERE id = ?";
        } else {
            sql = "UPDATE Users SET role = ?, first_name = ?, last_name = ?, username = ?, " +
                  "email = ?, phone_number = ?, password = ?, updated_at = ? WHERE id = ?";
        }
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roleComboBox.getValue());
            statement.setString(2, firstNameField.getText().trim());
            statement.setString(3, lastNameField.getText().trim());
            statement.setString(4, usernameField.getText().trim());
            statement.setString(5, emailField.getText().trim());
            statement.setString(6, phoneNumberField.getText().trim());
            
            LocalDateTime now = LocalDateTime.now();
            
            if (passwordField.getText().isEmpty()) {
                statement.setTimestamp(7, java.sql.Timestamp.valueOf(now));
                statement.setInt(8, userToUpdate.getId());
            } else {
                statement.setString(7, passwordField.getText());
                statement.setTimestamp(8, java.sql.Timestamp.valueOf(now));
                statement.setInt(9, userToUpdate.getId());
            }

            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                showInformationAlert("Success", "User Updated", "The user has been updated successfully.");

                if (refreshCallback != null) {
                    refreshCallback.run();
                }

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

        if (!isUpdateMode && passwordField.getText().isEmpty()) {
            errorMessages.append("Password is required.\n");
        } 

        if (!passwordField.getText().isEmpty() && passwordField.getText().length() < 6) {
            errorMessages.append("Password must be at least 6 characters long.\n");
        }
        
        if (errorMessages.length() > 0) {
            errorLabel.setText(errorMessages.toString());
            return false;
        }
        
        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    private void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
