DROP TABLE IF EXISTS Tickets;

CREATE TABLE Tickets (
     id            SERIAL PRIMARY KEY,
     booking_id    INTEGER      NOT NULL,
     show_id       INTEGER      NOT NULL,
     seat_id       INTEGER      NOT NULL,
     status        VARCHAR(20)  NOT NULL CHECK (status IN ('Pending', 'Active', 'Cancelled')),
     ticket_code   VARCHAR(20)  NOT NULL UNIQUE,
     issue_date    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
     price         DECIMAL(6,2) NOT NULL,
     CONSTRAINT fk_booking FOREIGN KEY (booking_id) REFERENCES Bookings(id) ON DELETE CASCADE,
     CONSTRAINT fk_show FOREIGN KEY (show_id) REFERENCES Shows(id) ON DELETE CASCADE,
     CONSTRAINT fk_seat FOREIGN KEY (seat_id) REFERENCES Seats(id) ON DELETE CASCADE
);

ALTER TABLE Tickets
    ADD CONSTRAINT check_ticket_code CHECK (ticket_code ~ '^[A-Z0-9]{8}$');

INSERT INTO Tickets (booking_id, show_id, seat_id, status, ticket_code, issue_date, price)
VALUES
    (1, 1, 1, 'Active', 'TCKT1234', '2025-05-20 10:05:00', 120.50),
    (2, 2, 6, 'Pending', 'TCKT5678', '2025-05-20 12:35:00', 250.00),
    (3, 3, 11, 'Active', 'TCKT9012', '2025-05-21 09:20:00', 130.00),
    (4, 4, 14, 'Cancelled', 'TCKT3456', '2025-05-21 14:50:00', 200.00),
    (5, 5, 5, 'Active', 'TCKT7890', '2025-05-22 11:25:00', 300.00),
    (6, 6, 7, 'Pending', 'TCKT2345', '2025-05-22 15:15:00', 340.50),
    (7, 7, 8, 'Active', 'TCKT6789', '2025-05-23 08:35:00', 220.00),
    (8, 8, 15, 'Cancelled', 'TCKT0123', '2025-05-23 13:05:00', 180.00),
    (9, 9, 2, 'Active', 'TCKT4567', '2025-05-24 10:50:00', 210.50),
    (10, 10, 9, 'Pending', 'TCKT8901', '2025-05-24 16:25:00', 160.00),
    (11, 1, 3, 'Active', 'TCKT1111', '2025-05-20 11:05:00', 120.50),
    (12, 2, 10, 'Active', 'TCKT2222', '2025-05-20 14:20:00', 150.00),
    (13, 3, 12, 'Pending', 'TCKT3333', '2025-05-21 10:35:00', 330.00),
    (14, 4, 16, 'Active', 'TCKT4444', '2025-05-21 16:05:00', 200.00),
    (15, 5, 4, 'Cancelled', 'TCKT5555', '2025-05-22 12:50:00', 100.00);
