package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.SeatModel;
import java.util.List;
import java.util.Optional;

public interface SeatModelDao {
    
    SeatModel save(SeatModel seat);
    
    boolean update(SeatModel seat);
    
    boolean delete(int id);
    
    Optional<SeatModel> findById(int id);
    
    Optional<SeatModel> findBySeatNumberAndHallId(String seatNumber, int hallId);
    
    List<SeatModel> findAll();
    
    List<SeatModel> findByHallId(int hallId);
    
    List<SeatModel> findAvailableByShowId(int showId);
    
    List<SeatModel> findByHallIdAndSeatType(int hallId, String seatType);
    
    boolean updateAvailability(int seatId, boolean isAvailable);
    
    boolean bookSeats(int showId, List<Integer> seatIds);
}
