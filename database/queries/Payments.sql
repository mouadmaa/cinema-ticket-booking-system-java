DROP TABLE IF EXISTS Payments;

CREATE TABLE Payments (
      id            SERIAL PRIMARY KEY,
      booking_id    INTEGER      NOT NULL,
      status        VARCHAR(20)  NOT NULL CHECK (status IN ('Completed', 'Pending', 'Failed')),
      amount        DECIMAL(6,2) NOT NULL CHECK (amount >= 5.00 AND amount <= 50.00),
      payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('Credit Card', 'Debit Card', 'Cash', 'Online')),
      payment_date  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
      CONSTRAINT fk_booking FOREIGN KEY (booking_id) REFERENCES Bookings(id) ON DELETE CASCADE
);

INSERT INTO Payments (booking_id, status, amount, payment_method, payment_date)
VALUES
    (1,  'Completed', 12.50,'Credit Card', '2025-05-20 10:10:00'),
    (2,  'Pending', 15.00, 'Online', '2025-05-20 12:40:00'),
    (3,  'Completed', 13.00,'Debit Card', '2025-05-21 09:25:00'),
    (4,  'Failed', 20.00, 'Cash', '2025-05-21 14:55:00'),
    (5,  'Completed', 10.00,'Credit Card', '2025-05-22 11:30:00'),
    (6,  'Pending', 14.50, 'Online', '2025-05-22 15:20:00'),
    (7,  'Completed', 12.00,'Debit Card', '2025-05-23 08:40:00'),
    (8,  'Failed', 18.00, 'Cash', '2025-05-23 13:10:00'),
    (9,  'Completed', 11.50,'Credit Card', '2025-05-24 10:55:00'),
    (10,  'Pending',  16.00, 'Online', '2025-05-24 16:30:00'),
    (11,  'Completed',  12.50,'Debit Card', '2025-05-20 11:10:00'),
    (12,  'Completed',  15.00,'Credit Card', '2025-05-20 14:25:00'),
    (13,  'Pending',  13.00, 'Cash', '2025-05-21 10:40:00'),
    (14,  'Completed',  20.00, 'Online', '2025-05-21 16:10:00'),
    (15,  'Failed',  10.00,'Debit Card', '2025-05-22 12:55:00');
