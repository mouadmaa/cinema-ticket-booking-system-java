package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.MainApplication;
import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.UserModel;
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
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private TableView<UserModel> userTableView;
    
    @FXML
    private TableColumn<UserModel, Integer> idColumn;
    
    @FXML
    private TableColumn<UserModel, String> roleColumn;
    
    @FXML
    private TableColumn<UserModel, String> firstNameColumn;
    
    @FXML
    private TableColumn<UserModel, String> lastNameColumn;
    
    @FXML
    private TableColumn<UserModel, String> usernameColumn;
    
    @FXML
    private TableColumn<UserModel, String> emailColumn;
    
    @FXML
    private TableColumn<UserModel, String> phoneNumberColumn;
    
    @FXML
    private TableColumn<UserModel, LocalDateTime> createdAtColumn;
    
    @FXML
    private TableColumn<UserModel, LocalDateTime> updatedAtColumn;
    
    @FXML
    private TableColumn<UserModel, Void> actionsColumn;
    
    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private Label pageLabel;

    @FXML
    private Button addUserButton;

    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalPages = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadUsers();

        // Configure table to automatically resize columns to fit content
        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupTableColumns() {
        // Use lambda instead of PropertyValueFactory for more reliable binding
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        createdAtColumn.setCellValueFactory(cellData -> cellData.getValue().createdAtProperty());
        updatedAtColumn.setCellValueFactory(cellData -> cellData.getValue().updatedAtProperty());

        // Set column proportional widths based on content
        idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        roleColumn.setMaxWidth(1f * Integer.MAX_VALUE * 9); // 9% width
        firstNameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        lastNameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        usernameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        emailColumn.setMaxWidth(1f * Integer.MAX_VALUE * 16); // 16% width
        phoneNumberColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        createdAtColumn.setMaxWidth(1f * Integer.MAX_VALUE * 8); // 8% width
        updatedAtColumn.setMaxWidth(1f * Integer.MAX_VALUE * 8); // 8% width
        actionsColumn.setMaxWidth(1f * Integer.MAX_VALUE * 14); // 14% width
        
        // Configure the actions column with update and delete buttons
        setupActionsColumn();

        // Format date columns
        createdAtColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toLocalDate().toString());
                }
            }
        });

        updatedAtColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toLocalDate().toString());
                }
            }
        });
    }

    private void loadUsers() {
        ObservableList<UserModel> users = FXCollections.observableArrayList();

        try (Connection connection = SingletonConnection.getConnection()) {
            // Count total users to calculate pagination
            String countQuery = "SELECT COUNT(*) FROM Users";
            try (PreparedStatement countStatement = connection.prepareStatement(countQuery)) {
                ResultSet countResult = countStatement.executeQuery();
                if (countResult.next()) {
                    int totalUsers = countResult.getInt(1);
                    totalPages = (int) Math.ceil((double) totalUsers / pageSize);
                }
            }

            // Fetch users for the current page
            String query = "SELECT id, role, first_name, last_name, username, email, phone_number, created_at, updated_at " +
                    "FROM Users ORDER BY id LIMIT ? OFFSET ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, pageSize);
                statement.setInt(2, (currentPage - 1) * pageSize);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String role = resultSet.getString("role");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phone_number");
                    Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");
                    Timestamp updatedAtTimestamp = resultSet.getTimestamp("updated_at");

                    LocalDateTime createdAt = createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null;
                    LocalDateTime updatedAt = updatedAtTimestamp != null ? updatedAtTimestamp.toLocalDateTime() : null;

                    UserModel user = new UserModel(id, role, firstName, lastName, username, email, phoneNumber, createdAt, updatedAt);
                    users.add(user);
                }
            }

            userTableView.setItems(users);
            updatePaginationControls();

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
        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Optionally force a resize for all columns
        userTableView.getColumns().forEach(column -> {
            // Trick to refresh the column width calculations
            double width = column.getWidth();
            column.setPrefWidth(width);
        });
    }

    @FXML
    private void handlePrevButton(ActionEvent event) {
        if (currentPage > 1) {
            currentPage--;
            loadUsers();
        }
    }

    @FXML
    private void handleNextButton(ActionEvent event) {
        if (currentPage < totalPages) {
            currentPage++;
            loadUsers();
        }
    }

    private void updatePaginationControls() {
        pageLabel.setText("Page " + currentPage + " of " + totalPages);
        prevButton.setDisable(currentPage <= 1);
        nextButton.setDisable(currentPage >= totalPages);
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleAddUserButton(ActionEvent event) {
        openUserForm(null); // Pass null for adding a new user
    }
    
    private void handleUpdateUser(UserModel user) {
        openUserForm(user); // Pass the user for updating
    }
    
    private void handleDeleteUser(UserModel user) {
        // Confirm deletion
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete User");
        confirmDialog.setContentText("Are you sure you want to delete user " + user.getFirstName() + " " + user.getLastName() + "?");
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteUser(user.getId());
            }
        });
    }
    
    private void openUserForm(UserModel user) {
        try {
            // Load the user form FXML
            FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("UserFormView.fxml"));
            Parent root = loader.load();
            
            // Get the controller and set callback to refresh user table after save
            UserFormController controller = loader.getController();
            controller.setRefreshCallback(this::loadUsers);
            
            // If updating, set the user to be updated
            if (user != null) {
                controller.setUserForUpdate(user);
            }
            
            // Create a new stage for the dialog
            Stage stage = new Stage();
            stage.setTitle(user == null ? "Add New User" : "Update User");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            
            // Show the dialog and wait for it to close
            stage.showAndWait();
            
        } catch (IOException e) {
            showErrorAlert("Error", "Could not open the User Form", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void deleteUser(int userId) {
        try (Connection connection = SingletonConnection.getConnection()) {
            String sql = "DELETE FROM Users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows > 0) {
                    // Success - refresh the table
                    loadUsers();
                } else {
                    showErrorAlert("Error", "Delete Failed", "No user was deleted. The user may not exist.");
                }
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not delete the user", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateBtn = new Button("Update");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, updateBtn, deleteBtn);
            
            {
                // Style the buttons
                updateBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                
                // Set button actions
                updateBtn.setOnAction(event -> {
                    UserModel user = getTableView().getItems().get(getIndex());
                    handleUpdateUser(user);
                });
                
                deleteBtn.setOnAction(event -> {
                    UserModel user = getTableView().getItems().get(getIndex());
                    handleDeleteUser(user);
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
