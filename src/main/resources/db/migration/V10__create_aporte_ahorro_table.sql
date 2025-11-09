-- Creación de tabla de aportes a metas de ahorro
CREATE TABLE IF NOT EXISTS aporte_ahorro (
    aporte_ahorro_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    ahorro_meta_id BIGINT UNSIGNED NOT NULL,
    aporte_asignado DECIMAL(12,2) DEFAULT 0,
    aporte DECIMAL(12,2) DEFAULT 0,
    fecha_limite DATE,
    estado VARCHAR(30),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Índices
    INDEX idx_ahorro_meta_id (ahorro_meta_id),
    INDEX idx_estado (estado),
    INDEX idx_fecha_limite (fecha_limite),
    INDEX idx_fecha_registro (fecha_registro),

    -- Foreign Keys
    CONSTRAINT fk_aporte_ahorro_meta
        FOREIGN KEY (ahorro_meta_id)
        REFERENCES ahorro_meta(ahorro_meta_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- Validaciones
    CONSTRAINT chk_aporte_asignado_no_negativo CHECK (aporte_asignado >= 0),
    CONSTRAINT chk_aporte_no_negativo CHECK (aporte >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios
ALTER TABLE aporte_ahorro COMMENT = 'Aportes individuales realizados a las metas de ahorro';