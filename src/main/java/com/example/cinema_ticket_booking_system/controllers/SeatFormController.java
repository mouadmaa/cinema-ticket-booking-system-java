package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.HallModel;
import com.example.cinema_ticket_booking_system.models.SeatModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SeatFormController implements Initializable {

    @FXML
    private Label formTitleLabel;
    
    @FXML
    private ComboBox<HallModel> hallComboBox;
    
    @FXML
    private TextField seatNumberField;
    
    @FXML
    private ComboBox<String> seatTypeComboBox;
    
    @FXML
    private CheckBox availableCheckBox;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    private SeatModel seatForUpdate;
    private Runnable refreshCallback;
    private boolean isUpdateMode = false;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> seatTypes = FXCollections.observableArrayList(
            "Standard", "Premium", "Accessible"
        );
        seatTypeComboBox.setItems(seatTypes);
        seatTypeComboBox.getSelectionModel().selectFirst();

        loadHalls();

        addValidationListeners();
    }
    
    private void loadHalls() {
        ObservableList<HallModel> halls = FXCollections.observableArrayList();
        
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "SELECT id, name, capacity FROM Halls ORDER BY name";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int capacity = resultSet.getInt("capacity");
                    
                    halls.add(new HallModel(id, name, capacity));
                }
                
                hallComboBox.setItems(halls);

                hallComboBox.setCellFactory(param -> new ListCell<>() {
                    @Override
                    protected void updateItem(HallModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                });

                hallComboBox.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(HallModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                });

                if (!halls.isEmpty()) {
                    hallComboBox.getSelectionModel().selectFirst();
                }
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not load halls", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addValidationListeners() {
        seatNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                seatNumberField.setStyle("-fx-border-color: red;");
            } else {
                seatNumberField.setStyle("");
            }
        });

        hallComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                hallComboBox.setStyle("-fx-border-color: red;");
            } else {
                hallComboBox.setStyle("");
            }
        });

        seatTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                seatTypeComboBox.setStyle("-fx-border-color: red;");
            } else {
                seatTypeComboBox.setStyle("");
            }
        });
    }
    
    public void setSeatForUpdate(SeatModel seat) {
        this.seatForUpdate = seat;
        this.isUpdateMode = true;

        formTitleLabel.setText("Update Seat");

        seatNumberField.setText(seat.getSeatNumber());

        for (HallModel hall : hallComboBox.getItems()) {
            if (hall.getId() == seat.getHallId()) {
                hallComboBox.getSelectionModel().select(hall);
                break;
            }
        }

        seatTypeComboBox.getSelectionModel().select(seat.getSeatType());

        availableCheckBox.setSelected(seat.getIsAvailable());
    }
    
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }
    
    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (validateForm()) {
            if (isUpdateMode) {
                updateSeat();
            } else {
                saveSeat();
            }
        }
    }
    
    @FXML
    private void handleCancelButton(ActionEvent event) {
        closeForm();
    }
    
    private boolean validateForm() {
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();

        if (hallComboBox.getValue() == null) {
            hallComboBox.setStyle("-fx-border-color: red;");
            errorMessage.append("Please select a hall.\n");
            isValid = false;
        }

        if (seatNumberField.getText().trim().isEmpty()) {
            seatNumberField.setStyle("-fx-border-color: red;");
            errorMessage.append("Please enter a seat number.\n");
            isValid = false;
        }

        if (seatTypeComboBox.getValue() == null || seatTypeComboBox.getValue().isEmpty()) {
            seatTypeComboBox.setStyle("-fx-border-color: red;");
            errorMessage.append("Please select a seat type.\n");
            isValid = false;
        }
        
        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
        
        return isValid;
    }
    
    private void saveSeat() {
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "INSERT INTO Seats (hall_id, seat_number, seat_type, is_available) VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                HallModel selectedHall = hallComboBox.getValue();
                
                statement.setInt(1, selectedHall.getId());
                statement.setString(2, seatNumberField.getText().trim());
                statement.setString(3, seatTypeComboBox.getValue());
                statement.setBoolean(4, availableCheckBox.isSelected());
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows > 0) {
                    if (refreshCallback != null) {
                        refreshCallback.run();
                    }
                    showSuccessAlert("Seat Added", "The seat has been successfully added.");
                    closeForm();
                } else {
                    showErrorAlert("Error", "Save Failed", "No seat was added.");
                }
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not add the seat", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateSeat() {
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "UPDATE Seats SET hall_id = ?, seat_number = ?, seat_type = ?, is_available = ? WHERE id = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                HallModel selectedHall = hallComboBox.getValue();
                
                statement.setInt(1, selectedHall.getId());
                statement.setString(2, seatNumberField.getText().trim());
                statement.setString(3, seatTypeComboBox.getValue());
                statement.setBoolean(4, availableCheckBox.isSelected());
                statement.setInt(5, seatForUpdate.getId());
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows > 0) {
                    if (refreshCallback != null) {
                        refreshCallback.run();
                    }
                    showSuccessAlert("Seat Updated", "The seat has been successfully updated.");
                    closeForm();
                } else {
                    showErrorAlert("Error", "Update Failed", "No seat was updated. The seat may not exist.");
                }
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not update the seat", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
