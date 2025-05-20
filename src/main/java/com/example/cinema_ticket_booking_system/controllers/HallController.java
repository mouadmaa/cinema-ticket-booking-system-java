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
        hallTableView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");

        setupButtons();

        setupTableColumns();
        loadHalls();

        hallTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void setupButtons() {
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView addIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.BUILDING);
        addIcon.setFill(javafx.scene.paint.Color.WHITE);
        addHallButton.setGraphic(addIcon);
        addHallButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 2;");
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        capacityColumn.setCellValueFactory(cellData -> cellData.getValue().capacityProperty().asObject());

        idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        nameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50);
        capacityColumn.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        actionsColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);

        actionsColumn.setMinWidth(70);

        actionsColumn.setStyle("-fx-alignment: CENTER;");

        setupActionsColumn();
    }

    private void loadHalls() {
        ObservableList<HallModel> halls = FXCollections.observableArrayList();

        try (Connection connection = SingletonConnection.getConnection()) {
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
        hallTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        hallTableView.getColumns().forEach(column -> {
            double width = column.getWidth();
            column.setPrefWidth(width);
        });
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView errorIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.EXCLAMATION_CIRCLE);
        errorIcon.setSize("28");
        errorIcon.setFill(javafx.scene.paint.Color.valueOf("#F44336"));

        javafx.scene.layout.HBox headerBox = new javafx.scene.layout.HBox(10, errorIcon, new Label(header));
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        alert.getDialogPane().setHeader(headerBox);

        alert.getDialogPane().setStyle("-fx-background-color: white;");
        alert.getDialogPane().getStyleClass().add("alert");
        
        alert.showAndWait();
    }

    @FXML
    private void handleAddHallButton(ActionEvent event) {
        openHallForm(null);
    }
    
    private void handleUpdateHall(HallModel hall) {
        openHallForm(hall);
    }
    
    private void handleDeleteHall(HallModel hall) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Hall");
        confirmDialog.setContentText("Are you sure you want to delete hall \"" + hall.getName() + "\"?");

        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView warningIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.WARNING);
        warningIcon.setSize("28");
        warningIcon.setFill(javafx.scene.paint.Color.valueOf("#FF9800"));

        javafx.scene.layout.HBox headerBox = new javafx.scene.layout.HBox(10, warningIcon, new Label("Delete Hall"));
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        confirmDialog.getDialogPane().setHeader(headerBox);
        confirmDialog.getDialogPane().setStyle("-fx-background-color: white;");
        confirmDialog.getDialogPane().getStyleClass().add("alert");

        ButtonType cancelButtonType = new ButtonType("Cancel");
        ButtonType deleteButtonType = new ButtonType("Delete");
        confirmDialog.getButtonTypes().setAll(cancelButtonType, deleteButtonType);

        Button deleteButton = (Button) confirmDialog.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

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
            FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("HallFormView.fxml"));
            Parent root = loader.load();

            HallFormController controller = loader.getController();
            controller.setRefreshCallback(this::loadHalls);

            if (hall != null) {
                controller.setHallForUpdate(hall);
            }

            Stage stage = new Stage();
            stage.setTitle(hall == null ? "Add Hall" : "Update Hall");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

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
                de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView editIcon = 
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.EDIT);
                editIcon.setSize("12");
                editIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                updateBtn.setGraphic(editIcon);
                updateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 2; -fx-min-width: 24px; -fx-min-height: 24px; -fx-max-width: 24px; -fx-max-height: 24px; -fx-padding: 2px;");
                updateBtn.setTooltip(new javafx.scene.control.Tooltip("Edit hall"));

                de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView deleteIcon = 
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TRASH);
                deleteIcon.setSize("12");
                deleteIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 2; -fx-min-width: 24px; -fx-min-height: 24px; -fx-max-width: 24px; -fx-max-height: 24px; -fx-padding: 2px;");
                deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Delete hall"));
                
                pane.setAlignment(javafx.geometry.Pos.CENTER);

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
