package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.TicketModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TicketController implements Initializable {

    @FXML
    private TableView<TicketModel> ticketTableView;
    
    @FXML
    private TableColumn<TicketModel, Integer> idColumn;
    
    @FXML
    private TableColumn<TicketModel, String> ticketCodeColumn;
    
    @FXML
    private TableColumn<TicketModel, String> clientNameColumn;
    
    @FXML
    private TableColumn<TicketModel, String> showInfoColumn;
    
    @FXML
    private TableColumn<TicketModel, String> seatInfoColumn;
    
    @FXML
    private TableColumn<TicketModel, String> statusColumn;
    
    @FXML
    private TableColumn<TicketModel, BigDecimal> priceColumn;
    
    @FXML
    private TableColumn<TicketModel, LocalDateTime> issueDateColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ticketCodeColumn.setCellValueFactory(new PropertyValueFactory<>("ticketCode"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        showInfoColumn.setCellValueFactory(new PropertyValueFactory<>("showInfo"));
        seatInfoColumn.setCellValueFactory(new PropertyValueFactory<>("seatInfo"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " DH");
                }
            }
        });

        issueDateColumn.setCellFactory(column -> new TableCell<>() {
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
                        case "Active":
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

        ticketCodeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-font-family: 'Monospace'; -fx-font-weight: bold;");
                }
            }
        });

        refreshTicketTable();

        ticketTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void refreshTicketTable() {
        ticketTableView.setItems(getAllTickets());
    }
    
    private ObservableList<TicketModel> getAllTickets() {
        ObservableList<TicketModel> tickets = FXCollections.observableArrayList();
        
        try {
            Connection conn = SingletonConnection.getConnection();
            Statement stmt = conn.createStatement();

            String query = "SELECT t.*, " +
                    "CONCAT(c.first_name, ' ', c.last_name) AS client_name, " +
                    "CONCAT(m.title, ' (', TO_CHAR(s.show_date, 'YYYY-MM-DD HH24:MI'), ')') AS show_info, " +
                    "CONCAT('Hall: ', h.name, ' - Seat ID: ', st.id) AS seat_info " +
                    "FROM Tickets t " +
                    "JOIN Bookings b ON t.booking_id = b.id " +
                    "JOIN Clients c ON b.client_id = c.id " +
                    "JOIN Shows s ON b.show_id = s.id " +
                    "JOIN Movies m ON s.movie_id = m.id " +
                    "JOIN Seats st ON b.seat_id = st.id " +
                    "JOIN Halls h ON st.hall_id = h.id " +
                    "ORDER BY t.id";
            
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                LocalDateTime issueDate = rs.getTimestamp("issue_date").toLocalDateTime();
                BigDecimal price = rs.getBigDecimal("price");
                
                tickets.add(new TicketModel(
                    rs.getInt("id"),
                    rs.getInt("booking_id"),
                    rs.getString("status"),
                    rs.getString("ticket_code"),
                    issueDate,
                    price,
                    rs.getString("client_name"),
                    rs.getString("show_info"),
                    rs.getString("seat_info")
                ));
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error fetching tickets from database: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tickets;
    }
}
