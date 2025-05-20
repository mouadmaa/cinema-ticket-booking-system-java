package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.PaymentModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentModelDao {
    
    PaymentModel save(PaymentModel payment);
    
    boolean update(PaymentModel payment);
    
    boolean delete(int id);
    
    Optional<PaymentModel> findById(int id);
    
    List<PaymentModel> findByBookingId(int bookingId);
    
    List<PaymentModel> findByStatus(String status);
    
    List<PaymentModel> findByPaymentMethod(String paymentMethod);
    
    List<PaymentModel> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<PaymentModel> findByClientId(int clientId);
    
    List<PaymentModel> findAll();
    
    boolean updateStatus(int paymentId, String status);
    
    boolean processRefund(int paymentId, BigDecimal amount);
    
    BigDecimal calculateDailyRevenue(LocalDate date);
    
    BigDecimal calculateRevenueForDateRange(LocalDate startDate, LocalDate endDate);
}
