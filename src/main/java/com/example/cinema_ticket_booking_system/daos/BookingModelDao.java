package com.example.cinema_ticket_booking_system.daos;

import com.example.cinema_ticket_booking_system.models.BookingModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingModelDao {
    
    BookingModel save(BookingModel booking);
    
    boolean update(BookingModel booking);
    
    boolean delete(int id);
    
    Optional<BookingModel> findById(int id);
    
    List<BookingModel> findByClientId(int clientId);
    
    List<BookingModel> findByShowId(int showId);
    
    List<BookingModel> findByBookingDate(LocalDate bookingDate);
    
    List<BookingModel> findByStatus(String status);
    
    List<BookingModel> findAll();
    
    boolean updateStatus(int bookingId, String status);
    
    boolean cancelBooking(int bookingId);
    
    List<BookingModel> findByBookingDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
