DROP TABLE IF EXISTS Users;

CREATE TABLE Users
(
    id           SERIAL PRIMARY KEY,
    role         VARCHAR(50)  NOT NULL CHECK (role IN ('user', 'front_desk')),
    first_name   VARCHAR(50)  NOT NULL,
    last_name    VARCHAR(50)  NOT NULL,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    email        VARCHAR(100) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15),

    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE Users
    ADD CONSTRAINT check_first_name CHECK (first_name ~ '^[A-Za-z]+$'),
    ADD CONSTRAINT check_last_name CHECK (last_name ~ '^[A-Za-z]+$'),
    ADD CONSTRAINT check_username CHECK (username ~ '^[a-zA-Z0-9_]+$'),
    ADD CONSTRAINT check_email CHECK (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    ADD CONSTRAINT check_password CHECK (password ~ '^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$'),
    ADD CONSTRAINT check_phone_number CHECK (phone_number ~ '^\+?[0-9]*$');

INSERT INTO Users (role, first_name, last_name, username, email, password, phone_number)
VALUES
    ('admin', 'Mouad', 'Maaroufi', 'mouad_admin', 'mouad.maaroufi@gmail.com', 'Admin1234', '+212612345678'),
    ('admin', 'Leila', 'ElAmrani', 'leila_ela', 'leila.elamrani@example.com', 'Leila1234', '+212678901234'),
    ('front_desk', 'Fatima', 'Zahra', 'fatima_z', 'fatima.zahra@example.com', 'Fatima123', '+212634567891'),
    ('front_desk', 'Rachid', 'Bennani', 'rachid_b', 'rachid.bennani@example.com', 'Rachid123', '+212655432109'),
    ('front_desk', 'Hassan', 'ElKhatib', 'hassan_elk', 'hassan.elkhatib@example.com', 'Hassan123', '+212661122334');
