package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.BookingModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for BookingModel
 * Provides methods to interact with booking data in the database
 */
public interface BookingModelDao {
    
    /**
     * Save a new booking to the database
     * 
     * @param booking the booking to save
     * @return the saved booking with generated ID
     */
    BookingModel save(BookingModel booking);
    
    /**
     * Update an existing booking in the database
     * 
     * @param booking the booking to update
     * @return true if successful, false otherwise
     */
    boolean update(BookingModel booking);
    
    /**
     * Delete a booking from the database by ID
     * 
     * @param id the ID of the booking to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Find a booking by ID
     * 
     * @param id the ID of the booking to find
     * @return Optional containing the booking if found, empty optional otherwise
     */
    Optional<BookingModel> findById(int id);
    
    /**
     * Find all bookings for a specific client
     * 
     * @param clientId the ID of the client
     * @return List of bookings for the client
     */
    List<BookingModel> findByClientId(int clientId);
    
    /**
     * Find all bookings for a specific show
     * 
     * @param showId the ID of the show
     * @return List of bookings for the show
     */
    List<BookingModel> findByShowId(int showId);
    
    /**
     * Find all bookings created on a specific date
     * 
     * @param bookingDate the date when bookings were created
     * @return List of bookings created on the specified date
     */
    List<BookingModel> findByBookingDate(LocalDate bookingDate);
    
    /**
     * Find all bookings by status
     * 
     * @param status the booking status
     * @return List of bookings with the specified status
     */
    List<BookingModel> findByStatus(String status);
    
    /**
     * Find all bookings
     * 
     * @return List of all bookings
     */
    List<BookingModel> findAll();
    
    /**
     * Update booking status
     * 
     * @param bookingId the ID of the booking
     * @param status the new status
     * @return true if successful, false otherwise
     */
    boolean updateStatus(int bookingId, String status);
    
    /**
     * Cancel a booking
     * 
     * @param bookingId the ID of the booking
     * @return true if successful, false otherwise
     */
    boolean cancelBooking(int bookingId);
    
    /**
     * Find bookings created between two dates
     * 
     * @param startDate the start date and time
     * @param endDate the end date and time
     * @return List of bookings created between the specified dates
     */
    List<BookingModel> findByBookingDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
