-- Creación de tabla de ingresos reales
CREATE TABLE IF NOT EXISTS ingreso (
    ingreso_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(20),
    monto DECIMAL(12,2) NOT NULL,
    descripcion VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_transaccion DATE NOT NULL,
    concepto_ingreso_id BIGINT UNSIGNED,
    usuario_id BIGINT UNSIGNED NOT NULL,

    -- Índices
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_fecha_registro (fecha_registro),
    INDEX idx_fecha_transaccion (fecha_transaccion),
    INDEX idx_concepto_ingreso_id (concepto_ingreso_id),
    INDEX idx_tipo (tipo),

    -- Foreign Keys
    CONSTRAINT fk_ingreso_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(usuario_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_ingreso_concepto
        FOREIGN KEY (concepto_ingreso_id)
        REFERENCES concepto_ingreso(concepto_ingreso_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,

    -- Validaciones
    CONSTRAINT chk_ingreso_monto_positivo CHECK (monto > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios
ALTER TABLE ingreso COMMENT = 'Ingresos reales registrados por el usuario';