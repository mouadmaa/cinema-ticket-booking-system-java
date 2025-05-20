DROP TABLE IF EXISTS Shows;

CREATE TABLE Shows (
       id            SERIAL PRIMARY KEY,
       movie_id      INTEGER      NOT NULL,
       hall_id       INTEGER      NOT NULL,
       show_date     DATE         NOT NULL,
       show_time     TIME         NOT NULL,
       ticket_price  DECIMAL(6,2) NOT NULL,
       CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES Movies(id) ON DELETE CASCADE,
       CONSTRAINT fk_hall FOREIGN KEY (hall_id) REFERENCES Halls(id) ON DELETE CASCADE
);

ALTER TABLE Shows
    ADD CONSTRAINT check_show_date CHECK (show_date >= CURRENT_DATE),
    ADD CONSTRAINT check_ticket_price CHECK (ticket_price > 5.00);

INSERT INTO Shows (movie_id, hall_id, show_date, show_time, ticket_price)
VALUES
    (1, 1, '2025-05-20', '18:00:00', 120.50),
    (2, 2, '2025-05-20', '20:30:00', 250.00),
    (3, 3, '2025-05-21', '19:00:00', 330.00),
    (4, 4, '2025-05-21', '17:30:00', 200.00),
    (5, 1, '2025-05-22', '21:00:00', 200.00),
    (6, 2, '2025-05-22', '16:00:00', 140.50),
    (7, 3, '2025-05-23', '18:30:00', 220.00),
    (8, 4, '2025-05-23', '20:00:00', 380.00),
    (9, 1, '2025-05-24', '19:30:00', 110.50),
    (10, 2, '2025-05-24', '17:00:00', 160.00),
    (10, 2, '2025-05-24', '17:00:00', 160.00),
    (11, 3, '2025-05-20', '18:00:00', 120.50),
    (12, 4, '2025-05-20', '20:30:00', 250.00),
    (13, 1, '2025-05-21', '19:00:00', 330.00),
    (14, 2, '2025-05-21', '17:30:00', 200.00),
    (15, 3, '2025-05-22', '21:00:00', 200.00);
