package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.ShowModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for ShowModel
 * Provides methods to interact with movie show/screening data in the database
 */
public interface ShowModelDao {
    
    /**
     * Save a new show to the database
     * 
     * @param show the show to save
     * @return the saved show with generated ID
     */
    ShowModel save(ShowModel show);
    
    /**
     * Update an existing show in the database
     * 
     * @param show the show to update
     * @return true if successful, false otherwise
     */
    boolean update(ShowModel show);
    
    /**
     * Delete a show from the database by ID
     * 
     * @param id the ID of the show to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Find a show by ID
     * 
     * @param id the ID of the show to find
     * @return Optional containing the show if found, empty optional otherwise
     */
    Optional<ShowModel> findById(int id);
    
    /**
     * Find all shows for a specific movie
     * 
     * @param movieId the ID of the movie
     * @return List of shows for the movie
     */
    List<ShowModel> findByMovieId(int movieId);
    
    /**
     * Find all shows in a specific hall
     * 
     * @param hallId the ID of the hall
     * @return List of shows in the hall
     */
    List<ShowModel> findByHallId(int hallId);
    
    /**
     * Find all shows on a specific date
     * 
     * @param date the date to search for
     * @return List of shows on the specified date
     */
    List<ShowModel> findByDate(LocalDate date);
    
    /**
     * Find all shows between two dates
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return List of shows between the specified dates
     */
    List<ShowModel> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find shows with available seats
     * 
     * @param minAvailability minimum number of available seats
     * @return List of shows with at least the specified number of available seats
     */
    List<ShowModel> findWithAvailableSeats(int minAvailability);
    
    /**
     * Find all shows
     * 
     * @return List of all shows
     */
    List<ShowModel> findAll();
    
    /**
     * Update show availability
     * 
     * @param showId the ID of the show
     * @param availability the new availability count
     * @return true if successful, false otherwise
     */
    boolean updateAvailability(int showId, int availability);
    
    /**
     * Check if a hall is available for a new show
     * 
     * @param hallId the ID of the hall
     * @param date the date of the show
     * @param startTime the start time of the show
     * @param endTime the end time of the show
     * @return true if the hall is available, false otherwise
     */
    boolean isHallAvailable(int hallId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
