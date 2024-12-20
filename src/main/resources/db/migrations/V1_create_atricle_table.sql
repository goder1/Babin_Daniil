CREATE TABLE if NOT EXISTS article
(
    id serial PRIMARY KEY,
    name VARCHAR NOT NULL,
    tags VARCHAR,
    trending BOOLEAN DEFAULT FALSE NOT NULL
);