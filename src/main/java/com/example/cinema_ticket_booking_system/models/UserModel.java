package com.example.cinema_ticket_booking_system.models;

import javafx.beans.property.*;

public class UserModel {
    private final IntegerProperty id;
    private final StringProperty role;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty username;
    private final StringProperty email;
    private final StringProperty phoneNumber;

    public UserModel(int id, String role, String firstName, String lastName, String username, 
                     String email, String phoneNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.role = new SimpleStringProperty(role);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber != null ? phoneNumber : "");
    }

    // Property accessors
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty roleProperty() {
        return role;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    // Standard getters
    public int getId() {
        return id.get();
    }

    public String getRole() {
        return role.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }
}
