package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.UserModel;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for UserModel
 * Provides methods to interact with user data in the database
 */
public interface UserModelDao {
    
    /**
     * Save a new user to the database
     * 
     * @param user the user to save
     * @return the saved user with generated ID
     */
    UserModel save(UserModel user);
    
    /**
     * Update an existing user in the database
     * 
     * @param user the user to update
     * @return true if successful, false otherwise
     */
    boolean update(UserModel user);
    
    /**
     * Delete a user from the database by ID
     * 
     * @param id the ID of the user to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Find a user by ID
     * 
     * @param id the ID of the user to find
     * @return Optional containing the user if found, empty optional otherwise
     */
    Optional<UserModel> findById(int id);
    
    /**
     * Find a user by username
     * 
     * @param username the username of the user to find
     * @return Optional containing the user if found, empty optional otherwise
     */
    Optional<UserModel> findByUsername(String username);
    
    /**
     * Find all users in the database
     * 
     * @return List of all users
     */
    List<UserModel> findAll();
    
    /**
     * Find users by role
     * 
     * @param role the role to filter by
     * @return List of users with the specified role
     */
    List<UserModel> findByRole(String role);
    
    /**
     * Authenticate a user with username and password
     * 
     * @param username the username
     * @param password the password
     * @return Optional containing the user if authentication succeeds, empty optional otherwise
     */
    Optional<UserModel> authenticate(String username, String password);
}
