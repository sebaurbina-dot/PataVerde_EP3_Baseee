-- =====================================================================
-- Clínica Veterinaria PataVerde - Base de datos (MySQL 8)
-- Crea la base de datos pataverde, sus tablas y los datos iniciales.
-- Ejecuta el script completo. No lo modifiques.
-- =====================================================================

DROP DATABASE IF EXISTS pataverde;
CREATE DATABASE pataverde CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE pataverde;

-- Catálogo de tipos de mascota
CREATE TABLE tipo_mascota (
    id_tipo      INT          NOT NULL,
    descripcion  VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_tipo_mascota PRIMARY KEY (id_tipo),
    CONSTRAINT uq_tipo_mascota_descripcion UNIQUE (descripcion)
);

-- Mascotas registradas.
-- raza, vacunado y disponible_adopcion se usan solo para perros;
-- vive_exterior se usa solo para gatos.
CREATE TABLE mascota (
    id                   INT           NOT NULL AUTO_INCREMENT,
    id_tipo              INT           NOT NULL,
    nombre               VARCHAR(50)   NOT NULL,
    edad                 INT           NOT NULL,
    peso                 DECIMAL(5,2)  NOT NULL,
    raza                 VARCHAR(50)   NULL,
    vacunado             BOOLEAN       NULL,
    disponible_adopcion  BOOLEAN       NULL,
    vive_exterior        BOOLEAN       NULL,
    CONSTRAINT pk_mascota PRIMARY KEY (id),
    CONSTRAINT fk_mascota_tipo FOREIGN KEY (id_tipo) REFERENCES tipo_mascota (id_tipo),
    CONSTRAINT ck_mascota_edad CHECK (edad BETWEEN 0 AND 30),
    CONSTRAINT ck_mascota_peso CHECK (peso > 0)
);

-- Datos iniciales
INSERT INTO tipo_mascota (id_tipo, descripcion) VALUES (1, 'Perro');
INSERT INTO tipo_mascota (id_tipo, descripcion) VALUES (2, 'Gato');
