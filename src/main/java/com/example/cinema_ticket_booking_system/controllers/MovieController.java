package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.MainApplication;
import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.MovieModel;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MovieController implements Initializable {

    @FXML
    private TableView<MovieModel> movieTableView;
    
    @FXML
    private TableColumn<MovieModel, Integer> idColumn;
    
    @FXML
    private TableColumn<MovieModel, String> titleColumn;
    
    @FXML
    private TableColumn<MovieModel, String> genreColumn;
    
    @FXML
    private TableColumn<MovieModel, Integer> durationColumn;
    
    @FXML
    private TableColumn<MovieModel, LocalDate> releaseDateColumn;
    
    @FXML
    private TableColumn<MovieModel, String> descriptionColumn;
    
    @FXML
    private TableColumn<MovieModel, Void> actionsColumn;
    
    @FXML
    private Button prevButton;
    
    @FXML
    private Button nextButton;
    
    @FXML
    private Label pageLabel;
    
    @FXML
    private Button addMovieButton;

    private int currentPage = 1;
    private final int pageSize = 50;
    private int totalPages = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Apply style to table
        movieTableView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        
        // Style pagination and control buttons with FontAwesome icons
        setupButtons();
        
        // Setup table columns and load data
        setupTableColumns();
        loadMovies();
    
        // Configure table to automatically resize columns to fit content
        movieTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void setupButtons() {
        // Style the previous button with a left arrow icon
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView prevIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.ARROW_LEFT);
        prevIcon.setFill(javafx.scene.paint.Color.WHITE);
        prevButton.setGraphic(prevIcon);
        prevButton.setStyle("-fx-background-color: #455A64; -fx-text-fill: white; -fx-background-radius: 2;");
        
        // Style the next button with a right arrow icon
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView nextIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.ARROW_RIGHT);
        nextIcon.setFill(javafx.scene.paint.Color.WHITE);
        nextButton.setGraphic(nextIcon);
        nextButton.setStyle("-fx-background-color: #455A64; -fx-text-fill: white; -fx-background-radius: 2;");
        
        // Style the add movie button with a film icon
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView addIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.FILM);
        addIcon.setFill(javafx.scene.paint.Color.WHITE);
        addMovieButton.setGraphic(addIcon);
        addMovieButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 2;");
        
        // Style the page label
        pageLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #455A64;");
    }

    private void setupTableColumns() {
        // Use lambda instead of PropertyValueFactory for more reliable binding
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asObject());
        releaseDateColumn.setCellValueFactory(cellData -> cellData.getValue().releaseDateProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        
        // Set column proportional widths based on content
        idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 5); // 5% width
        titleColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width
        genreColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        durationColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        releaseDateColumn.setMaxWidth(1f * Integer.MAX_VALUE * 12); // 12% width
        descriptionColumn.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30% width
        actionsColumn.setMaxWidth(1f * Integer.MAX_VALUE * 8); // 8% width
        
        // Set minimum width for actions column to ensure buttons fit
        actionsColumn.setMinWidth(70);
        
        // Set alignment for actions column to center
        actionsColumn.setStyle("-fx-alignment: CENTER;");
        
        // Format duration column to display as hours and minutes
        durationColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    int hours = item / 60;
                    int minutes = item % 60;
                    if (hours > 0) {
                        setText(hours + " hr " + (minutes > 0 ? minutes + " min" : ""));
                    } else {
                        setText(minutes + " min");
                    }
                }
            }
        });
        
        // Format release date column
        releaseDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
        
        // Limit description text and add tooltip for full text
        descriptionColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    String text = item.length() > 50 ? item.substring(0, 47) + "..." : item;
                    setText(text);
                    setTooltip(new Tooltip(item));
                }
            }
        });
        
        // Configure the actions column with update and delete buttons
        setupActionsColumn();
    }

    private void loadMovies() {
        ObservableList<MovieModel> movies = FXCollections.observableArrayList();

        try (Connection connection = SingletonConnection.getConnection()) {
            // Count total movies to calculate pagination
            String countQuery = "SELECT COUNT(*) FROM Movies";
            try (PreparedStatement countStatement = connection.prepareStatement(countQuery)) {
                ResultSet countResult = countStatement.executeQuery();
                if (countResult.next()) {
                    int totalMovies = countResult.getInt(1);
                    totalPages = (int) Math.ceil((double) totalMovies / pageSize);
                }
            }

            // Fetch movies for the current page
            String query = "SELECT id, title, genre, duration, release_date, description " +
                    "FROM Movies ORDER BY id LIMIT ? OFFSET ?";
    
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, pageSize);
                statement.setInt(2, (currentPage - 1) * pageSize);
    
                ResultSet resultSet = statement.executeQuery();
    
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    int duration = resultSet.getInt("duration");
                    Date releaseDateSql = resultSet.getDate("release_date");
                    LocalDate releaseDate = releaseDateSql.toLocalDate();
                    String description = resultSet.getString("description");
    
                    MovieModel movie = new MovieModel(id, title, genre, duration, releaseDate, description);
                    movies.add(movie);
                }
            }

            movieTableView.setItems(movies);
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
        movieTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Optionally force a resize for all columns
        movieTableView.getColumns().forEach(column -> {
            // Trick to refresh the column width calculations
            double width = column.getWidth();
            column.setPrefWidth(width);
        });
    }

    @FXML
    private void handlePrevButton(ActionEvent event) {
        if (currentPage > 1) {
            currentPage--;
            loadMovies();
        }
    }

    @FXML
    private void handleNextButton(ActionEvent event) {
        if (currentPage < totalPages) {
            currentPage++;
            loadMovies();
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
    private void handleAddMovieButton(ActionEvent event) {
        openMovieForm(null); // Pass null for adding a new movie
    }
    
    private void handleUpdateMovie(MovieModel movie) {
        openMovieForm(movie); // Pass the movie for updating
    }
    
    private void handleDeleteMovie(MovieModel movie) {
        // Create confirmation alert
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Movie");
        confirmDialog.setContentText("Are you sure you want to delete movie \"" + movie.getTitle() + "\"?");
        
        // Create custom header with warning icon
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView warningIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.WARNING);
        warningIcon.setSize("28");
        warningIcon.setFill(javafx.scene.paint.Color.valueOf("#FF9800"));
        
        // Set header with icon
        javafx.scene.layout.HBox headerBox = new javafx.scene.layout.HBox(10, warningIcon, new Label("Delete Movie"));
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
                deleteMovie(movie.getId());
            }
        });
    }
    
    private void openMovieForm(MovieModel movie) {
        try {
            // Load the movie form FXML
            FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("MovieFormView.fxml"));
            Parent root = loader.load();
            
            // Get the controller and set callback to refresh movie table after save
            MovieFormController controller = loader.getController();
            controller.setRefreshCallback(this::loadMovies);
            
            // If updating, set the movie to be updated
            if (movie != null) {
                controller.setMovieForUpdate(movie);
            }
            
            // Create a new stage for the dialog
            Stage stage = new Stage();
            stage.setTitle(movie == null ? "Add Movie" : "Update Movie");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            
            // Show the dialog and wait for it to close
            stage.showAndWait();
            
        } catch (IOException e) {
            showErrorAlert("Error", "Could not open the Movie Form", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void deleteMovie(int movieId) {
        try (Connection connection = SingletonConnection.getConnection()) {
            String sql = "DELETE FROM Movies WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, movieId);
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows > 0) {
                    // Success - refresh the table
                    loadMovies();
                } else {
                    showErrorAlert("Error", "Delete Failed", "No movie was deleted. The movie may not exist.");
                }
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not delete the movie", e.getMessage());
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
                updateBtn.setTooltip(new javafx.scene.control.Tooltip("Edit movie"));
                
                // Create and style the delete button with icon only
                de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView deleteIcon = 
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TRASH);
                deleteIcon.setSize("12");
                deleteIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 2; -fx-min-width: 24px; -fx-min-height: 24px; -fx-max-width: 24px; -fx-max-height: 24px; -fx-padding: 2px;");
                deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Delete movie"));
                
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                
                // Set button actions
                updateBtn.setOnAction(event -> {
                    MovieModel movie = getTableView().getItems().get(getIndex());
                    handleUpdateMovie(movie);
                });
                
                deleteBtn.setOnAction(event -> {
                    MovieModel movie = getTableView().getItems().get(getIndex());
                    handleDeleteMovie(movie);
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
