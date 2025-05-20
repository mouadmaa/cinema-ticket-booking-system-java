package com.example.cinema_ticket_booking_system.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserModelTest {

    @Test
    public void testUserModelConstructorAndGetters() {
        // Arrange
        int id = 1;
        String role = "ADMIN";
        String firstName = "Mohamed";
        String lastName = "El Alaoui";
        String username = "malaoui";
        String email = "mohamed.elalaoui@gmail.com";
        String phoneNumber = "0661234567";
        String password = "password123";

        // Act
        UserModel userModel = new UserModel(
                id, role, firstName, lastName,
                username, email, phoneNumber, password
        );

        // Assert
        assertEquals(id, userModel.getId());
        assertEquals(role, userModel.getRole());
        assertEquals(firstName, userModel.getFirstName());
        assertEquals(lastName, userModel.getLastName());
        assertEquals(username, userModel.getUsername());
        assertEquals(email, userModel.getEmail());
        assertEquals(phoneNumber, userModel.getPhoneNumber());
        assertEquals(password, userModel.getPassword());
    }

    @Test
    public void testUserModelProperties() {
        // Arrange
        int id = 2;
        String role = "USER";
        String firstName = "Fatima";
        String lastName = "Benali";
        String username = "fbenali";
        String email = "fatima.benali@gmail.com";
        String phoneNumber = "0777654321";
        String password = "securePass456";

        // Act
        UserModel userModel = new UserModel(
                id, role, firstName, lastName,
                username, email, phoneNumber, password
        );

        // Assert
        assertEquals(id, userModel.idProperty().get());
        assertEquals(role, userModel.roleProperty().get());
        assertEquals(firstName, userModel.firstNameProperty().get());
        assertEquals(lastName, userModel.lastNameProperty().get());
        assertEquals(username, userModel.usernameProperty().get());
        assertEquals(email, userModel.emailProperty().get());
        assertEquals(phoneNumber, userModel.phoneNumberProperty().get());
        assertEquals(password, userModel.passwordProperty().get());
    }

    @Test
    public void testUserModelWithSpecificData() {
        // Arrange
        int id = 5;
        String role = "MANAGER";
        String firstName = "Karim";
        String lastName = "El Idrissi";
        String username = "kelidrissi";
        String email = "karim.elidrissi@menara.ma";
        String phoneNumber = "0522987654"; // Casablanca area code
        String password = "Atlas@2023";

        // Act
        UserModel userModel = new UserModel(
                id, role, firstName, lastName,
                username, email, phoneNumber, password
        );

        // Assert
        assertEquals(id, userModel.getId());
        assertEquals(role, userModel.getRole());
        assertEquals(firstName, userModel.getFirstName());
        assertEquals(lastName, userModel.getLastName());
        assertEquals(username, userModel.getUsername());
        assertEquals(email, userModel.getEmail());
        assertEquals(phoneNumber, userModel.getPhoneNumber());
        assertEquals(password, userModel.getPassword());
    }

    @Test
    public void testUserModelWithEmptyValues() {
        // Arrange
        int id = 4;
        String role = "";
        String firstName = "";
        String lastName = "";
        String username = "";
        String email = "";
        String phoneNumber = "";
        String password = "";

        // Act
        UserModel userModel = new UserModel(
            id, role, firstName, lastName, 
            username, email, phoneNumber, password
        );

        // Assert
        assertEquals(id, userModel.getId());
        assertEquals("", userModel.getRole());
        assertEquals("", userModel.getFirstName());
        assertEquals("", userModel.getLastName());
        assertEquals("", userModel.getUsername());
        assertEquals("", userModel.getEmail());
        assertEquals("", userModel.getPhoneNumber());
        assertEquals("", userModel.getPassword());
    }
}
