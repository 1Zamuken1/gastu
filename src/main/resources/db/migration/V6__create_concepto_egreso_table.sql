-- Creación de tabla de conceptos de egreso
CREATE TABLE IF NOT EXISTS concepto_egreso (
    concepto_egreso_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Índices
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios
ALTER TABLE concepto_egreso COMMENT = 'Catálogo de conceptos de egreso (Alimentación, Transporte, Entretenimiento, etc.)';