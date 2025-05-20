DROP TABLE IF EXISTS Movies;

CREATE TABLE Movies (
    id            SERIAL PRIMARY KEY,
    title         VARCHAR(100) NOT NULL,
    genre         VARCHAR(50)  NOT NULL CHECK (genre IN ('Action', 'Comedy', 'Drama', 'Sci-Fi', 'Horror', 'Romance', 'Thriller')),
    duration      INTEGER      NOT NULL CHECK (duration >= 30 AND duration <= 300),
    release_date  DATE         NOT NULL,
    description   TEXT
);

ALTER TABLE Movies
    ADD CONSTRAINT check_title CHECK (title ~ '^[A-Za-z0-9\s\-:'']*$');

INSERT INTO Movies (title, genre, duration, release_date, description)
VALUES
    ('The Dark Knight', 'Action', 152, '2008-07-18', 'Batman faces the Joker in a battle for Gotham.'),
    ('Inception', 'Sci-Fi', 148, '2010-07-16', 'A thief enters dreams to steal secrets.'),
    ('La La Land', 'Romance', 128, '2016-12-09', 'A musician and actress fall in love in Los Angeles.'),
    ('Get Out', 'Horror', 104, '2017-02-24', 'A man uncovers a sinister secret at his girlfriend''s family estate.'),
    ('Parasite', 'Thriller', 132, '2019-05-30', 'A poor family infiltrates a wealthy household.'),
    ('The Avengers', 'Action', 143, '2012-05-04', 'Superheroes team up to save Earth from an alien invasion.'),
    ('The Grand Budapest Hotel', 'Comedy', 99, '2014-03-07', 'A concierge and his lobby boy embark on a quirky adventure.'),
    ('Interstellar', 'Sci-Fi', 169, '2014-11-07', 'Astronauts travel through a wormhole to find a new home for humanity.'),
    ('The Shining', 'Horror', 146, '1980-05-23', 'A writer descends into madness at an isolated hotel.'),
    ('Titanic', 'Romance', 194, '1997-12-19', 'A love story unfolds aboard the doomed ship Titanic.'),
    ('Gone Girl', 'Thriller', 149, '2014-10-03', 'A man''s life unravels when his wife disappears.'),
    ('Mad Max: Fury Road', 'Action', 120, '2015-05-15', 'Survivors battle across a post-apocalyptic wasteland.'),
    ('Superbad', 'Comedy', 113, '2007-08-17', 'High school friends plan a wild night before graduation.'),
    ('Blade Runner 2049', 'Sci-Fi', 163, '2017-10-06', 'A replicant hunter uncovers a dangerous secret.'),
    ('Hereditary', 'Horror', 127, '2018-06-08', 'A family faces terrifying events after their matriarch''s death.'),
    ('The Notebook', 'Romance', 123, '2004-06-25', 'A young couple''s love is tested by social differences.'),
    ('Shutter Island', 'Thriller', 138, '2010-02-19', 'A U.S. Marshal investigates a mysterious psychiatric facility.'),
    ('Gladiator', 'Action', 155, '2000-05-05', 'A Roman general seeks revenge against a corrupt emperor.'),
    ('Anchorman', 'Comedy', 94, '2004-07-09', 'A 1970s news anchor faces challenges from a new female colleague.'),
    ('The Matrix', 'Sci-Fi', 136, '1999-03-31', 'A hacker discovers reality is a simulated construct.'),
    ('It', 'Horror', 135, '2017-09-08', 'Kids confront a shape-shifting entity in their town.'),
    ('Pride and Prejudice', 'Romance', 129, '2005-11-23', 'Elizabeth Bennet navigates love and societal expectations.'),
    ('Se7en', 'Thriller', 127, '1995-09-22', 'Detectives hunt a serial killer using the seven deadly sins.'),
    ('Die Hard', 'Action', 132, '1988-07-15', 'A cop battles terrorists in a skyscraper on Christmas Eve.'),
    ('Mean Girls', 'Comedy', 97, '2004-04-30', 'A teen navigates high school cliques and betrayal.'),
    ('Arrival', 'Sci-Fi', 116, '2016-11-11', 'A linguist decodes an alien language to prevent conflict.'),
    ('A Quiet Place', 'Horror', 90, '2018-04-06', 'A family survives in silence to avoid sound-hunting creatures.'),
    ('Before Sunrise', 'Romance', 101, '1995-01-27', 'Two strangers share a romantic night in Vienna.'),
    ('The Departed', 'Thriller', 151, '2006-10-06', 'An undercover cop and a mole clash in Boston''s underworld.'),
    ('Jurassic Park', 'Action', 127, '1993-06-11', 'Scientists encounter living dinosaurs on a remote island.');