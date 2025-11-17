CREATE TABLE IF NOT EXISTS egreso (
    egreso_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    monto DECIMAL(12,2) NOT NULL,
    descripcion VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_transaccion DATE NOT NULL,
    activo TINYINT(1) DEFAULT 1,
    concepto_egreso_id BIGINT UNSIGNED,
    usuario_id BIGINT UNSIGNED NOT NULL,

    INDEX idx_usuario_id (usuario_id),
    INDEX idx_fecha_registro (fecha_registro),
    INDEX idx_fecha_transaccion (fecha_transaccion),
    INDEX idx_concepto_egreso_id (concepto_egreso_id),
    INDEX idx_activo (activo),

    CONSTRAINT fk_egreso_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(usuario_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_egreso_concepto
        FOREIGN KEY (concepto_egreso_id)
        REFERENCES concepto_egreso(concepto_egreso_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,

    CONSTRAINT chk_egreso_monto_positivo CHECK (monto > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE egreso COMMENT = 'Egresos reales registrados por el usuario';