package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.HallModel;
import com.example.cinema_ticket_booking_system.models.MovieModel;
import com.example.cinema_ticket_booking_system.models.ShowModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ShowFormController implements Initializable {

    @FXML
    private ComboBox<MovieModel> movieComboBox;
    
    @FXML
    private ComboBox<HallModel> hallComboBox;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private ComboBox<String> hourComboBox;
    
    @FXML
    private ComboBox<String> minuteComboBox;
    
    @FXML
    private TextField priceField;
    
    @FXML
    private Spinner<Integer> availabilitySpinner;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label errorLabel;
    
    private ShowModel showToUpdate;
    private Runnable refreshCallback;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configure time selection comboboxes
        setupTimeComboBoxes();
        
        // Configure availability spinner
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 100);
        availabilitySpinner.setValueFactory(valueFactory);
        
        // Load movies and halls into combo boxes
        loadMovies();
        loadHalls();
        
        // Set default date to today
        datePicker.setValue(LocalDate.now());
        
        // Add validation for price field (only allow numbers and decimal point)
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                priceField.setText(oldValue);
            }
        });
    }
    
    private void setupTimeComboBoxes() {
        // Setup hours (00-23)
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
        }
        hourComboBox.setItems(hours);
        hourComboBox.setValue("18"); // Default to 6 PM
        
        // Setup minutes (00, 15, 30, 45)
        ObservableList<String> minutes = FXCollections.observableArrayList("00", "15", "30", "45");
        minuteComboBox.setItems(minutes);
        minuteComboBox.setValue("00"); // Default to xx:00
    }
    
    private void loadMovies() {
        ObservableList<MovieModel> movies = FXCollections.observableArrayList();
        
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "SELECT id, title, genre, duration, release_date, description FROM Movies ORDER BY title";
            
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    int duration = resultSet.getInt("duration");
                    
                    // Get release date (handle potential null)
                    LocalDate releaseDate = null;
                    Date sqlDate = resultSet.getDate("release_date");
                    if (sqlDate != null) {
                        releaseDate = sqlDate.toLocalDate();
                    } else {
                        releaseDate = LocalDate.now(); // Default to current date if null
                    }
                    
                    String description = resultSet.getString("description");
                    if (description == null) {
                        description = "";
                    }
                    
                    // Create movie with parameters in the correct order
                    MovieModel movie = new MovieModel(id, title, genre, duration, releaseDate, description);
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not load movies", e.getMessage());
            e.printStackTrace();
        }
        
        movieComboBox.setItems(movies);
    }
    
    private void loadHalls() {
        ObservableList<HallModel> halls = FXCollections.observableArrayList();
        
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "SELECT id, name, capacity FROM Halls ORDER BY name";
            
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int capacity = resultSet.getInt("capacity");
                    
                    halls.add(new HallModel(id, name, capacity));
                }
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not load halls", e.getMessage());
            e.printStackTrace();
        }
        
        hallComboBox.setItems(halls);
    }
    
    @FXML
    private void handleSaveButton() {
        if (validateForm()) {
            if (showToUpdate == null) {
                saveNewShow();
            } else {
                updateShow();
            }
        }
    }
    
    @FXML
    private void handleCancelButton() {
        closeForm();
    }
    
    private boolean validateForm() {
        if (movieComboBox.getValue() == null) {
            errorLabel.setText("Please select a movie.");
            return false;
        }
        
        if (hallComboBox.getValue() == null) {
            errorLabel.setText("Please select a hall.");
            return false;
        }
        
        if (datePicker.getValue() == null) {
            errorLabel.setText("Please select a date.");
            return false;
        }
        
        if (hourComboBox.getValue() == null || minuteComboBox.getValue() == null) {
            errorLabel.setText("Please select a valid time.");
            return false;
        }
        
        if (priceField.getText().isEmpty()) {
            errorLabel.setText("Please enter a ticket price.");
            return false;
        }
        
        return true;
    }
    
    private void saveNewShow() {
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "INSERT INTO Shows (movie_id, hall_id, show_date, show_time, ticket_price, availability) VALUES (?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Set parameters
                statement.setInt(1, movieComboBox.getValue().getId());
                statement.setInt(2, hallComboBox.getValue().getId());
                statement.setDate(3, java.sql.Date.valueOf(datePicker.getValue()));
                statement.setTime(4, java.sql.Time.valueOf(getSelectedTime()));
                statement.setDouble(5, Double.parseDouble(priceField.getText()));
                statement.setInt(6, availabilitySpinner.getValue());
                
                int rowsAffected = statement.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Show success message
                    showInformationAlert("Success", "Show Added", "The show has been added successfully.");
                    
                    // Call the refresh callback to update the shows list
                    if (refreshCallback != null) {
                        refreshCallback.run();
                    }
                    
                    // Close the form
                    closeForm();
                } else {
                    errorLabel.setText("Failed to add show. Please try again.");
                }
            }
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateShow() {
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "UPDATE Shows SET movie_id = ?, hall_id = ?, show_date = ?, show_time = ?, ticket_price = ?, availability = ? WHERE id = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Set parameters
                statement.setInt(1, movieComboBox.getValue().getId());
                statement.setInt(2, hallComboBox.getValue().getId());
                statement.setDate(3, java.sql.Date.valueOf(datePicker.getValue()));
                statement.setTime(4, java.sql.Time.valueOf(getSelectedTime()));
                statement.setDouble(5, Double.parseDouble(priceField.getText()));
                statement.setInt(6, availabilitySpinner.getValue());
                statement.setInt(7, showToUpdate.getId());
                
                int rowsAffected = statement.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Show success message
                    showInformationAlert("Success", "Show Updated", "The show has been updated successfully.");
                    
                    // Call the refresh callback to update the shows list
                    if (refreshCallback != null) {
                        refreshCallback.run();
                    }
                    
                    // Close the form
                    closeForm();
                } else {
                    errorLabel.setText("Failed to update show. Please try again.");
                }
            }
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private LocalTime getSelectedTime() {
        int hour = Integer.parseInt(hourComboBox.getValue());
        int minute = Integer.parseInt(minuteComboBox.getValue());
        return LocalTime.of(hour, minute);
    }
    
    public void setShowForUpdate(ShowModel show) {
        this.showToUpdate = show;
        
        // Populate the form with the show's data
        for (MovieModel movie : movieComboBox.getItems()) {
            if (movie.getId() == show.getMovieId()) {
                movieComboBox.setValue(movie);
                break;
            }
        }
        
        for (HallModel hall : hallComboBox.getItems()) {
            if (hall.getId() == show.getHallId()) {
                hallComboBox.setValue(hall);
                break;
            }
        }
        
        // Set the date (already a LocalDate, no conversion needed)
        datePicker.setValue(show.getShowDate());
        
        // Set the time (already a LocalTime, no conversion needed)
        LocalTime time = show.getShowTime();
        hourComboBox.setValue(String.format("%02d", time.getHour()));
        minuteComboBox.setValue(String.format("%02d", time.getMinute()));
        
        // Set the price
        priceField.setText(String.valueOf(show.getTicketPrice()));
        
        // Set availability using the property added to ShowModel
        try {
            availabilitySpinner.getValueFactory().setValue(show.getAvailability());
        } catch (Exception e) {
            // If there's any issue, use default value
            availabilitySpinner.getValueFactory().setValue(100);
        }
        
        // Update title
        saveButton.setText("Update");
    }
    
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }
    
    private void closeForm() {
        // Get the current window
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
