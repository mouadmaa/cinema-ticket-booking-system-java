package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.MainApplication;
import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.HallModel;
import com.example.cinema_ticket_booking_system.models.SeatModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SeatController implements Initializable {

    @FXML
    private TableView<SeatModel> seatTableView;
    
    @FXML
    private TableColumn<SeatModel, Integer> idColumn;
    
    @FXML
    private TableColumn<SeatModel, String> hallNameColumn;
    
    @FXML
    private TableColumn<SeatModel, String> seatNumberColumn;
    
    @FXML
    private TableColumn<SeatModel, String> seatTypeColumn;
    
    @FXML
    private TableColumn<SeatModel, Boolean> availabilityColumn;
    
    @FXML
    private TableColumn<SeatModel, Void> actionsColumn;
    
    @FXML
    private ComboBox<HallModel> hallFilterComboBox;
    
    @FXML
    private Button addSeatButton;
    
    private ObservableList<SeatModel> allSeats = FXCollections.observableArrayList();
    private ObservableList<HallModel> halls = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup table columns
        setupTableColumns();
        
        // Load hall data for the filter combobox
        loadHalls();
        
        // Add listener to the hall filter combobox
        hallFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filterSeatsByHall(newVal.getId());
            } else {
                // If no hall is selected, show all seats
                seatTableView.setItems(allSeats);
            }
        });
        
        // Load all seats by default
        loadSeats();
    }
    
    private void setupTableColumns() {
        // Configure the table columns using property value factories
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        hallNameColumn.setCellValueFactory(cellData -> cellData.getValue().hallNameProperty());
        seatNumberColumn.setCellValueFactory(cellData -> cellData.getValue().seatNumberProperty());
        seatTypeColumn.setCellValueFactory(cellData -> cellData.getValue().seatTypeProperty());
        availabilityColumn.setCellValueFactory(cellData -> cellData.getValue().isAvailableProperty());
        
        // Set column proportional widths
        idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        hallNameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 20% width
        seatNumberColumn.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 20% width
        seatTypeColumn.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 20% width
        availabilityColumn.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 15% width
        actionsColumn.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 15% width
        
        // Format availability column to display checkmarks
        availabilityColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(null);
                    de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView icon;
                    if (item) {
                        icon = new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(
                            de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.CHECK_CIRCLE);
                        icon.setFill(javafx.scene.paint.Color.valueOf("#4CAF50"));
                    } else {
                        icon = new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(
                            de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TIMES_CIRCLE);
                        icon.setFill(javafx.scene.paint.Color.valueOf("#F44336"));
                    }
                    icon.setSize("16");
                    setGraphic(icon);
                    setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });
        
        // Configure actions column with edit and delete buttons
        setupActionsColumn();
        
        // Set alignment for availability column to center
        availabilityColumn.setStyle("-fx-alignment: CENTER;");
    }
    
    private void loadHalls() {
        halls.clear();
        
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "SELECT id, name, capacity FROM Halls ORDER BY name";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                
                // Add a "All Halls" option
                halls.add(null);
                
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int capacity = resultSet.getInt("capacity");
                    
                    halls.add(new HallModel(id, name, capacity));
                }
                
                hallFilterComboBox.setItems(halls);
                
                // Set a custom cell factory to display hall names
                hallFilterComboBox.setCellFactory(param -> new ListCell<>() {
                    @Override
                    protected void updateItem(HallModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("All Halls");
                        } else {
                            setText(item.getName());
                        }
                    }
                });
                
                // Set the same approach for the button cell
                hallFilterComboBox.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(HallModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("All Halls");
                        } else {
                            setText(item.getName());
                        }
                    }
                });
                
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not load halls", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadSeats() {
        allSeats.clear();
        
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "SELECT s.id, s.hall_id, s.seat_number, s.seat_type, s.is_available, h.name AS hall_name " +
                           "FROM Seats s " +
                           "JOIN Halls h ON s.hall_id = h.id " +
                           "ORDER BY h.name, s.seat_number";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int hallId = resultSet.getInt("hall_id");
                    String seatNumber = resultSet.getString("seat_number");
                    String seatType = resultSet.getString("seat_type");
                    boolean isAvailable = resultSet.getBoolean("is_available");
                    String hallName = resultSet.getString("hall_name");
                    
                    SeatModel seat = new SeatModel(id, hallId, seatNumber, seatType, isAvailable, hallName);
                    allSeats.add(seat);
                }
                
                seatTableView.setItems(allSeats);
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not load seats", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void filterSeatsByHall(int hallId) {
        ObservableList<SeatModel> filteredSeats = FXCollections.observableArrayList();
        
        for (SeatModel seat : allSeats) {
            if (seat.getHallId() == hallId) {
                filteredSeats.add(seat);
            }
        }
        
        seatTableView.setItems(filteredSeats);
    }
    
    @FXML
    private void handleAddSeatButton(ActionEvent event) {
        openSeatForm(null); // Pass null for adding a new seat
    }
    
    private void openSeatForm(SeatModel seat) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("SeatFormView.fxml"));
            Parent root = loader.load();
            
            SeatFormController controller = loader.getController();
            controller.setRefreshCallback(this::loadSeats);
            
            // If updating, set the seat to be updated
            if (seat != null) {
                controller.setSeatForUpdate(seat);
            }
            
            // Create a new stage for the dialog
            Stage stage = new Stage();
            stage.setTitle(seat == null ? "Add Seat" : "Update Seat");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            
            // Show the dialog and wait for it to close
            stage.showAndWait();
            
        } catch (IOException e) {
            showErrorAlert("Error", "Could not open the Seat Form", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateBtn = new Button();
            private final Button deleteBtn = new Button();
            private final HBox pane = new HBox(8, updateBtn, deleteBtn);
            
            {
                // Create and style the update button with icon only
                de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView editIcon = 
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.EDIT);
                editIcon.setSize("12");
                editIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                updateBtn.setGraphic(editIcon);
                updateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 2; -fx-min-width: 24px; -fx-min-height: 24px; -fx-max-width: 24px; -fx-max-height: 24px; -fx-padding: 2px;");
                updateBtn.setTooltip(new javafx.scene.control.Tooltip("Edit seat"));
                
                // Create and style the delete button with icon only
                de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView deleteIcon = 
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TRASH);
                deleteIcon.setSize("12");
                deleteIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 2; -fx-min-width: 24px; -fx-min-height: 24px; -fx-max-width: 24px; -fx-max-height: 24px; -fx-padding: 2px;");
                deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Delete seat"));
                
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                
                // Set button actions
                updateBtn.setOnAction(event -> {
                    SeatModel seat = getTableView().getItems().get(getIndex());
                    handleUpdateSeat(seat);
                });
                
                deleteBtn.setOnAction(event -> {
                    SeatModel seat = getTableView().getItems().get(getIndex());
                    handleDeleteSeat(seat);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }
    
    private void handleUpdateSeat(SeatModel seat) {
        openSeatForm(seat);
    }
    
    private void handleDeleteSeat(SeatModel seat) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Seat");
        confirmDialog.setContentText("Are you sure you want to delete seat " + seat.getSeatNumber() + " in hall " + seat.getHallName() + "?");
        
        // Create custom buttons
        ButtonType cancelButtonType = new ButtonType("Cancel");
        ButtonType deleteButtonType = new ButtonType("Delete");
        confirmDialog.getButtonTypes().setAll(cancelButtonType, deleteButtonType);
        
        // Add styling to buttons
        Button deleteButton = (Button) confirmDialog.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        
        // Add icon to delete button
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView deleteIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TRASH);
        deleteIcon.setFill(javafx.scene.paint.Color.WHITE);
        deleteButton.setGraphic(deleteIcon);
        
        Button cancelButton = (Button) confirmDialog.getDialogPane().lookupButton(cancelButtonType);
        cancelButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white;");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == deleteButtonType) {
            deleteSeat(seat.getId());
        }
    }
    
    private void deleteSeat(int seatId) {
        try (Connection connection = SingletonConnection.getConnection()) {
            String sql = "DELETE FROM Seats WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, seatId);
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows > 0) {
                    // Success - refresh the table
                    loadSeats();
                } else {
                    showErrorAlert("Error", "Delete Failed", "No seat was deleted. The seat may not exist.");
                }
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not delete the seat", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
