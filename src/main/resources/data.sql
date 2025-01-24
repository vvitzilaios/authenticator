-- Insert admin user if it doesn't already exist
INSERT INTO "user" (username, email, password, role)
SELECT 'admin',
       'admin@example.com',
       '$2a$10$v12SpTwNq0YsJzSI8zcRgOjY2UAxyGK.ACXcgFFAvQQDjhwyw9aMu',
       'ADMIN' WHERE NOT EXISTS (
    SELECT 1 FROM "user" WHERE username = 'admin'
);

-- Insert regular user if it doesn't already exist
INSERT INTO "user" (username, email, password, role)
SELECT 'user',
       'user@example.com',
       '$2a$10$5cE0IEqsb9Nfq7ty.pY12uRd8j/2o8vby.dH4eNN8KTQEbYB9Fa0i',
       'USER' WHERE NOT EXISTS (
    SELECT 1 FROM "user" WHERE username = 'user'
);