package com.example.cinema_ticket_booking_system.controllers;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    // Basic count statistics
    @FXML
    private Label movieCount;
    
    @FXML
    private Label showCount;
    
    @FXML
    private Label hallCount;
    
    @FXML
    private Label seatCount;
    
    @FXML
    private Label userCount;
    
    @FXML
    private Label clientCount;
    
    @FXML
    private Label bookingCount;
    
    @FXML
    private Label ticketCount;
    
    @FXML
    private Label paymentCount;
    
    // Additional basic stats
    @FXML
    private Label activeShows;
    
    @FXML
    private Label todayBookings;
    
    // KPI statistics
    @FXML
    private Label totalRevenue;
    
    @FXML
    private Label weeklyTickets;
    
    @FXML
    private Label averageOccupancy;
    
    @FXML
    private ProgressBar occupancyProgressBar;
    
    // Top movies statistics
    @FXML
    private Label topMovie1Name;
    
    @FXML
    private Label topMovie1Tickets;
    
    @FXML
    private Label topMovie1Revenue;
    
    @FXML
    private Label topMovie2Name;
    
    @FXML
    private Label topMovie2Tickets;
    
    @FXML
    private Label topMovie2Revenue;
    
    @FXML
    private Label topMovie3Name;
    
    @FXML
    private Label topMovie3Tickets;
    
    @FXML
    private Label topMovie3Revenue;
    
    // Upcoming shows
    @FXML
    private Label upcomingShow1Movie;
    
    @FXML
    private Label upcomingShow1Hall;
    
    @FXML
    private Label upcomingShow1DateTime;
    
    @FXML
    private Label upcomingShow1Seats;
    
    @FXML
    private Label upcomingShow2Movie;
    
    @FXML
    private Label upcomingShow2Hall;
    
    @FXML
    private Label upcomingShow2DateTime;
    
    @FXML
    private Label upcomingShow2Seats;
    
    @FXML
    private Label upcomingShow3Movie;
    
    @FXML
    private Label upcomingShow3Hall;
    
    @FXML
    private Label upcomingShow3DateTime;
    
    @FXML
    private Label upcomingShow3Seats;

    private Connection connection;
    private DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 DH");
    private DecimalFormat percentFormat = new DecimalFormat("0.0%");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            connection = SingletonConnection.getConnection();
            loadAllStatistics();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllStatistics() {
        // Load basic table count statistics
        loadTableCountStatistics();
        
        // Load additional statistics
        loadAdditionalBasicStats();
        
        // Load KPI statistics
        loadRevenueStatistics();
        loadWeeklyTicketsSold();
        loadOccupancyRate();
        
        // Load top movies
        loadTopMoviesStatistics();
        
        // Load upcoming shows
        loadUpcomingShows();
    }
    
    private void loadTableCountStatistics() {
        movieCount.setText(String.valueOf(getTableCount("movies")));
        showCount.setText(String.valueOf(getTableCount("shows")));
        hallCount.setText(String.valueOf(getTableCount("halls")));
        seatCount.setText(String.valueOf(getTableCount("seats")));
        userCount.setText(String.valueOf(getTableCount("users")));
        clientCount.setText(String.valueOf(getTableCount("clients")));
        bookingCount.setText(String.valueOf(getTableCount("bookings")));
        ticketCount.setText(String.valueOf(getTableCount("tickets")));
        paymentCount.setText(String.valueOf(getTableCount("payments")));
    }
    
    private void loadAdditionalBasicStats() {
        activeShows.setText(String.valueOf(getActiveShowsCount()));
        todayBookings.setText(String.valueOf(getTodayBookingsCount()));
    }
    
    private int getActiveShowsCount() {
        int count = 0;
        try {
            // Query to get shows that are scheduled in the future
            String query = "SELECT COUNT(*) AS total FROM shows WHERE datetime > CURRENT_TIMESTAMP()";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error counting active shows: " + e.getMessage());
        }
        return count;
    }
    
    private int getTodayBookingsCount() {
        int count = 0;
        try {
            // Query to get bookings made today
            String query = "SELECT COUNT(*) AS total FROM bookings WHERE DATE(booking_date) = CURRENT_DATE()";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error counting today's bookings: " + e.getMessage());
        }
        return count;
    }
    
    private void loadRevenueStatistics() {
        double revenue = getTotalRevenue();
        totalRevenue.setText(currencyFormat.format(revenue));
    }
    
    private double getTotalRevenue() {
        double revenue = 0.0;
        try {
            // Query to get the sum of all payment amounts
            String query = "SELECT SUM(amount) AS total_revenue FROM payments";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                revenue = resultSet.getDouble("total_revenue");
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error calculating total revenue: " + e.getMessage());
        }
        return revenue;
    }
    
    private void loadWeeklyTicketsSold() {
        int ticketsSold = getWeeklyTicketsSold();
        weeklyTickets.setText(String.valueOf(ticketsSold));
    }
    
    private int getWeeklyTicketsSold() {
        int count = 0;
        try {
            // Query to get tickets booked in the last 7 days
            String query = "SELECT COUNT(*) AS total_tickets FROM tickets " +
                           "JOIN bookings ON tickets.booking_id = bookings.id " +
                           "WHERE booking_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY)";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                count = resultSet.getInt("total_tickets");
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error calculating weekly tickets sold: " + e.getMessage());
        }
        return count;
    }
    
    private void loadOccupancyRate() {
        double rate = getAverageOccupancyRate();
        
        // Update progress bar value
        occupancyProgressBar.setProgress(rate);
        
        // Format percentage for display
        averageOccupancy.setText(percentFormat.format(rate));
    }
    
    private double getAverageOccupancyRate() {
        double occupancyRate = 0.0;
        try {
            // Complex query to calculate average occupancy
            // Counts tickets sold for past shows and compares with total seats in halls
            String query = "SELECT AVG(occupancy_rate) AS avg_occupancy FROM (" +
                           "SELECT COUNT(t.id) / h.capacity AS occupancy_rate " +
                           "FROM shows s " +
                           "JOIN halls h ON s.hall_id = h.id " +
                           "LEFT JOIN bookings b ON b.show_id = s.id " +
                           "LEFT JOIN tickets t ON t.booking_id = b.id " +
                           "WHERE s.datetime < CURRENT_TIMESTAMP() " +
                           "GROUP BY s.id) AS show_occupancy";
            
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                occupancyRate = resultSet.getDouble("avg_occupancy");
                // Handle null result
                if (resultSet.wasNull()) {
                    occupancyRate = 0.0;
                }
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error calculating average occupancy rate: " + e.getMessage());
        }
        return occupancyRate;
    }
    
    private void loadTopMoviesStatistics() {
        try {
            // Query to get top 3 movies by ticket sales
            String query = "SELECT m.title, COUNT(t.id) AS tickets_sold, SUM(p.amount) AS revenue " +
                           "FROM movies m " +
                           "JOIN shows s ON s.movie_id = m.id " +
                           "JOIN bookings b ON b.show_id = s.id " +
                           "JOIN tickets t ON t.booking_id = b.id " +
                           "JOIN payments p ON p.booking_id = b.id " +
                           "GROUP BY m.id " +
                           "ORDER BY tickets_sold DESC " +
                           "LIMIT 3";
            
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            int row = 1;
            while (resultSet.next() && row <= 3) {
                String title = resultSet.getString("title");
                int ticketsSold = resultSet.getInt("tickets_sold");
                double revenue = resultSet.getDouble("revenue");
                
                // Update UI based on which row we're on
                switch (row) {
                    case 1:
                        topMovie1Name.setText(title);
                        topMovie1Tickets.setText(String.valueOf(ticketsSold));
                        topMovie1Revenue.setText(currencyFormat.format(revenue));
                        break;
                    case 2:
                        topMovie2Name.setText(title);
                        topMovie2Tickets.setText(String.valueOf(ticketsSold));
                        topMovie2Revenue.setText(currencyFormat.format(revenue));
                        break;
                    case 3:
                        topMovie3Name.setText(title);
                        topMovie3Tickets.setText(String.valueOf(ticketsSold));
                        topMovie3Revenue.setText(currencyFormat.format(revenue));
                        break;
                }
                row++;
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error loading top movies statistics: " + e.getMessage());
        }
    }
    
    private void loadUpcomingShows() {
        try {
            // Query to get upcoming shows in the next 7 days
            String query = "SELECT m.title, h.name AS hall_name, s.datetime, " +
                           "(h.capacity - COUNT(t.id)) AS available_seats " +
                           "FROM shows s " +
                           "JOIN movies m ON s.movie_id = m.id " +
                           "JOIN halls h ON s.hall_id = h.id " +
                           "LEFT JOIN bookings b ON b.show_id = s.id " +
                           "LEFT JOIN tickets t ON t.booking_id = b.id " +
                           "WHERE s.datetime BETWEEN CURRENT_TIMESTAMP() AND DATE_ADD(CURRENT_DATE(), INTERVAL 7 DAY) " +
                           "GROUP BY s.id " +
                           "ORDER BY s.datetime ASC " +
                           "LIMIT 3";
            
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            int row = 1;
            while (resultSet.next() && row <= 3) {
                String movieTitle = resultSet.getString("title");
                String hallName = resultSet.getString("hall_name");
                LocalDateTime showDateTime = resultSet.getTimestamp("datetime").toLocalDateTime();
                int availableSeats = resultSet.getInt("available_seats");
                
                String formattedDateTime = showDateTime.format(dateTimeFormatter);
                
                // Update UI based on which row we're on
                switch (row) {
                    case 1:
                        upcomingShow1Movie.setText(movieTitle);
                        upcomingShow1Hall.setText(hallName);
                        upcomingShow1DateTime.setText(formattedDateTime);
                        upcomingShow1Seats.setText(String.valueOf(availableSeats));
                        break;
                    case 2:
                        upcomingShow2Movie.setText(movieTitle);
                        upcomingShow2Hall.setText(hallName);
                        upcomingShow2DateTime.setText(formattedDateTime);
                        upcomingShow2Seats.setText(String.valueOf(availableSeats));
                        break;
                    case 3:
                        upcomingShow3Movie.setText(movieTitle);
                        upcomingShow3Hall.setText(hallName);
                        upcomingShow3DateTime.setText(formattedDateTime);
                        upcomingShow3Seats.setText(String.valueOf(availableSeats));
                        break;
                }
                row++;
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error loading upcoming shows: " + e.getMessage());
        }
    }

    private int getTableCount(String tableName) {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) AS total FROM " + tableName;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error counting records in " + tableName + ": " + e.getMessage());
        }
        return count;
    }
}
