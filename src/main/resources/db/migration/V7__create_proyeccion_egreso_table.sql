-- Creación de tabla de proyección de egresos (programados)
CREATE TABLE IF NOT EXISTS proyeccion_egreso (
    proyeccion_egreso_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    monto_programado DECIMAL(12,2) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    fecha_fin DATE,
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    concepto_egreso_id BIGINT UNSIGNED NOT NULL,
    usuario_id BIGINT UNSIGNED NOT NULL,

    -- Índices
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_activo (activo),
    INDEX idx_fecha_fin (fecha_fin),
    INDEX idx_concepto_egreso_id (concepto_egreso_id),

    -- Foreign Keys
    CONSTRAINT fk_proyeccion_egreso_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(usuario_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_proyeccion_egreso_concepto
        FOREIGN KEY (concepto_egreso_id)
        REFERENCES concepto_egreso(concepto_egreso_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios
ALTER TABLE proyeccion_egreso COMMENT = 'Egresos programados o recurrentes del usuario';