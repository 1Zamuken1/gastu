CREATE TABLE IF NOT EXISTS ingreso (
    ingreso_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    monto DECIMAL(12,2) NOT NULL,
    descripcion VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_transaccion DATE NOT NULL,
    activo TINYINT(1) DEFAULT 1,
    concepto_ingreso_id BIGINT UNSIGNED,
    usuario_id BIGINT UNSIGNED NOT NULL,

    INDEX idx_usuario_id (usuario_id),
    INDEX idx_fecha_registro (fecha_registro),
    INDEX idx_fecha_transaccion (fecha_transaccion),
    INDEX idx_concepto_ingreso_id (concepto_ingreso_id),
    INDEX idx_activo (activo),

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

    CONSTRAINT chk_ingreso_monto_positivo CHECK (monto > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE ingreso COMMENT = 'Ingresos reales registrados por el usuario';