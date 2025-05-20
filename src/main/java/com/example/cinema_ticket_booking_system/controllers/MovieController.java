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
    private Button addMovieButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieTableView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");

        setupButtons();

        setupTableColumns();
        loadMovies();

        movieTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void setupButtons() {
        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView addIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.FILM);
        addIcon.setFill(javafx.scene.paint.Color.WHITE);
        addMovieButton.setGraphic(addIcon);
        addMovieButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 2;");
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asObject());
        releaseDateColumn.setCellValueFactory(cellData -> cellData.getValue().releaseDateProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        titleColumn.setMaxWidth(1f * Integer.MAX_VALUE * 25);
        genreColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        durationColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        releaseDateColumn.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        descriptionColumn.setMaxWidth(1f * Integer.MAX_VALUE * 30);
        actionsColumn.setMaxWidth(1f * Integer.MAX_VALUE * 8);

        actionsColumn.setMinWidth(70);

        actionsColumn.setStyle("-fx-alignment: CENTER;");

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

        setupActionsColumn();
    }

    private void loadMovies() {
        ObservableList<MovieModel> movies = FXCollections.observableArrayList();

        try (Connection connection = SingletonConnection.getConnection()) {
            String query = "SELECT id, title, genre, duration, release_date, description " +
                    "FROM Movies ORDER BY id";
    
            try (PreparedStatement statement = connection.prepareStatement(query)) {
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
        movieTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        movieTableView.getColumns().forEach(column -> {
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
    private void handleAddMovieButton(ActionEvent event) {
        openMovieForm(null);
    }
    
    private void handleUpdateMovie(MovieModel movie) {
        openMovieForm(movie);
    }
    
    private void handleDeleteMovie(MovieModel movie) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Movie");
        confirmDialog.setContentText("Are you sure you want to delete movie \"" + movie.getTitle() + "\"?");

        de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView warningIcon = 
            new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.WARNING);
        warningIcon.setSize("28");
        warningIcon.setFill(javafx.scene.paint.Color.valueOf("#FF9800"));

        javafx.scene.layout.HBox headerBox = new javafx.scene.layout.HBox(10, warningIcon, new Label("Delete Movie"));
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
                deleteMovie(movie.getId());
            }
        });
    }
    
    private void openMovieForm(MovieModel movie) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.getFxmlUrl("MovieFormView.fxml"));
            Parent root = loader.load();

            MovieFormController controller = loader.getController();
            controller.setRefreshCallback(this::loadMovies);

            if (movie != null) {
                controller.setMovieForUpdate(movie);
            }

            Stage stage = new Stage();
            stage.setTitle(movie == null ? "Add Movie" : "Update Movie");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

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
                de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView editIcon = 
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.EDIT);
                editIcon.setSize("12");
                editIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                updateBtn.setGraphic(editIcon);
                updateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 2; -fx-min-width: 24px; -fx-min-height: 24px; -fx-max-width: 24px; -fx-max-height: 24px; -fx-padding: 2px;");
                updateBtn.setTooltip(new javafx.scene.control.Tooltip("Edit movie"));

                de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView deleteIcon = 
                    new de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView(de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.TRASH);
                deleteIcon.setSize("12");
                deleteIcon.setFill(javafx.scene.paint.Color.WHITE);
                
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 2; -fx-min-width: 24px; -fx-min-height: 24px; -fx-max-width: 24px; -fx-max-height: 24px; -fx-padding: 2px;");
                deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Delete movie"));
                
                pane.setAlignment(javafx.geometry.Pos.CENTER);

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
