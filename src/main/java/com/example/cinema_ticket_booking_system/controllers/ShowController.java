package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.MainApplication;
import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.MovieModel;
import com.example.cinema_ticket_booking_system.models.ShowModel;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShowController implements Initializable {

    @FXML
    private TableView<ShowModel> showTableView;
    
    @FXML
    private TableColumn<ShowModel, Integer> idColumn;
    
    @FXML
    private TableColumn<ShowModel, String> movieNameColumn;
    
    @FXML
    private TableColumn<ShowModel, String> hallNameColumn;
    
    @FXML
    private TableColumn<ShowModel, LocalDate> showDateColumn;
    
    @FXML
    private TableColumn<ShowModel, LocalTime> showTimeColumn;
    
    @FXML
    private TableColumn<ShowModel, Double> ticketPriceColumn;
    
    @FXML
    private TableColumn<ShowModel, Void> actionsColumn;
    
    @FXML
    private DatePicker dateFilterPicker;
    
    @FXML
    private ComboBox<MovieModel> movieFilterComboBox;
    
    @FXML
    private Button addShowButton;
    
    private ObservableList<ShowModel> allShows = FXCollections.observableArrayList();
    private ObservableList<MovieModel> movies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup table columns
        setupTableColumns();
        
        // Load movie data for the filter combobox
        loadMovies();
        
        // Add listeners to filters
        dateFilterPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            applyFilters();
        });
        
        movieFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            applyFilters();
        });
        
        // Load all shows by default
        loadShows();
    }
    
    private void setupTableColumns() {
        // Configure the table columns using property value factories
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        movieNameColumn.setCellValueFactory(cellData -> cellData.getValue().movieNameProperty());
        hallNameColumn.setCellValueFactory(cellData -> cellData.getValue().hallNameProperty());
        showDateColumn.setCellValueFactory(cellData -> cellData.getValue().showDateProperty());
        showTimeColumn.setCellValueFactory(cellData -> cellData.getValue().showTimeProperty());
        ticketPriceColumn.setCellValueFactory(cellData -> cellData.getValue().ticketPriceProperty().asObject());
        
        // Set column proportional widths
        idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 7); // 7% width
        movieNameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25); // 25% width
        hallNameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 15% width
        showDateColumn.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 15% width
        showTimeColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        ticketPriceColumn.setMaxWidth(1f * Integer.MAX_VALUE * 13); // 13% width
        actionsColumn.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 15% width
        
        // Set minimum width for actions column to ensure buttons fit
        actionsColumn.setMinWidth(80);
        
        // Format date column to display in a readable format
        showDateColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        
        // Format time column to display in a readable format
        showTimeColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            
            @Override
            protected void updateItem(LocalTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        
        // Format price column to display with currency symbol
        ticketPriceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
        
        // Configure actions column with edit and delete buttons
        setupActionsColumn();
        
        // Set alignment for numeric columns to center
        idColumn.setStyle("-fx-alignment: CENTER;");
        showDateColumn.setStyle("-fx-alignment: CENTER;");
        showTimeColumn.setStyle("-fx-alignment: CENTER;");
        ticketPriceColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        // Ensure table uses all available width
        showTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void loadMovies() {
        movies.clear();
        
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "SELECT id, title, genre, duration, release_date, description FROM Movies ORDER BY title";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                
                // Add a null entry for "All Movies" option
                movies.add(null);
                
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    int duration = resultSet.getInt("duration");
                    LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
                    String description = resultSet.getString("description");
                    
                    movies.add(new MovieModel(id, title, genre, duration, releaseDate, description));
                }
                
                movieFilterComboBox.setItems(movies);
                
                // Set a custom cell factory to display movie titles
                movieFilterComboBox.setCellFactory(param -> new ListCell<>() {
                    @Override
                    protected void updateItem(MovieModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("All Movies");
                        } else {
                            setText(item.getTitle());
                        }
                    }
                });
                
                // Set the same approach for the button cell
                movieFilterComboBox.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(MovieModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("All Movies");
                        } else {
                            setText(item.getTitle());
                        }
                    }
                });
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not load movies", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadShows() {
        allShows.clear();
        
        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "SELECT s.id, s.movie_id, s.hall_id, s.show_date, s.show_time, s.ticket_price, "
                          + "m.title AS movie_title, h.name AS hall_name "
                          + "FROM Shows s "
                          + "JOIN Movies m ON s.movie_id = m.id "
                          + "JOIN Halls h ON s.hall_id = h.id "
                          + "ORDER BY s.show_date, s.show_time";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int movieId = resultSet.getInt("movie_id");
                    int hallId = resultSet.getInt("hall_id");
                    LocalDate showDate = resultSet.getDate("show_date").toLocalDate();
                    LocalTime showTime = resultSet.getTime("show_time").toLocalTime();
                    double ticketPrice = resultSet.getDouble("ticket_price");
                    String movieName = resultSet.getString("movie_title");
                    String hallName = resultSet.getString("hall_name");
                    
                    ShowModel show = new ShowModel(id, movieId, hallId, showDate, showTime, 
                                                  ticketPrice, movieName, hallName);
                    allShows.add(show);
                }
                
                showTableView.setItems(allShows);
            }
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Could not load shows", e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void applyFilters() {
        ObservableList<ShowModel> filteredShows = FXCollections.observableArrayList(allShows);
        
        // Apply date filter if selected
        if (dateFilterPicker.getValue() != null) {
            LocalDate selectedDate = dateFilterPicker.getValue();
            filteredShows.removeIf(show -> !show.getShowDate().equals(selectedDate));
        }
        
        // Apply movie filter if selected
        if (movieFilterComboBox.getValue() != null) {
            int selectedMovieId = movieFilterComboBox.getValue().getId();
            filteredShows.removeIf(show -> show.getMovieId() != selectedMovieId);
        }
        
        showTableView.setItems(filteredShows);
    }
    
    @FXML
    private void handleAddShowButton(ActionEvent event) {
        openShowForm(null); // Pass null for adding a new show
    }
    
    private void openShowForm(ShowModel show) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("ShowFormView.fxml"));
            Parent root = loader.load();
            
            ShowFormController controller = loader.getController();
            controller.setRefreshCallback(this::loadShows);
            
            // If updating, set the show to be updated
            if (show != null) {
                controller.setShowForUpdate(show);
            }
            
            // Create a new stage for the dialog
            Stage stage = new Stage();
            stage.setTitle(show == null ? "Add Show" : "Update Show");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            stage.setResizable(true);
            
            // Show the dialog and wait for it to close
            stage.showAndWait();
            
        } catch (IOException e) {
            showErrorAlert("Error", "Could not open the Show Form", e.getMessage());
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
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(
                        de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.EDIT);
                editIcon.setSize("12");
                editIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                updateBtn.setGraphic(editIcon);
                updateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; " +
                                  "-fx-background-radius: 2; -fx-min-width: 24px; " +
                                  "-fx-min-height: 24px; -fx-max-width: 24px; " +
                                  "-fx-max-height: 24px; -fx-padding: 2px;");
                updateBtn.setTooltip(new Tooltip("Edit show"));
                
                // Create and style the delete button with icon only
                de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView deleteIcon = 
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(
                        de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TRASH);
                deleteIcon.setSize("12");
                deleteIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; " +
                                  "-fx-background-radius: 2; -fx-min-width: 24px; " +
                                  "-fx-min-height: 24px; -fx-max-width: 24px; " +
                                  "-fx-max-height: 24px; -fx-padding: 2px;");
                deleteBtn.setTooltip(new Tooltip("Delete show"));
                
                pane.setAlignment(javafx.geometry.Pos.CENTER);
                
                // Set button actions
                updateBtn.setOnAction(event -> {
                    ShowModel show = getTableView().getItems().get(getIndex());
                    handleUpdateShow(show);
                });
                
                deleteBtn.setOnAction(event -> {
                    ShowModel show = getTableView().getItems().get(getIndex());
                    handleDeleteShow(show);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }
    
    private void handleUpdateShow(ShowModel show) {
        openShowForm(show);
    }
    
    private void handleDeleteShow(ShowModel show) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Show");
        confirmDialog.setContentText("Are you sure you want to delete the show for \"" + 
                                    show.getMovieName() + "\" in " + show.getHallName() + 
                                    " on " + show.getShowDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
                                    " at " + show.getShowTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "?");
        
        // Style the dialog
        DialogPane dialogPane = confirmDialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/cinema_ticket_booking_system/styles/application.css").toExternalForm());
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection connection = SingletonConnection.getConnection()) {
                String query = "DELETE FROM Shows WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, show.getId());
                    
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        loadShows(); // Refresh the table
                        showInformationAlert("Success", "Show Deleted", 
                                          "The show has been successfully deleted.");
                    } else {
                        showErrorAlert("Error", "Delete Failed", 
                                     "Could not delete the show. Please try again.");
                    }
                }
            } catch (SQLException e) {
                showErrorAlert("Database Error", "Could not delete show", e.getMessage());
                e.printStackTrace();
            }
        }
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
