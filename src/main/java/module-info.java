module com.example.cinema_ticket_booking_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.cinema_ticket_booking_system to javafx.fxml;
    opens com.example.cinema_ticket_booking_system.controllers to javafx.fxml;
    
    exports com.example.cinema_ticket_booking_system;
    exports com.example.cinema_ticket_booking_system.controllers;
}