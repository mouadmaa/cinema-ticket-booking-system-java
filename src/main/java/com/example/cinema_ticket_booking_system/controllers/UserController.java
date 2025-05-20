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
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
    private TableColumn<UserModel, Void> actionsColumn;
    
    @FXML
    private Button addUserButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userTableView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");

        setupButtons();

        setupTableColumns();
        loadUsers();

        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void setupButtons() {
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView addIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.USER_PLUS);
        addIcon.setFill(javafx.scene.paint.Color.WHITE);
        addUserButton.setGraphic(addIcon);
        addUserButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 2;");
    }

    void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());

        idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        roleColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        firstNameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        lastNameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        usernameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 15);
        emailColumn.setMaxWidth(1f * Integer.MAX_VALUE * 22);
        phoneNumberColumn.setMaxWidth(1f * Integer.MAX_VALUE * 16);
        actionsColumn.setMaxWidth(1f * Integer.MAX_VALUE * 8);

        actionsColumn.setMinWidth(70);

        actionsColumn.setStyle("-fx-alignment: CENTER;");

        setupActionsColumn();
    }

    void loadUsers() {
        ObservableList<UserModel> users = FXCollections.observableArrayList();

        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "SELECT id, role, first_name, last_name, username, email, phone_number " +
                    "FROM Users ORDER BY id";
    
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
    
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String role = resultSet.getString("role");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phone_number");

                    UserModel user = new UserModel(id, role, firstName, lastName, username, email, phoneNumber, "");
                    users.add(user);
                }
            }
    
            userTableView.setItems(users);

            autoResizeColumns();

        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not connect to the database", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Adjust columns to fit content after data is loaded
     */
    void autoResizeColumns() {
        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        userTableView.getColumns().forEach(column -> {
            double width = column.getWidth();
            column.setPrefWidth(width);
        });
    }


    void showErrorAlert(String title, String header, String content) {
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
    void handleAddUserButton(ActionEvent event) {
        openUserForm(null);
    }
    
    void handleUpdateUser(UserModel user) {
        openUserForm(user);
    }
    
    private void handleDeleteUser(UserModel user) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete User");
        confirmDialog.setContentText("Are you sure you want to delete user " + user.getFirstName() + " " + user.getLastName() + "?");

        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView warningIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.WARNING);
        warningIcon.setSize("28");
        warningIcon.setFill(javafx.scene.paint.Color.valueOf("#FF9800"));

        javafx.scene.layout.HBox headerBox = new javafx.scene.layout.HBox(10, warningIcon, new Label("Delete User"));
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
                deleteUser(user.getId());
            }
        });
    }
    
    void openUserForm(UserModel user) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("UserFormView.fxml"));
            Parent root = loader.load();

            UserFormController controller = loader.getController();
            controller.setRefreshCallback(this::loadUsers);

            if (user != null) {
                controller.setUserForUpdate(user);
            }

            Stage stage = new Stage();
            stage.setTitle(user == null ? "Add User" : "Update User");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            
        } catch (IOException e) {
            showErrorAlert("Error", "Could not open the User Form", e.getMessage());
            e.printStackTrace();
        }
    }
    
    void deleteUser(int userId) {
        try (Connection connection = SingletonConnection.getConnection()) {
            String sql = "DELETE FROM Users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows > 0) {
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
    
    void setupActionsColumn() {
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
                updateBtn.setTooltip(new javafx.scene.control.Tooltip("Edit user"));

                de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView deleteIcon = 
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TRASH);
                deleteIcon.setSize("12");
                deleteIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 2; -fx-min-width: 24px; -fx-min-height: 24px; -fx-max-width: 24px; -fx-max-height: 24px; -fx-padding: 2px;");
                deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Delete user"));
                
                pane.setAlignment(javafx.geometry.Pos.CENTER);

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
