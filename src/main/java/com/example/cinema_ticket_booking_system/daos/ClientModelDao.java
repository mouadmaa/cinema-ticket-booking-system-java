package com.example.cinema_ticket_booking_system.daos;

import com.example.cinema_ticket_booking_system.models.ClientModel;
import java.util.List;
import java.util.Optional;

public interface ClientModelDao {
    
    ClientModel save(ClientModel client);
    
    boolean update(ClientModel client);
    
    boolean delete(int id);
    
    Optional<ClientModel> findById(int id);
    
    Optional<ClientModel> findByEmail(String email);
    
    List<ClientModel> findByLastName(String lastName);
    
    Optional<ClientModel> findByPhoneNumber(String phoneNumber);
    
    List<ClientModel> findAll();
    
    Optional<ClientModel> authenticate(String email, String password);
    
    boolean changePassword(int clientId, String newPassword);
}
