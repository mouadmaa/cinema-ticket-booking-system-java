package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.BookingModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class BookingController implements Initializable {

    @FXML
    private TableView<BookingModel> bookingTableView;
    
    @FXML
    private TableColumn<BookingModel, Integer> idColumn;
    
    @FXML
    private TableColumn<BookingModel, String> clientNameColumn;
    
    @FXML
    private TableColumn<BookingModel, String> showInfoColumn;
    
    @FXML
    private TableColumn<BookingModel, String> seatInfoColumn;
    
    @FXML
    private TableColumn<BookingModel, String> statusColumn;
    
    @FXML
    private TableColumn<BookingModel, LocalDateTime> bookingDateColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        showInfoColumn.setCellValueFactory(new PropertyValueFactory<>("showInfo"));
        seatInfoColumn.setCellValueFactory(new PropertyValueFactory<>("seatInfo"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        
        // Format date column
        bookingDateColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        
        // Format status column with colors
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Confirmed":
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case "Pending":
                            setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
                            break;
                        case "Cancelled":
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });
        
        // Load data from the database
        refreshBookingTable();
        
        // Adjust column widths
        bookingTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void refreshBookingTable() {
        bookingTableView.setItems(getAllBookings());
    }
    
    private ObservableList<BookingModel> getAllBookings() {
        ObservableList<BookingModel> bookings = FXCollections.observableArrayList();
        
        try {
            Connection conn = SingletonConnection.getConnection();
            Statement stmt = conn.createStatement();
            
            // Join with Client, Show, and Seat tables to get more information
            String query = "SELECT b.*, " +
                    "CONCAT(c.first_name, ' ', c.last_name) AS client_name, " +
                    "CONCAT(m.title, ' (', TO_CHAR(s.show_date, 'YYYY-MM-DD HH24:MI'), ')') AS show_info, " +
                    "CONCAT('Hall ', h.hall_number, ' - Seat ', st.seat_number) AS seat_info " +
                    "FROM Bookings b " +
                    "JOIN Clients c ON b.client_id = c.id " +
                    "JOIN Shows s ON b.show_id = s.id " +
                    "JOIN Movies m ON s.movie_id = m.id " +
                    "JOIN Seats st ON b.seat_id = st.id " +
                    "JOIN Halls h ON st.hall_id = h.id " +
                    "ORDER BY b.id";
            
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                LocalDateTime bookingDate = rs.getTimestamp("booking_date").toLocalDateTime();
                
                bookings.add(new BookingModel(
                    rs.getInt("id"),
                    rs.getInt("client_id"),
                    rs.getInt("show_id"),
                    rs.getInt("seat_id"),
                    rs.getString("status"),
                    bookingDate,
                    rs.getString("client_name"),
                    rs.getString("show_info"),
                    rs.getString("seat_info")
                ));
            }
            
            rs.close();
            stmt.close();
            // Don't close the connection as it's managed by SingletonConnection
            
        } catch (SQLException e) {
            System.err.println("Error fetching bookings from database: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }
}
