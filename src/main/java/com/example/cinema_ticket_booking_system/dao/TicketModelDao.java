package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.TicketModel;
import java.util.List;
import java.util.Optional;

public interface TicketModelDao {
    
    TicketModel save(TicketModel ticket);
    
    boolean update(TicketModel ticket);
    
    boolean delete(int id);
    
    Optional<TicketModel> findById(int id);
    
    List<TicketModel> findByBookingId(int bookingId);
    
    List<TicketModel> findByShowId(int showId);
    
    List<TicketModel> findByClientId(int clientId);
    
    Optional<TicketModel> findBySeatIdAndShowId(int seatId, int showId);
    
    List<TicketModel> findAll();
    
    boolean cancelTicket(int ticketId);
    
    String generateTicketQRCode(int ticketId);
    
    boolean validateTicket(String ticketCode);
}
