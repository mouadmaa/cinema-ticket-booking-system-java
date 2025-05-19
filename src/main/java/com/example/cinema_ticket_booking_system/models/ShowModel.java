package com.example.cinema_ticket_booking_system.models;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShowModel {
    private final IntegerProperty id;
    private final IntegerProperty movieId;
    private final IntegerProperty hallId;
    private final ObjectProperty<LocalDate> showDate;
    private final ObjectProperty<LocalTime> showTime;
    private final DoubleProperty ticketPrice;
    
    // For display purposes
    private final StringProperty movieName;
    private final StringProperty hallName;

    public ShowModel(int id, int movieId, int hallId, LocalDate showDate, LocalTime showTime, 
                     double ticketPrice, String movieName, String hallName) {
        this.id = new SimpleIntegerProperty(id);
        this.movieId = new SimpleIntegerProperty(movieId);
        this.hallId = new SimpleIntegerProperty(hallId);
        this.showDate = new SimpleObjectProperty<>(showDate);
        this.showTime = new SimpleObjectProperty<>(showTime);
        this.ticketPrice = new SimpleDoubleProperty(ticketPrice);
        this.movieName = new SimpleStringProperty(movieName);
        this.hallName = new SimpleStringProperty(hallName);
    }

    // ID property methods
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Movie ID property methods
    public int getMovieId() {
        return movieId.get();
    }

    public IntegerProperty movieIdProperty() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId.set(movieId);
    }

    // Hall ID property methods
    public int getHallId() {
        return hallId.get();
    }

    public IntegerProperty hallIdProperty() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId.set(hallId);
    }

    // Show date property methods
    public LocalDate getShowDate() {
        return showDate.get();
    }

    public ObjectProperty<LocalDate> showDateProperty() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate.set(showDate);
    }

    // Show time property methods
    public LocalTime getShowTime() {
        return showTime.get();
    }

    public ObjectProperty<LocalTime> showTimeProperty() {
        return showTime;
    }

    public void setShowTime(LocalTime showTime) {
        this.showTime.set(showTime);
    }

    // Ticket price property methods
    public double getTicketPrice() {
        return ticketPrice.get();
    }

    public DoubleProperty ticketPriceProperty() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice.set(ticketPrice);
    }

    // Movie name property methods (for display)
    public String getMovieName() {
        return movieName.get();
    }

    public StringProperty movieNameProperty() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName.set(movieName);
    }

    // Hall name property methods (for display)
    public String getHallName() {
        return hallName.get();
    }

    public StringProperty hallNameProperty() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName.set(hallName);
    }
    
    // Add availability property to support the form
    private final IntegerProperty availability = new SimpleIntegerProperty(100);
    
    public int getAvailability() {
        return availability.get();
    }
    
    public IntegerProperty availabilityProperty() {
        return availability;
    }
    
    public void setAvailability(int availability) {
        this.availability.set(availability);
    }
}
