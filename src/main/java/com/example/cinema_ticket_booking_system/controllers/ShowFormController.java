package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.HallModel;
import com.example.cinema_ticket_booking_system.models.MovieModel;
import com.example.cinema_ticket_booking_system.models.ShowModel;
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
    private DatePicker showDatePicker;
    
    @FXML
    private TextField showTimeField;
    
    @FXML
    private TextField ticketPriceField;
    
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
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 100);
        availabilitySpinner.setValueFactory(valueFactory);

        loadMovies();
        loadHalls();

        showDatePicker.setValue(LocalDate.now());

        showTimeField.setText("18:00");

        showTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
                if (!newValue.isEmpty()) {
                    errorLabel.setText("Invalid time format. Use HH:MM (24-hour)");
                }
            } else {
                errorLabel.setText("");
            }
        });

        ticketPriceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                ticketPriceField.setText(oldValue);
            }
        });
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

                    LocalDate releaseDate = null;
                    Date sqlDate = resultSet.getDate("release_date");
                    if (sqlDate != null) {
                        releaseDate = sqlDate.toLocalDate();
                    } else {
                        releaseDate = LocalDate.now();
                    }
                    
                    String description = resultSet.getString("description");
                    if (description == null) {
                        description = "";
                    }

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
        
        if (showDatePicker.getValue() == null) {
            errorLabel.setText("Please select a date.");
            return false;
        }
        
        if (showTimeField.getText().isEmpty() || !showTimeField.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            errorLabel.setText("Please enter a valid time in format HH:MM.");
            return false;
        }
        
        if (ticketPriceField.getText().isEmpty()) {
            errorLabel.setText("Please enter a ticket price.");
            return false;
        }
        
        return true;
    }
    
    private void saveNewShow() {
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "INSERT INTO Shows (movie_id, hall_id, show_date, show_time, ticket_price, availability) VALUES (?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, movieComboBox.getValue().getId());
                statement.setInt(2, hallComboBox.getValue().getId());
                statement.setDate(3, java.sql.Date.valueOf(showDatePicker.getValue()));
                statement.setTime(4, java.sql.Time.valueOf(getSelectedTime()));
                statement.setDouble(5, Double.parseDouble(ticketPriceField.getText()));
                statement.setInt(6, availabilitySpinner.getValue());
                
                int rowsAffected = statement.executeUpdate();
                
                if (rowsAffected > 0) {
                    showInformationAlert("Success", "Show Added", "The show has been added successfully.");

                    if (refreshCallback != null) {
                        refreshCallback.run();
                    }

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
                statement.setInt(1, movieComboBox.getValue().getId());
                statement.setInt(2, hallComboBox.getValue().getId());
                statement.setDate(3, java.sql.Date.valueOf(showDatePicker.getValue()));
                statement.setTime(4, java.sql.Time.valueOf(getSelectedTime()));
                statement.setDouble(5, Double.parseDouble(ticketPriceField.getText()));
                statement.setInt(6, availabilitySpinner.getValue());
                statement.setInt(7, showToUpdate.getId());
                
                int rowsAffected = statement.executeUpdate();
                
                if (rowsAffected > 0) {
                    showInformationAlert("Success", "Show Updated", "The show has been updated successfully.");

                    if (refreshCallback != null) {
                        refreshCallback.run();
                    }

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
        try {
            String[] timeComponents = showTimeField.getText().split(":");
            int hour = Integer.parseInt(timeComponents[0]);
            int minute = Integer.parseInt(timeComponents[1]);
            return LocalTime.of(hour, minute);
        } catch (Exception e) {
            return LocalTime.of(18, 0);
        }
    }
    
    public void setShowForUpdate(ShowModel show) {
        this.showToUpdate = show;

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

        showDatePicker.setValue(show.getShowDate());

        LocalTime time = show.getShowTime();
        showTimeField.setText(String.format("%02d:%02d", time.getHour(), time.getMinute()));

        ticketPriceField.setText(String.valueOf(show.getTicketPrice()));

        try {
            availabilitySpinner.getValueFactory().setValue(show.getAvailability());
        } catch (Exception e) {
            availabilitySpinner.getValueFactory().setValue(100);
        }

        saveButton.setText("Update");
    }
    
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }
    
    private void closeForm() {
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
