-- Insert Users
INSERT INTO "user" (username, password, email, role)
VALUES ('admin', 'hashedAdminPassword', 'admin@example.com',  'ROLE_ADMIN'),
       ('guest', 'hashedJohnPassword', 'john@example.com', 'ROLE_USER');

-- Insert Groups
INSERT INTO user_group (name)
VALUES ('Finance Department'),
       ('IT Department');

-- Insert Roles
INSERT INTO role (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_OWNER'),
       ('ROLE_USER');

-- Insert Authorities
INSERT INTO authority (name)
VALUES ('READ_PRIVILEGES'),
       ('WRITE_PRIVILEGES'),
       ('DELETE_PRIVILEGES');

-- Map Users to Groups
INSERT INTO user_groups (user_id, group_id)
VALUES (1, 1), -- Admin in Finance Department
       (2, 2);
-- John in IT Department

-- Map Groups to Roles
INSERT INTO group_roles (group_id, role_id)
VALUES (1, 1), -- Finance Department has ROLE_ADMIN
       (2, 2);
-- IT Department has ROLE_USER

-- Map Roles to Authorities
INSERT INTO role_authority (role_id, authority_id)
VALUES (1, 1), -- ROLE_ADMIN has READ_PRIVILEGES
       (1, 2), -- ROLE_ADMIN has WRITE_PRIVILEGES
       (1, 3), -- ROLE_ADMIN has DELETE_PRIVILEGES
       (2, 1); -- ROLE_USER has READ_PRIVILEGES