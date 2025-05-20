package com.example.cinema_ticket_booking_system.daos;

import com.example.cinema_ticket_booking_system.models.HallModel;
import java.util.List;
import java.util.Optional;

public interface HallModelDao {
    
    HallModel save(HallModel hall);
    
    boolean update(HallModel hall);
    
    boolean delete(int id);
    
    Optional<HallModel> findById(int id);
    
    Optional<HallModel> findByName(String name);
    
    List<HallModel> findAll();
    
    List<HallModel> findByMinCapacity(int minCapacity);
    
    int getTotalSeatsCount(int hallId);
    
    boolean isHallAvailable(int hallId, String date, String startTime, int duration);
}
