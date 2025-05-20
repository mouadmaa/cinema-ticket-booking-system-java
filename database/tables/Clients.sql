DROP TABLE IF EXISTS Clients;

CREATE TABLE Clients (
     id            SERIAL PRIMARY KEY,
     first_name    VARCHAR(50)  NOT NULL,
     last_name     VARCHAR(50)  NOT NULL,
     email         VARCHAR(100) NOT NULL UNIQUE,
     password      VARCHAR(255) NOT NULL,
     phone_number  VARCHAR(15)
);

ALTER TABLE Clients
    ADD CONSTRAINT check_email CHECK (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    ADD CONSTRAINT check_phone_number CHECK (phone_number ~ '^\+?[0-9]*$'),
    ADD CONSTRAINT check_password CHECK (password ~ '^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$');

INSERT INTO Clients (first_name, last_name, email, password, phone_number)
VALUES
    ('Ahmed', 'Benali', 'ahmed.benali@example.com', 'Ahmed1234', '+212612345678'),
    ('Sara', 'Haddad', 'sara.haddad@example.com', 'Sara12345', '+212678901234'),
    ('Youssef', 'Amrani', 'youssef.amrani@example.com', 'Youssef123', '+212634567891'),
    ('Nadia', 'ElFassi', 'nadia.elfassi@example.com', 'Nadia1234', '+212655432109'),
    ('Omar', 'Lahlou', 'omar.lahlou@example.com', 'Omar12345', '+212661122334');
