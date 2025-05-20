package com.example.cinema_ticket_booking_system.models;

import javafx.beans.property.*;

public class SeatModel {
    private final IntegerProperty id;
    private final IntegerProperty hallId;
    private final StringProperty seatNumber;
    private final StringProperty seatType;
    private final BooleanProperty isAvailable;
    private final StringProperty hallName;

    public SeatModel(int id, int hallId, String seatNumber, String seatType, boolean isAvailable, String hallName) {
        this.id = new SimpleIntegerProperty(id);
        this.hallId = new SimpleIntegerProperty(hallId);
        this.seatNumber = new SimpleStringProperty(seatNumber);
        this.seatType = new SimpleStringProperty(seatType);
        this.isAvailable = new SimpleBooleanProperty(isAvailable);
        this.hallName = new SimpleStringProperty(hallName);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getHallId() {
        return hallId.get();
    }

    public IntegerProperty hallIdProperty() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId.set(hallId);
    }

    public String getSeatNumber() {
        return seatNumber.get();
    }

    public StringProperty seatNumberProperty() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber.set(seatNumber);
    }

    public String getSeatType() {
        return seatType.get();
    }

    public StringProperty seatTypeProperty() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType.set(seatType);
    }

    public boolean getIsAvailable() {
        return isAvailable.get();
    }

    public BooleanProperty isAvailableProperty() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable.set(isAvailable);
    }

    public String getHallName() {
        return hallName.get();
    }

    public StringProperty hallNameProperty() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName.set(hallName);
    }
}
