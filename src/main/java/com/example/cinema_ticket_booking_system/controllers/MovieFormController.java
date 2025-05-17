package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.MovieModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MovieFormController implements Initializable {
    
    @FXML
    private TextField titleField;
    
    @FXML
    private ComboBox<String> genreComboBox;
    
    @FXML
    private Spinner<Integer> durationSpinner;
    
    @FXML
    private DatePicker releaseDatePicker;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label errorLabel;
    
    private MovieModel movieToUpdate;
    private boolean isUpdateMode = false;
    private Runnable refreshCallback;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configure genre combo box with predefined genres
        genreComboBox.setItems(FXCollections.observableArrayList(
                "Action", "Comedy", "Drama", "Sci-Fi", "Horror", "Romance", "Thriller"
        ));
        
        // Configure duration spinner (30-300 minutes)
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(30, 300, 120, 5);
        durationSpinner.setValueFactory(valueFactory);
        
        // Set current date as default for release date
        releaseDatePicker.setValue(LocalDate.now());
        
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
    
    public void setMovieForUpdate(MovieModel movie) {
        this.movieToUpdate = movie;
        this.isUpdateMode = true;
        
        // Populate form fields with existing movie data
        titleField.setText(movie.getTitle());
        genreComboBox.setValue(movie.getGenre());
        durationSpinner.getValueFactory().setValue(movie.getDuration());
        releaseDatePicker.setValue(movie.getReleaseDate());
        descriptionArea.setText(movie.getDescription());
        
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
        String title = titleField.getText().trim();
        String genre = genreComboBox.getValue();
        int duration = durationSpinner.getValue();
        LocalDate releaseDate = releaseDatePicker.getValue();
        String description = descriptionArea.getText().trim();
        
        try (Connection connection = SingletonConnection.getConnection()) {
            if (isUpdateMode) {
                updateMovie(connection, title, genre, duration, releaseDate, description);
            } else {
                addMovie(connection, title, genre, duration, releaseDate, description);
            }
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addMovie(Connection connection, String title, String genre, int duration, 
                          LocalDate releaseDate, String description) throws SQLException {
        // SQL for inserting a new movie
        String sql = "INSERT INTO Movies (title, genre, duration, release_date, description) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set parameters
            statement.setString(1, title);
            statement.setString(2, genre);
            statement.setInt(3, duration);
            statement.setDate(4, Date.valueOf(releaseDate));
            statement.setString(5, description.isEmpty() ? null : description);
            
            // Execute the query
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                // Movie added successfully
                showInformationAlert("Success", "Movie Added", "The movie has been added successfully.");
                
                // Refresh the movie table in the parent view
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
                
                // Close the form
                closeForm();
            } else {
                errorLabel.setText("Failed to add movie. Please try again.");
            }
        }
    }
    
    private void updateMovie(Connection connection, String title, String genre, int duration,
                             LocalDate releaseDate, String description) throws SQLException {
        // SQL for updating an existing movie
        String sql = "UPDATE Movies SET title = ?, genre = ?, duration = ?, " +
                     "release_date = ?, description = ? WHERE id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set parameters
            statement.setString(1, title);
            statement.setString(2, genre);
            statement.setInt(3, duration);
            statement.setDate(4, Date.valueOf(releaseDate));
            statement.setString(5, description.isEmpty() ? null : description);
            statement.setInt(6, movieToUpdate.getId());
            
            // Execute the query
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                // Movie updated successfully
                showInformationAlert("Success", "Movie Updated", "The movie has been updated successfully.");
                
                // Refresh the movie table in the parent view
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
                
                // Close the form
                closeForm();
            } else {
                errorLabel.setText("Failed to update movie. Please try again.");
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
        
        // Validate title
        if (titleField.getText().trim().isEmpty()) {
            errorLabel.setText("Title is required");
            titleField.requestFocus();
            return false;
        }
        
        // Validate title format based on the regex constraint in the database
        if (!titleField.getText().trim().matches("^[A-Za-z0-9\\s\\-:'']*$")) {
            errorLabel.setText("Title can only contain letters, numbers, spaces, hyphens, colons, and apostrophes");
            titleField.requestFocus();
            return false;
        }
        
        // Validate genre
        if (genreComboBox.getValue() == null) {
            errorLabel.setText("Genre is required");
            genreComboBox.requestFocus();
            return false;
        }
        
        // Validate release date
        if (releaseDatePicker.getValue() == null) {
            errorLabel.setText("Release date is required");
            releaseDatePicker.requestFocus();
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
