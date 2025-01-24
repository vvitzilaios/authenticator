CREATE TABLE IF NOT EXISTS "user"
(
    id       SERIAL PRIMARY KEY,           -- Auto-incrementing primary key
    username VARCHAR(255) NOT NULL UNIQUE, -- Username, unique constraint
    email    VARCHAR(255) NOT NULL UNIQUE, -- Email, unique constraint
    password VARCHAR(255) NOT NULL,        -- Password field for hashed passwords
    role     VARCHAR(50)  NOT NULL         -- Role field (e.g., ADMIN, USER)
);