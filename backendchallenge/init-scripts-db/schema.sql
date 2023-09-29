--- EXTENSIONS

create extension if not exists "uuid-ossp";

--- TABLES

CREATE TABLE movies (
                        id UUID PRIMARY KEY NOT NULL,
                        title VARCHAR(250) NOT NULL CHECK (LENGTH(title) >= 2 AND LENGTH(title) <= 250),
                        launch_date DATE NOT NULL,  -- Change to launch_date
                        rank INTEGER NOT NULL CHECK (rank >= 0 AND rank <= 10),
                        revenue NUMERIC(19, 2) NOT NULL
);
