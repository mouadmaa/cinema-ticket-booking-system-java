package com.example.cinema_ticket_booking_system.models;

import javafx.beans.property.*;

public class ClientModel {
    private final IntegerProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty password;
    private final StringProperty phoneNumber;

    public ClientModel(int id, String firstName, String lastName, String email, 
                     String password, String phoneNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.phoneNumber = new SimpleStringProperty(phoneNumber != null ? phoneNumber : "");
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }
    
    public int getId() {
        return id.get();
    }
    
    public String getFirstName() {
        return firstName.get();
    }
    
    public String getLastName() {
        return lastName.get();
    }
    
    public String getEmail() {
        return email.get();
    }
    
    public String getPassword() {
        return password.get();
    }
    
    public String getPhoneNumber() {
        return phoneNumber.get();
    }
}
