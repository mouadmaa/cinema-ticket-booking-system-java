package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.MainApplication;
import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.HallModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HallController implements Initializable {

    @FXML
    private TableView<HallModel> hallTableView;
    
    @FXML
    private TableColumn<HallModel, Integer> idColumn;
    
    @FXML
    private TableColumn<HallModel, String> nameColumn;
    
    @FXML
    private TableColumn<HallModel, Integer> capacityColumn;
    
    @FXML
    private TableColumn<HallModel, Void> actionsColumn;
    
    @FXML
    private Button addHallButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Apply style to table
        hallTableView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        
        // Style buttons with FontAwesome icons
        setupButtons();
        
        // Setup table columns and load data
        setupTableColumns();
        loadHalls();
    
        // Configure table to automatically resize columns to fit content
        hallTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void setupButtons() {
        // Style the add hall button with a building icon
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView addIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.BUILDING);
        addIcon.setFill(javafx.scene.paint.Color.WHITE);
        addHallButton.setGraphic(addIcon);
        addHallButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 2;");
    }

    private void setupTableColumns() {
        // Use lambda instead of PropertyValueFactory for more reliable binding
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        capacityColumn.setCellValueFactory(cellData -> cellData.getValue().capacityProperty().asObject());
        
        // Set column proportional widths based on content
        idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        nameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 50% width
        capacityColumn.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30% width
        actionsColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        
        // Set minimum width for actions column to ensure buttons fit
        actionsColumn.setMinWidth(70);
        
        // Set alignment for actions column to center
        actionsColumn.setStyle("-fx-alignment: CENTER;");
        
        // Configure the actions column with update and delete buttons
        setupActionsColumn();
    }

    private void loadHalls() {
        ObservableList<HallModel> halls = FXCollections.observableArrayList();

        try (Connection connection = SingletonConnection.getConnection()) {
            // Fetch all halls
            String query = "SELECT id, name, capacity FROM Halls ORDER BY id";
    
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
    
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int capacity = resultSet.getInt("capacity");
    
                    HallModel hall = new HallModel(id, name, capacity);
                    halls.add(hall);
                }
            }
    
            hallTableView.setItems(halls);

            // Ensure columns fit the data after loading
            autoResizeColumns();

        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not connect to the database", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Adjust columns to fit content after data is loaded
     */
    private void autoResizeColumns() {
        // Ensure the table uses all available width
        hallTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Optionally force a resize for all columns
        hallTableView.getColumns().forEach(column -> {
            // Trick to refresh the column width calculations
            double width = column.getWidth();
            column.setPrefWidth(width);
        });
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        // Create custom header with icon
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView errorIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.EXCLAMATION_CIRCLE);
        errorIcon.setSize("28");
        errorIcon.setFill(javafx.scene.paint.Color.valueOf("#F44336"));
        
        // Set header with icon and text
        javafx.scene.layout.HBox headerBox = new javafx.scene.layout.HBox(10, errorIcon, new Label(header));
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        // Set dialog header content
        alert.getDialogPane().setHeader(headerBox);
        
        // Apply some styling
        alert.getDialogPane().setStyle("-fx-background-color: white;");
        alert.getDialogPane().getStyleClass().add("alert");
        
        alert.showAndWait();
    }

    @FXML
    private void handleAddHallButton(ActionEvent event) {
        openHallForm(null); // Pass null for adding a new hall
    }
    
    private void handleUpdateHall(HallModel hall) {
        openHallForm(hall); // Pass the hall for updating
    }
    
    private void handleDeleteHall(HallModel hall) {
        // Create confirmation alert
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Hall");
        confirmDialog.setContentText("Are you sure you want to delete hall \"" + hall.getName() + "\"?");
        
        // Create custom header with warning icon
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView warningIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.WARNING);
        warningIcon.setSize("28");
        warningIcon.setFill(javafx.scene.paint.Color.valueOf("#FF9800"));
        
        // Set header with icon
        javafx.scene.layout.HBox headerBox = new javafx.scene.layout.HBox(10, warningIcon, new Label("Delete Hall"));
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        // Style the dialog
        confirmDialog.getDialogPane().setHeader(headerBox);
        confirmDialog.getDialogPane().setStyle("-fx-background-color: white;");
        confirmDialog.getDialogPane().getStyleClass().add("alert");
        
        // Create custom buttons
        ButtonType cancelButtonType = new ButtonType("Cancel");
        ButtonType deleteButtonType = new ButtonType("Delete");
        confirmDialog.getButtonTypes().setAll(cancelButtonType, deleteButtonType);
        
        // Custom styling for buttons
        Button deleteButton = (Button) confirmDialog.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        
        // Add a trash icon to delete button
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView deleteIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TRASH);
        deleteIcon.setFill(javafx.scene.paint.Color.WHITE);
        deleteButton.setGraphic(deleteIcon);
        
        Button cancelButton = (Button) confirmDialog.getDialogPane().lookupButton(cancelButtonType);
        cancelButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white;");
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == deleteButtonType) {
                deleteHall(hall.getId());
            }
        });
    }
    
    private void openHallForm(HallModel hall) {
        try {
            // Load the hall form FXML
            FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("HallFormView.fxml"));
            Parent root = loader.load();
            
            // Get the controller and set callback to refresh hall table after save
            HallFormController controller = loader.getController();
            controller.setRefreshCallback(this::loadHalls);
            
            // If updating, set the hall to be updated
            if (hall != null) {
                controller.setHallForUpdate(hall);
            }
            
            // Create a new stage for the dialog
            Stage stage = new Stage();
            stage.setTitle(hall == null ? "Add Hall" : "Update Hall");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            
            // Show the dialog and wait for it to close
            stage.showAndWait();
            
        } catch (IOException e) {
            showErrorAlert("Error", "Could not open the Hall Form", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void deleteHall(int hallId) {
        try (Connection connection = SingletonConnection.getConnection()) {
            String sql = "DELETE FROM Halls WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, hallId);
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows > 0) {
                    // Success - refresh the table
                    loadHalls();
                } else {
                    showErrorAlert("Error", "Delete Failed", "No hall was deleted. The hall may not exist.");
                }
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not delete the hall", e.getMessage());
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
                updateBtn.setTooltip(new javafx.scene.control.Tooltip("Edit hall"));
                
                // Create and style the delete button with icon only
                de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView deleteIcon = 
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TRASH);
                deleteIcon.setSize("12");
                deleteIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 2; -fx-min-width: 24px; -fx-min-height: 24px; -fx-max-width: 24px; -fx-max-height: 24px; -fx-padding: 2px;");
                deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Delete hall"));
                
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                
                // Set button actions
                updateBtn.setOnAction(event -> {
                    HallModel hall = getTableView().getItems().get(getIndex());
                    handleUpdateHall(hall);
                });
                
                deleteBtn.setOnAction(event -> {
                    HallModel hall = getTableView().getItems().get(getIndex());
                    handleDeleteHall(hall);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }
}
