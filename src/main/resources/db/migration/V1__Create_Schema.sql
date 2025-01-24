-- User Table
CREATE TABLE user
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE
);

-- Group Table
CREATE TABLE group
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Role Table
CREATE TABLE role
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Authority Table
CREATE TABLE authority
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- User-Group Mapping Table
CREATE TABLE user_group
(
    user_id  BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES group (id) ON DELETE CASCADE
);

-- Group-Role Mapping Table
CREATE TABLE group_role
(
    group_id BIGINT NOT NULL,
    role_id  BIGINT NOT NULL,
    PRIMARY KEY (group_id, role_id),
    FOREIGN KEY (group_id) REFERENCES group (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE
);

-- Role-Authority Mapping Table
CREATE TABLE role_authorities
(
    role_id      BIGINT NOT NULL,
    authority_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, authority_id),
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE,
    FOREIGN KEY (authority_id) REFERENCES authority (id) ON DELETE CASCADE
);