-- Creación de tabla de notificaciones
CREATE TABLE IF NOT EXISTS notificacion (
    notificacion_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT UNSIGNED NOT NULL,
    tipo VARCHAR(50) NOT NULL COMMENT 'ALERTA_SALDO, CONFIRMACION_PROYECCION, MENSAJE_IA, META_PROXIMA',
    categoria VARCHAR(30) COMMENT 'FINANCIERA, SISTEMA, IA',

    -- Datos estructurados
    titulo VARCHAR(150) NOT NULL,
    mensaje TEXT NOT NULL,

    -- Datos flexibles en JSON (usar LONGTEXT temporalmente para compatibilidad con Hibernate)
    datos LONGTEXT COMMENT 'Metadata adicional según el tipo de notificación (formato JSON)',

    -- Control
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    leida BOOLEAN DEFAULT FALSE,
    fecha_lectura TIMESTAMP NULL,

    -- Índices
    INDEX idx_usuario_noleida (usuario_id, leida, fecha_creacion),
    INDEX idx_tipo (tipo),
    INDEX idx_categoria (categoria),
    INDEX idx_fecha_creacion (fecha_creacion),

    -- Foreign Keys
    CONSTRAINT fk_notificacion_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(usuario_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios
ALTER TABLE notificacion COMMENT = 'Notificaciones y alertas del sistema para los usuarios';