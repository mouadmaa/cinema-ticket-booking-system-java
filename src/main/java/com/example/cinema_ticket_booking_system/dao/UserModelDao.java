package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.UserModel;
import java.util.List;
import java.util.Optional;

public interface UserModelDao {
    
    UserModel save(UserModel user);
    
    boolean update(UserModel user);
    
    boolean delete(int id);
    
    Optional<UserModel> findById(int id);
    
    Optional<UserModel> findByUsername(String username);
    
    List<UserModel> findAll();
    
    List<UserModel> findByRole(String role);
    
    Optional<UserModel> authenticate(String username, String password);
}
