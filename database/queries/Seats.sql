DROP TABLE IF EXISTS Seats;

CREATE TABLE Seats (
    id            SERIAL PRIMARY KEY,
    hall_id       INTEGER      NOT NULL,
    seat_number   VARCHAR(10)  NOT NULL,
    seat_type     VARCHAR(20)  NOT NULL CHECK (seat_type IN ('Standard', 'Premium', 'Accessible')),
    is_available  BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_hall FOREIGN KEY (hall_id) REFERENCES Halls(id) ON DELETE CASCADE
);

ALTER TABLE Seats
    ADD CONSTRAINT check_seat_number CHECK (seat_number ~ '^[A-Z][1-9][0-9]?$'),
    ADD CONSTRAINT unique_hall_seat UNIQUE (hall_id, seat_number);

INSERT INTO Seats (hall_id, seat_number, seat_type, is_available)
VALUES
    (1, 'A1', 'Standard', TRUE),
    (1, 'A2', 'Standard', TRUE),
    (1, 'B1', 'Premium', TRUE),
    (1, 'B2', 'Premium', FALSE),
    (1, 'C1', 'Accessible', TRUE),
    (2, 'A1', 'Standard', TRUE),
    (2, 'A2', 'Standard', TRUE),
    (2, 'B1', 'Premium', TRUE),
    (2, 'B2', 'Premium', TRUE),
    (2, 'C1', 'Accessible', TRUE),
    (3, 'A1', 'Standard', TRUE),
    (3, 'A2', 'Standard', FALSE),
    (3, 'B1', 'Premium', TRUE),
    (3, 'B2', 'Premium', TRUE),
    (4, 'A1', 'Standard', TRUE),
    (4, 'A2', 'Standard', TRUE),
    (4, 'B1', 'Premium', TRUE),
    (4, 'B2', 'Premium', FALSE),
    (4, 'C1', 'Accessible', TRUE),
    (4, 'C2', 'Accessible', TRUE);
