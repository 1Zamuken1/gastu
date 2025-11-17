CREATE TABLE IF NOT EXISTS concepto_egreso (
    concepto_egreso_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    tipo_transaccion VARCHAR(20) NOT NULL DEFAULT 'VARIABLE',
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_activo (activo),
    INDEX idx_tipo_transaccion (tipo_transaccion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE concepto_egreso COMMENT = 'Catálogo de conceptos de egreso con tipo de transacción';