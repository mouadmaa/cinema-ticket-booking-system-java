package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.TicketModel;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for TicketModel
 * Provides methods to interact with ticket data in the database
 */
public interface TicketModelDao {
    
    /**
     * Save a new ticket to the database
     * 
     * @param ticket the ticket to save
     * @return the saved ticket with generated ID
     */
    TicketModel save(TicketModel ticket);
    
    /**
     * Update an existing ticket in the database
     * 
     * @param ticket the ticket to update
     * @return true if successful, false otherwise
     */
    boolean update(TicketModel ticket);
    
    /**
     * Delete a ticket from the database by ID
     * 
     * @param id the ID of the ticket to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Find a ticket by ID
     * 
     * @param id the ID of the ticket to find
     * @return Optional containing the ticket if found, empty optional otherwise
     */
    Optional<TicketModel> findById(int id);
    
    /**
     * Find tickets by booking ID
     * 
     * @param bookingId the ID of the booking
     * @return List of tickets for the booking
     */
    List<TicketModel> findByBookingId(int bookingId);
    
    /**
     * Find tickets by show ID
     * 
     * @param showId the ID of the show
     * @return List of tickets for the show
     */
    List<TicketModel> findByShowId(int showId);
    
    /**
     * Find tickets by client ID
     * 
     * @param clientId the ID of the client
     * @return List of tickets for the client
     */
    List<TicketModel> findByClientId(int clientId);
    
    /**
     * Find a ticket by seat ID and show ID
     * 
     * @param seatId the ID of the seat
     * @param showId the ID of the show
     * @return Optional containing the ticket if found, empty optional otherwise
     */
    Optional<TicketModel> findBySeatIdAndShowId(int seatId, int showId);
    
    /**
     * Find all tickets
     * 
     * @return List of all tickets
     */
    List<TicketModel> findAll();
    
    /**
     * Cancel a ticket
     * 
     * @param ticketId the ID of the ticket
     * @return true if successful, false otherwise
     */
    boolean cancelTicket(int ticketId);
    
    /**
     * Generate a ticket QR code
     * 
     * @param ticketId the ID of the ticket
     * @return String containing the QR code data
     */
    String generateTicketQRCode(int ticketId);
    
    /**
     * Validate a ticket
     * 
     * @param ticketCode the ticket code or QR code data
     * @return true if ticket is valid, false otherwise
     */
    boolean validateTicket(String ticketCode);
}
