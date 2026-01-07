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
VALUES ('admin', '$2a$10$CwTycUXWue0Thq9StjUM0uJ8Y4ZC/7gv2Fne5DE5pSxbFzNw2K6yW', '系统管理员', 'admin@waterinfo.com', '13800138000', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE username = username;

INSERT INTO stations (id, name, location, latitude, longitude, description, status)
VALUES (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001','-','')), '主站监测点', '北京市朝阳区', 39.904200, 116.407400, '主站监测站点', 'ACTIVE')
ON DUPLICATE KEY UPDATE name = name;
