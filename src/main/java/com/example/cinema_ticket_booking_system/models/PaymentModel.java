package com.example.cinema_ticket_booking_system.models;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentModel {
    private final IntegerProperty id;
    private final IntegerProperty bookingId;
    private final StringProperty status;
    private final ObjectProperty<BigDecimal> amount;
    private final StringProperty paymentMethod;
    private final ObjectProperty<LocalDateTime> paymentDate;
    
    // Additional fields for display purposes
    private final StringProperty clientName;
    private final StringProperty showInfo;

    public PaymentModel(int id, int bookingId, String status, BigDecimal amount, 
                       String paymentMethod, LocalDateTime paymentDate,
                       String clientName, String showInfo) {
        this.id = new SimpleIntegerProperty(id);
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.status = new SimpleStringProperty(status);
        this.amount = new SimpleObjectProperty<>(amount);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.paymentDate = new SimpleObjectProperty<>(paymentDate);
        this.clientName = new SimpleStringProperty(clientName);
        this.showInfo = new SimpleStringProperty(showInfo);
    }

    // Property accessors
    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty bookingIdProperty() {
        return bookingId;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public ObjectProperty<BigDecimal> amountProperty() {
        return amount;
    }

    public StringProperty paymentMethodProperty() {
        return paymentMethod;
    }

    public ObjectProperty<LocalDateTime> paymentDateProperty() {
        return paymentDate;
    }
    
    public StringProperty clientNameProperty() {
        return clientName;
    }
    
    public StringProperty showInfoProperty() {
        return showInfo;
    }
    
    // Standard getters
    public int getId() {
        return id.get();
    }
    
    public int getBookingId() {
        return bookingId.get();
    }
    
    public String getStatus() {
        return status.get();
    }
    
    public BigDecimal getAmount() {
        return amount.get();
    }
    
    public String getPaymentMethod() {
        return paymentMethod.get();
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate.get();
    }
    
    public String getClientName() {
        return clientName.get();
    }
    
    public String getShowInfo() {
        return showInfo.get();
    }
}
