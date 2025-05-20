package com.example.cinema_ticket_booking_system.models;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class BookingModel {
    private final IntegerProperty id;
    private final IntegerProperty clientId;
    private final IntegerProperty showId;
    private final IntegerProperty seatId;
    private final StringProperty status;
    private final ObjectProperty<LocalDateTime> bookingDate;
    private final StringProperty clientName;
    private final StringProperty showInfo;
    private final StringProperty seatInfo;

    public BookingModel(int id, int clientId, int showId, int seatId, 
                       String status, LocalDateTime bookingDate,
                       String clientName, String showInfo, String seatInfo) {
        this.id = new SimpleIntegerProperty(id);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.showId = new SimpleIntegerProperty(showId);
        this.seatId = new SimpleIntegerProperty(seatId);
        this.status = new SimpleStringProperty(status);
        this.bookingDate = new SimpleObjectProperty<>(bookingDate);
        this.clientName = new SimpleStringProperty(clientName);
        this.showInfo = new SimpleStringProperty(showInfo);
        this.seatInfo = new SimpleStringProperty(seatInfo);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public IntegerProperty showIdProperty() {
        return showId;
    }

    public IntegerProperty seatIdProperty() {
        return seatId;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public ObjectProperty<LocalDateTime> bookingDateProperty() {
        return bookingDate;
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

    public int getId() {
        return id.get();
    }
    
    public int getClientId() {
        return clientId.get();
    }
    
    public int getShowId() {
        return showId.get();
    }
    
    public int getSeatId() {
        return seatId.get();
    }
    
    public String getStatus() {
        return status.get();
    }
    
    public LocalDateTime getBookingDate() {
        return bookingDate.get();
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
