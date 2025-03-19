-- Crear tabla permissions
CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Crear tabla roles
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL UNIQUE
);

-- Crear tabla role_permission
CREATE TABLE role_permission (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

-- Crear tabla users
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(8) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_enabled BOOLEAN NOT NULL,
    account_no_expired BOOLEAN NOT NULL,
    account_no_locked BOOLEAN NOT NULL,
    credential_no_expired BOOLEAN NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Crear permisos
INSERT INTO permissions (id, name) VALUES
(1, 'CREATE'),
(2, 'READ'),
(3, 'UPDATE'),
(4, 'DELETE');

-- Crear roles
INSERT INTO roles (id, role_name) VALUES
(1, 'ADMIN'),
(2, 'SECRETARY'),
(3, 'DOCTOR'),
(4, 'USER');

-- Asociar roles con permisos
INSERT INTO role_permission (role_id, permission_id) VALUES
(1, 1), -- ADMIN - CREATE
(1, 2), -- ADMIN - READ
(1, 3), -- ADMIN - UPDATE
(1, 4), -- ADMIN - DELETE
(2, 1), -- SECRETARY - CREATE
(2, 2), -- SECRETARY - READ
(2, 3), -- SECRETARY - UPDATE
(3, 2), -- DOCTOR - READ
(3, 3), -- DOCTOR - UPDATE
(4, 2); -- USER - READ

INSERT INTO users (username, email, password, is_enabled, account_no_expired, account_no_locked, credential_no_expired, role_id) VALUES
('admin', 'admin@example.com', '$2a$12$YWTvWVyrq1l6rZqKMFyROunxG1jIzzg0djOZVqBrPayknoaEl0HBK', TRUE, TRUE, TRUE, TRUE, 1);
