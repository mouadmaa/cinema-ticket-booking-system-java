package com.example.cinema_ticket_booking_system.dao;

import com.example.cinema_ticket_booking_system.models.PaymentModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for PaymentModel
 * Provides methods to interact with payment data in the database
 */
public interface PaymentModelDao {
    
    /**
     * Save a new payment to the database
     * 
     * @param payment the payment to save
     * @return the saved payment with generated ID
     */
    PaymentModel save(PaymentModel payment);
    
    /**
     * Update an existing payment in the database
     * 
     * @param payment the payment to update
     * @return true if successful, false otherwise
     */
    boolean update(PaymentModel payment);
    
    /**
     * Delete a payment from the database by ID
     * 
     * @param id the ID of the payment to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Find a payment by ID
     * 
     * @param id the ID of the payment to find
     * @return Optional containing the payment if found, empty optional otherwise
     */
    Optional<PaymentModel> findById(int id);
    
    /**
     * Find payments by booking ID
     * 
     * @param bookingId the ID of the booking
     * @return List of payments for the booking
     */
    List<PaymentModel> findByBookingId(int bookingId);
    
    /**
     * Find payments by status
     * 
     * @param status the payment status
     * @return List of payments with the specified status
     */
    List<PaymentModel> findByStatus(String status);
    
    /**
     * Find payments by payment method
     * 
     * @param paymentMethod the payment method
     * @return List of payments with the specified payment method
     */
    List<PaymentModel> findByPaymentMethod(String paymentMethod);
    
    /**
     * Find payments by date range
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return List of payments made between the specified dates
     */
    List<PaymentModel> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find payments by client ID
     * 
     * @param clientId the ID of the client
     * @return List of payments made by the client
     */
    List<PaymentModel> findByClientId(int clientId);
    
    /**
     * Find all payments
     * 
     * @return List of all payments
     */
    List<PaymentModel> findAll();
    
    /**
     * Update payment status
     * 
     * @param paymentId the ID of the payment
     * @param status the new status
     * @return true if successful, false otherwise
     */
    boolean updateStatus(int paymentId, String status);
    
    /**
     * Process a refund
     * 
     * @param paymentId the ID of the payment
     * @param amount the refund amount
     * @return true if successful, false otherwise
     */
    boolean processRefund(int paymentId, BigDecimal amount);
    
    /**
     * Calculate daily revenue
     * 
     * @param date the date to calculate revenue for
     * @return the total revenue for the specified date
     */
    BigDecimal calculateDailyRevenue(LocalDate date);
    
    /**
     * Calculate revenue for date range
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return the total revenue for the specified date range
     */
    BigDecimal calculateRevenueForDateRange(LocalDate startDate, LocalDate endDate);
}
