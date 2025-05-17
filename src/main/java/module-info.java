module com.example.cinema_ticket_booking_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires com.jfoenix;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.example.cinema_ticket_booking_system to javafx.base, javafx.fxml;
    opens com.example.cinema_ticket_booking_system.controllers to javafx.fxml;
    opens com.example.cinema_ticket_booking_system.models to javafx.base;

    exports com.example.cinema_ticket_booking_system;
    exports com.example.cinema_ticket_booking_system.controllers;
    exports com.example.cinema_ticket_booking_system.models;
}