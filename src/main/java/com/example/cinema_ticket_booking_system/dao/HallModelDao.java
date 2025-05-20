package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.HallModel;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for HallModel
 * Provides methods to interact with cinema hall data in the database
 */
public interface HallModelDao {
    
    /**
     * Save a new hall to the database
     * 
     * @param hall the hall to save
     * @return the saved hall with generated ID
     */
    HallModel save(HallModel hall);
    
    /**
     * Update an existing hall in the database
     * 
     * @param hall the hall to update
     * @return true if successful, false otherwise
     */
    boolean update(HallModel hall);
    
    /**
     * Delete a hall from the database by ID
     * 
     * @param id the ID of the hall to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Find a hall by ID
     * 
     * @param id the ID of the hall to find
     * @return Optional containing the hall if found, empty optional otherwise
     */
    Optional<HallModel> findById(int id);
    
    /**
     * Find a hall by name
     * 
     * @param name the name of the hall to find
     * @return Optional containing the hall if found, empty optional otherwise
     */
    Optional<HallModel> findByName(String name);
    
    /**
     * Find all halls in the database
     * 
     * @return List of all halls
     */
    List<HallModel> findAll();
    
    /**
     * Find halls with capacity greater than or equal to specified value
     * 
     * @param minCapacity the minimum capacity to filter by
     * @return List of halls with capacity greater than or equal to specified value
     */
    List<HallModel> findByMinCapacity(int minCapacity);
    
    /**
     * Get the total number of seats in a hall
     * 
     * @param hallId the ID of the hall
     * @return the total number of seats
     */
    int getTotalSeatsCount(int hallId);
    
    /**
     * Check if a hall is available at a specific date and time
     * 
     * @param hallId the ID of the hall
     * @param date the date in yyyy-MM-dd format
     * @param startTime the start time in HH:mm format
     * @param duration the duration in minutes
     * @return true if hall is available, false otherwise
     */
    boolean isHallAvailable(int hallId, String date, String startTime, int duration);
}
