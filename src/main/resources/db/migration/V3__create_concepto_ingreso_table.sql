-- Creación de tabla de conceptos de ingreso
CREATE TABLE IF NOT EXISTS concepto_ingreso (
    concepto_ingreso_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Índices
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios
ALTER TABLE concepto_ingreso COMMENT = 'Catálogo de conceptos de ingreso (Salario, Bonos, Ventas, etc.)';