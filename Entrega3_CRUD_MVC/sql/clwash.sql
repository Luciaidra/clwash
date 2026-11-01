-- =========================================================
--  C&L Wash - Script de base de datos (MySQL 8)
--  Crea la base, las tablas y algunos datos de prueba.
-- =========================================================

CREATE DATABASE IF NOT EXISTS clwash
    CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE clwash;

-- ---------- Vehiculo ----------
CREATE TABLE IF NOT EXISTS vehiculo (
    patente  VARCHAR(10)  NOT NULL,
    marca    VARCHAR(40)  NOT NULL,
    modelo   VARCHAR(40)  NOT NULL,
    tipo     VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_vehiculo PRIMARY KEY (patente)
);

-- ---------- Servicio ----------
CREATE TABLE IF NOT EXISTS servicio (
    id_servicio   INT           NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(60)   NOT NULL,
    descripcion   VARCHAR(255),
    precio        DECIMAL(10,2) NOT NULL,
    duracion_min  INT           NOT NULL,
    CONSTRAINT pk_servicio PRIMARY KEY (id_servicio),
    CONSTRAINT uq_servicio_nombre UNIQUE (nombre)
);

-- ---------- OrdenLavado (entidad combinatoria) ----------
CREATE TABLE IF NOT EXISTS orden_lavado (
    id_orden       INT           NOT NULL AUTO_INCREMENT,
    fecha_hora     DATETIME      NOT NULL,
    estado         VARCHAR(20)   NOT NULL,
    total          DECIMAL(10,2) NOT NULL,
    observaciones  VARCHAR(255),
    patente        VARCHAR(10)   NOT NULL,
    id_servicio    INT           NOT NULL,
    CONSTRAINT pk_orden PRIMARY KEY (id_orden),
    CONSTRAINT fk_orden_vehiculo FOREIGN KEY (patente)
        REFERENCES vehiculo (patente) ON UPDATE CASCADE,
    CONSTRAINT fk_orden_servicio FOREIGN KEY (id_servicio)
        REFERENCES servicio (id_servicio)
);

-- ---------- Datos de prueba ----------
INSERT IGNORE INTO vehiculo (patente, marca, modelo, tipo) VALUES
    ('AB123CD', 'Toyota',     'Corolla', 'AUTO'),
    ('AE456FG', 'Ford',       'Ranger',  'CAMIONETA'),
    ('KLM789',  'Volkswagen', 'Gol',     'AUTO'),
    ('A123BCD', 'Honda',      'CB 190',  'MOTO');

INSERT IGNORE INTO servicio (nombre, descripcion, precio, duracion_min) VALUES
    ('Lavado exterior', 'Lavado de carrocería, llantas y secado', 8000.00, 30),
    ('Lavado completo', 'Exterior + aspirado y limpieza interior', 15000.00, 60),
    ('Encerado',        'Aplicación de cera protectora',          12000.00, 45);
