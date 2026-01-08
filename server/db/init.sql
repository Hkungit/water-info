-- MySQL 8.x initialization script for water-info backend
-- Run: mysql -u root -p < server/db/init.sql

CREATE DATABASE IF NOT EXISTS water_info
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;
USE water_info;

-- Users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(64),
    email VARCHAR(128) UNIQUE,
    phone VARCHAR(32),
    role VARCHAR(16) NOT NULL DEFAULT 'VIEWER',
    status INT NOT NULL DEFAULT 1,
    last_login_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Stations
CREATE TABLE IF NOT EXISTS stations (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    location VARCHAR(256),
    latitude DECIMAL(10,6),
    longitude DECIMAL(10,6),
    description VARCHAR(512),
    status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_station_status (status)
) ENGINE=InnoDB;

-- Water level records
CREATE TABLE IF NOT EXISTS water_level_records (
    id BINARY(16) PRIMARY KEY,
    station_id BINARY(16) NOT NULL,
    current_level DECIMAL(10,2) NOT NULL,
    warning_level DECIMAL(10,2),
    danger_level DECIMAL(10,2),
    status VARCHAR(16) NOT NULL DEFAULT 'NORMAL',
    recorded_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_water_level_station FOREIGN KEY (station_id) REFERENCES stations(id) ON DELETE CASCADE,
    INDEX idx_water_level_station (station_id),
    INDEX idx_water_level_recorded_at (recorded_at)
) ENGINE=InnoDB;

-- Flow records
CREATE TABLE IF NOT EXISTS flow_records (
    id BINARY(16) PRIMARY KEY,
    station_id BINARY(16) NOT NULL,
    flow_rate DECIMAL(10,2),
    velocity DECIMAL(10,2),
    status VARCHAR(16) NOT NULL DEFAULT 'NORMAL',
    recorded_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_flow_station FOREIGN KEY (station_id) REFERENCES stations(id) ON DELETE CASCADE,
    INDEX idx_flow_station (station_id),
    INDEX idx_flow_recorded_at (recorded_at)
) ENGINE=InnoDB;

-- Water quality records
CREATE TABLE IF NOT EXISTS water_quality_records (
    id BINARY(16) PRIMARY KEY,
    station_id BINARY(16) NOT NULL,
    ph DECIMAL(10,2),
    dissolved_oxygen DECIMAL(10,2),
    turbidity DECIMAL(10,2),
    temperature DECIMAL(10,2),
    conductivity DECIMAL(10,2),
    status VARCHAR(16) NOT NULL DEFAULT 'NORMAL',
    recorded_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_water_quality_station FOREIGN KEY (station_id) REFERENCES stations(id) ON DELETE CASCADE,
    INDEX idx_wq_station (station_id),
    INDEX idx_wq_recorded_at (recorded_at)
) ENGINE=InnoDB;

-- Alarms
CREATE TABLE IF NOT EXISTS alarms (
    id BINARY(16) PRIMARY KEY,
    station_id BINARY(16) NOT NULL,
    alarm_type VARCHAR(32) NOT NULL,
    severity VARCHAR(16) NOT NULL,
    message VARCHAR(512) NOT NULL,
    status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    resolved_at DATETIME,
    resolved_by BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_alarm_station FOREIGN KEY (station_id) REFERENCES stations(id) ON DELETE CASCADE,
    CONSTRAINT fk_alarm_resolver FOREIGN KEY (resolved_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_alarm_station (station_id),
    INDEX idx_alarm_status (status),
    INDEX idx_alarm_severity (severity)
) ENGINE=InnoDB;

-- Seed sample data (UUID literals are stored as BINARY(16))
INSERT INTO users (username, password, real_name, email, phone, role, status)
VALUES
  ('admin', '$2a$10$CwTycUXWue0Thq9StjUM0uJ8Y4ZC/7gv2Fne5DE5pSxbFzNw2K6yW', 'System Admin', 'admin@waterinfo.com', '13800138000', 'ADMIN', 1),
  ('operator1', '$2a$10$CwTycUXWue0Thq9StjUM0uJ8Y4ZC/7gv2Fne5DE5pSxbFzNw2K6yW', 'Field Operator', 'operator1@waterinfo.com', '13800138001', 'OPERATOR', 1)
ON DUPLICATE KEY UPDATE username = username;

INSERT INTO stations (id, name, location, latitude, longitude, description, status)
VALUES
  (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001','-','')), 'Main Station', 'Beijing - Chaoyang', 39.904200, 116.407400, 'Primary monitoring station', 'ACTIVE'),
  (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002','-','')), 'East Station', 'Beijing - Tongzhou', 39.909700, 116.663800, 'Eastern river monitoring', 'ACTIVE'),
  (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003','-','')), 'North Station', 'Beijing - Changping', 40.213000, 116.239500, 'Reservoir monitoring station', 'MAINTENANCE')
ON DUPLICATE KEY UPDATE name = VALUES(name), location = VALUES(location), status = VALUES(status);

INSERT INTO water_level_records (id, station_id, current_level, warning_level, danger_level, status, recorded_at)
VALUES
  (UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001','-','')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001','-','')), 12.50, 15.00, 18.00, 'NORMAL', '2024-01-01 12:00:00'),
  (UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440002','-','')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002','-','')), 10.20, 14.00, 17.00, 'NORMAL', '2024-01-01 12:10:00'),
  (UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440003','-','')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003','-','')), 16.20, 15.00, 18.00, 'WARNING', '2024-01-01 11:50:00')
ON DUPLICATE KEY UPDATE recorded_at = VALUES(recorded_at), current_level = VALUES(current_level), status = VALUES(status);

INSERT INTO flow_records (id, station_id, flow_rate, velocity, status, recorded_at)
VALUES
  (UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440001','-','')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001','-','')), 150.25, 2.50, 'NORMAL', '2024-01-01 12:05:00'),
  (UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440002','-','')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002','-','')), 98.60, 1.80, 'NORMAL', '2024-01-01 12:08:00')
ON DUPLICATE KEY UPDATE recorded_at = VALUES(recorded_at), flow_rate = VALUES(flow_rate), status = VALUES(status);

INSERT INTO water_quality_records (id, station_id, ph, dissolved_oxygen, turbidity, temperature, conductivity, status, recorded_at)
VALUES
  (UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440001','-','')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001','-','')), 7.20, 8.50, 3.20, 22.50, 450.0, 'NORMAL', '2024-01-01 12:00:00'),
  (UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440002','-','')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002','-','')), 6.50, 7.80, 5.10, 21.30, 520.0, 'WARNING', '2024-01-01 12:12:00')
ON DUPLICATE KEY UPDATE recorded_at = VALUES(recorded_at), ph = VALUES(ph), status = VALUES(status);

INSERT INTO alarms (id, station_id, alarm_type, severity, message, status, resolved_at, resolved_by, created_at)
VALUES
  (UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440001','-','')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003','-','')), 'WATER_LEVEL_WARNING', 'HIGH', 'Water level above warning at North Station', 'ACTIVE', NULL, NULL, '2024-01-01 11:55:00'),
  (UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440002','-','')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002','-','')), 'WATER_QUALITY_ABNORMAL', 'MEDIUM', 'pH below normal range at East Station', 'RESOLVED', '2024-01-01 12:20:00', 2, '2024-01-01 11:50:00')
ON DUPLICATE KEY UPDATE message = VALUES(message), status = VALUES(status), resolved_at = VALUES(resolved_at), resolved_by = VALUES(resolved_by);
