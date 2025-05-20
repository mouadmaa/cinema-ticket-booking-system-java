package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.ClientModel;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for ClientModel
 * Provides methods to interact with client data in the database
 */
public interface ClientModelDao {
    
    /**
     * Save a new client to the database
     * 
     * @param client the client to save
     * @return the saved client with generated ID
     */
    ClientModel save(ClientModel client);
    
    /**
     * Update an existing client in the database
     * 
     * @param client the client to update
     * @return true if successful, false otherwise
     */
    boolean update(ClientModel client);
    
    /**
     * Delete a client from the database by ID
     * 
     * @param id the ID of the client to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Find a client by ID
     * 
     * @param id the ID of the client to find
     * @return Optional containing the client if found, empty optional otherwise
     */
    Optional<ClientModel> findById(int id);
    
    /**
     * Find a client by email
     * 
     * @param email the email of the client to find
     * @return Optional containing the client if found, empty optional otherwise
     */
    Optional<ClientModel> findByEmail(String email);
    
    /**
     * Find clients by last name
     * 
     * @param lastName the last name to search for
     * @return List of clients with the specified last name
     */
    List<ClientModel> findByLastName(String lastName);
    
    /**
     * Find a client by phone number
     * 
     * @param phoneNumber the phone number to search for
     * @return Optional containing the client if found, empty optional otherwise
     */
    Optional<ClientModel> findByPhoneNumber(String phoneNumber);
    
    /**
     * Find all clients in the database
     * 
     * @return List of all clients
     */
    List<ClientModel> findAll();
    
    /**
     * Authenticate a client with email and password
     * 
     * @param email the email
     * @param password the password
     * @return Optional containing the client if authentication succeeds, empty optional otherwise
     */
    Optional<ClientModel> authenticate(String email, String password);
    
    /**
     * Change client password
     * 
     * @param clientId the ID of the client
     * @param newPassword the new password
     * @return true if successful, false otherwise
     */
    boolean changePassword(int clientId, String newPassword);
}
