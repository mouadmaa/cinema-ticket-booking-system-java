package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.SeatModel;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for SeatModel
 * Provides methods to interact with seat data in the database
 */
public interface SeatModelDao {
    
    /**
     * Save a new seat to the database
     * 
     * @param seat the seat to save
     * @return the saved seat with generated ID
     */
    SeatModel save(SeatModel seat);
    
    /**
     * Update an existing seat in the database
     * 
     * @param seat the seat to update
     * @return true if successful, false otherwise
     */
    boolean update(SeatModel seat);
    
    /**
     * Delete a seat from the database by ID
     * 
     * @param id the ID of the seat to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Find a seat by ID
     * 
     * @param id the ID of the seat to find
     * @return Optional containing the seat if found, empty optional otherwise
     */
    Optional<SeatModel> findById(int id);
    
    /**
     * Find a seat by seat number and hall ID
     * 
     * @param seatNumber the seat number
     * @param hallId the ID of the hall
     * @return Optional containing the seat if found, empty optional otherwise
     */
    Optional<SeatModel> findBySeatNumberAndHallId(String seatNumber, int hallId);
    
    /**
     * Find all seats in the database
     * 
     * @return List of all seats
     */
    List<SeatModel> findAll();
    
    /**
     * Find all seats in a specific hall
     * 
     * @param hallId the ID of the hall
     * @return List of seats in the specified hall
     */
    List<SeatModel> findByHallId(int hallId);
    
    /**
     * Find available seats for a specific show
     * 
     * @param showId the ID of the show
     * @return List of available seats for the show
     */
    List<SeatModel> findAvailableByShowId(int showId);
    
    /**
     * Find seats by type in a specific hall
     * 
     * @param hallId the ID of the hall
     * @param seatType the type of seats to find
     * @return List of seats with the specified type in the hall
     */
    List<SeatModel> findByHallIdAndSeatType(int hallId, String seatType);
    
    /**
     * Update seat availability
     * 
     * @param seatId the ID of the seat
     * @param isAvailable the new availability status
     * @return true if successful, false otherwise
     */
    boolean updateAvailability(int seatId, boolean isAvailable);
    
    /**
     * Book seats for a show
     * 
     * @param showId the ID of the show
     * @param seatIds list of seat IDs to book
     * @return true if all seats were booked successfully, false otherwise
     */
    boolean bookSeats(int showId, List<Integer> seatIds);
}
