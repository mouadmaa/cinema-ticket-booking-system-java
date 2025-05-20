DROP TABLE IF EXISTS Bookings;

CREATE TABLE Bookings (
      id            SERIAL PRIMARY KEY,
      client_id     INTEGER      NOT NULL,
      show_id       INTEGER      NOT NULL,
      seat_id       INTEGER      NOT NULL,
      status        VARCHAR(20)  NOT NULL CHECK (status IN ('Confirmed', 'Pending', 'Cancelled')),
      booking_date  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
      CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES Clients(id) ON DELETE CASCADE,
      CONSTRAINT fk_show FOREIGN KEY (show_id) REFERENCES Shows(id) ON DELETE CASCADE,
      CONSTRAINT fk_seat FOREIGN KEY (seat_id) REFERENCES Seats(id) ON DELETE CASCADE,
      CONSTRAINT unique_show_seat UNIQUE (show_id, seat_id)
);

INSERT INTO Bookings (client_id, show_id, seat_id, booking_date, status)
VALUES
    (1, 1, 1, '2025-05-20 10:00:00', 'Confirmed'),
    (2, 2, 6, '2025-05-20 12:30:00', 'Pending'),
    (3, 3, 11, '2025-05-21 09:15:00', 'Confirmed'),
    (4, 4, 14, '2025-05-21 14:45:00', 'Cancelled'),
    (5, 5, 5, '2025-05-22 11:20:00', 'Confirmed'),
    (1, 6, 7, '2025-05-22 15:10:00', 'Pending'),
    (2, 7, 8, '2025-05-23 08:30:00', 'Confirmed'),
    (3, 8, 15, '2025-05-23 13:00:00', 'Cancelled'),
    (4, 9, 2, '2025-05-24 10:45:00', 'Confirmed'),
    (5, 10, 9, '2025-05-24 16:20:00', 'Pending'),
    (1, 1, 3, '2025-05-20 11:00:00', 'Confirmed'),
    (2, 2, 10, '2025-05-20 14:15:00', 'Confirmed'),
    (3, 3, 12, '2025-05-21 10:30:00', 'Pending'),
    (4, 4, 16, '2025-05-21 16:00:00', 'Confirmed'),
    (5, 5, 4, '2025-05-22 12:45:00', 'Cancelled');

