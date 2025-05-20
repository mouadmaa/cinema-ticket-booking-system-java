package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.ShowModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ShowModelDao {
    
    ShowModel save(ShowModel show);
    
    boolean update(ShowModel show);
    
    boolean delete(int id);
    
    Optional<ShowModel> findById(int id);
    
    List<ShowModel> findByMovieId(int movieId);
    
    List<ShowModel> findByHallId(int hallId);
    
    List<ShowModel> findByDate(LocalDate date);
    
    List<ShowModel> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    List<ShowModel> findWithAvailableSeats(int minAvailability);
    
    List<ShowModel> findAll();
    
    boolean updateAvailability(int showId, int availability);
    
    boolean isHallAvailable(int hallId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
