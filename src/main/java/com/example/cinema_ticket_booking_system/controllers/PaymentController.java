package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.PaymentModel;
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

public class PaymentController implements Initializable {

    @FXML
    private TableView<PaymentModel> paymentTableView;
    
    @FXML
    private TableColumn<PaymentModel, Integer> idColumn;
    
    @FXML
    private TableColumn<PaymentModel, String> clientNameColumn;
    
    @FXML
    private TableColumn<PaymentModel, String> showInfoColumn;
    
    @FXML
    private TableColumn<PaymentModel, BigDecimal> amountColumn;
    
    @FXML
    private TableColumn<PaymentModel, String> paymentMethodColumn;
    
    @FXML
    private TableColumn<PaymentModel, String> statusColumn;
    
    @FXML
    private TableColumn<PaymentModel, LocalDateTime> paymentDateColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        showInfoColumn.setCellValueFactory(new PropertyValueFactory<>("showInfo"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        
        // Format amount column
        amountColumn.setCellFactory(column -> new TableCell<>() {
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
        
        // Format date column
        paymentDateColumn.setCellFactory(column -> new TableCell<>() {
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
                        case "Completed":
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case "Pending":
                            setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
                            break;
                        case "Failed":
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });
        
        // Format payment method column
        paymentMethodColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Credit Card":
                            setStyle("-fx-text-fill: #3949AB;");
                            break;
                        case "Debit Card":
                            setStyle("-fx-text-fill: #00897B;");
                            break;
                        case "Cash":
                            setStyle("-fx-text-fill: #388E3C;");
                            break;
                        case "Online":
                            setStyle("-fx-text-fill: #7B1FA2;");
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });
        
        // Load data from the database
        refreshPaymentTable();
        
        // Adjust column widths
        paymentTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void refreshPaymentTable() {
        paymentTableView.setItems(getAllPayments());
    }
    
    private ObservableList<PaymentModel> getAllPayments() {
        ObservableList<PaymentModel> payments = FXCollections.observableArrayList();
        
        try {
            Connection conn = SingletonConnection.getConnection();
            Statement stmt = conn.createStatement();
            
            // Join with Booking, Client, and Show tables to get more information
            String query = "SELECT p.*, " +
                    "CONCAT(c.first_name, ' ', c.last_name) AS client_name, " +
                    "CONCAT(m.title, ' (', TO_CHAR(s.show_date, 'YYYY-MM-DD HH24:MI'), ')') AS show_info " +
                    "FROM Payments p " +
                    "JOIN Bookings b ON p.booking_id = b.id " +
                    "JOIN Clients c ON b.client_id = c.id " +
                    "JOIN Shows s ON b.show_id = s.id " +
                    "JOIN Movies m ON s.movie_id = m.id " +
                    "ORDER BY p.id";
            
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                LocalDateTime paymentDate = rs.getTimestamp("payment_date").toLocalDateTime();
                BigDecimal amount = rs.getBigDecimal("amount");
                
                payments.add(new PaymentModel(
                    rs.getInt("id"),
                    rs.getInt("booking_id"),
                    rs.getString("status"),
                    amount,
                    rs.getString("payment_method"),
                    paymentDate,
                    rs.getString("client_name"),
                    rs.getString("show_info")
                ));
            }
            
            rs.close();
            stmt.close();
            // Don't close the connection as it's managed by SingletonConnection
            
        } catch (SQLException e) {
            System.err.println("Error fetching payments from database: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payments;
    }
}
