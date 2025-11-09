-- Creación de tabla de usuarios
CREATE TABLE IF NOT EXISTS usuario (
    usuario_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    correo VARCHAR(50) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo TINYINT(1) DEFAULT 1,
    rol_id INT NOT NULL,

    -- Índices
    INDEX idx_correo (correo),
    INDEX idx_activo (activo),
    INDEX idx_rol_id (rol_id),

    -- Foreign Keys
    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (rol_id)
        REFERENCES rol(rol_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios
ALTER TABLE usuario COMMENT = 'Tabla que almacena los usuarios del sistema con sus credenciales';