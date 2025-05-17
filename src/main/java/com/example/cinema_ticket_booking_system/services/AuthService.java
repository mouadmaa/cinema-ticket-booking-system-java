package com.example.cinema_ticket_booking_system.services;

import com.example.cinema_ticket_booking_system.SingletonConnection;
import com.example.cinema_ticket_booking_system.models.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AuthService {
    public Optional<UserModel> authenticateUser(String email, String password) {
        try {
            Connection connection = SingletonConnection.getConnection();
            String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);
                statement.setString(2, password); // Use hashedPassword in production
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        UserModel user = new UserModel(
                            resultSet.getInt("id"),
                            resultSet.getString("role"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("username"),
                            resultSet.getString("email"),
                            resultSet.getString("phone_number"),
                            resultSet.getString("password")
                        );
                        return Optional.of(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
