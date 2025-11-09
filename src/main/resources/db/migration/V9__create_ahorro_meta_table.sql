-- Creación de tabla de metas de ahorro
CREATE TABLE IF NOT EXISTS ahorro_meta (
    ahorro_meta_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT UNSIGNED NOT NULL,
    concepto VARCHAR(60) NOT NULL,
    descripcion VARCHAR(100),
    monto_meta DECIMAL(12,2) NOT NULL,
    total_acumulado DECIMAL(12,2) DEFAULT 0,
    frecuencia VARCHAR(30) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_meta DATE NOT NULL,
    estado VARCHAR(30) DEFAULT 'ACTIVO',
    cantidad_cuotas INT,

    -- Índices
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_estado (estado),
    INDEX idx_fecha_meta (fecha_meta),

    -- Foreign Keys
    CONSTRAINT fk_ahorro_meta_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(usuario_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- Validaciones
    CONSTRAINT chk_ahorro_meta_monto_positivo CHECK (monto_meta > 0),
    CONSTRAINT chk_ahorro_meta_acumulado_no_negativo CHECK (total_acumulado >= 0),
    CONSTRAINT chk_ahorro_meta_cuotas_positivas CHECK (cantidad_cuotas IS NULL OR cantidad_cuotas > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios
ALTER TABLE ahorro_meta COMMENT = 'Metas de ahorro definidas por el usuario';