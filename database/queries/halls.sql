DROP TABLE IF EXISTS Halls;

CREATE TABLE Halls (
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(50)  NOT NULL,
    capacity      INTEGER      NOT NULL CHECK (capacity >= 10 AND capacity <= 500)
);

INSERT INTO Halls (name, capacity)
VALUES
    ('Main Hall', 250),
    ('Starlight 3D', 150),
    ('VIP Lounge', 50),
    ('Cinema B1', 180);
