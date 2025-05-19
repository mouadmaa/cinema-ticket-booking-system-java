package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.ClientModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private TableView<ClientModel> clientTableView;
    
    @FXML
    private TableColumn<ClientModel, Integer> idColumn;
    
    @FXML
    private TableColumn<ClientModel, String> firstNameColumn;
    
    @FXML
    private TableColumn<ClientModel, String> lastNameColumn;
    
    @FXML
    private TableColumn<ClientModel, String> emailColumn;
    
    @FXML
    private TableColumn<ClientModel, String> phoneNumberColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        
        // Load data from the database
        refreshClientTable();
        
        // Adjust column widths
        clientTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void refreshClientTable() {
        clientTableView.setItems(getAllClients());
    }
    
    private ObservableList<ClientModel> getAllClients() {
        ObservableList<ClientModel> clients = FXCollections.observableArrayList();
        
        try {
            Connection conn = SingletonConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Clients ORDER BY id");
            
            while (rs.next()) {
                clients.add(new ClientModel(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone_number")
                ));
            }
            
            rs.close();
            stmt.close();
            // Don't close the connection as it's managed by SingletonConnection
            
        } catch (SQLException e) {
            System.err.println("Error fetching clients from database: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clients;
    }
}
