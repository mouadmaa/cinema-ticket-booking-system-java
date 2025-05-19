package com.example.cinema_ticket_booking_system.models;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketModel {
    private final IntegerProperty id;
    private final IntegerProperty bookingId;
    private final StringProperty status;
    private final StringProperty ticketCode;
    private final ObjectProperty<LocalDateTime> issueDate;
    private final ObjectProperty<BigDecimal> price;
    
    // Additional fields for display purposes
    private final StringProperty clientName;
    private final StringProperty showInfo;
    private final StringProperty seatInfo;

    public TicketModel(int id, int bookingId, String status, String ticketCode, 
                       LocalDateTime issueDate, BigDecimal price,
                       String clientName, String showInfo, String seatInfo) {
        this.id = new SimpleIntegerProperty(id);
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.status = new SimpleStringProperty(status);
        this.ticketCode = new SimpleStringProperty(ticketCode);
        this.issueDate = new SimpleObjectProperty<>(issueDate);
        this.price = new SimpleObjectProperty<>(price);
        this.clientName = new SimpleStringProperty(clientName);
        this.showInfo = new SimpleStringProperty(showInfo);
        this.seatInfo = new SimpleStringProperty(seatInfo);
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

    public StringProperty ticketCodeProperty() {
        return ticketCode;
    }

    public ObjectProperty<LocalDateTime> issueDateProperty() {
        return issueDate;
    }

    public ObjectProperty<BigDecimal> priceProperty() {
        return price;
    }
    
    public StringProperty clientNameProperty() {
        return clientName;
    }
    
    public StringProperty showInfoProperty() {
        return showInfo;
    }
    
    public StringProperty seatInfoProperty() {
        return seatInfo;
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
    
    public String getTicketCode() {
        return ticketCode.get();
    }
    
    public LocalDateTime getIssueDate() {
        return issueDate.get();
    }
    
    public BigDecimal getPrice() {
        return price.get();
    }
    
    public String getClientName() {
        return clientName.get();
    }
    
    public String getShowInfo() {
        return showInfo.get();
    }
    
    public String getSeatInfo() {
        return seatInfo.get();
    }
}
