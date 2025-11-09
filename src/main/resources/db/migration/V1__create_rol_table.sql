-- Creación de tabla de roles
CREATE TABLE IF NOT EXISTS rol (
    rol_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo TINYINT(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios de tabla
ALTER TABLE rol COMMENT = 'Tabla que almacena los roles del sistema (administrador, instructor, aprendiz)';