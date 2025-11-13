-- Insertar roles iniciales
INSERT INTO rol (nombre, descripcion) VALUES
('ADMINISTRADOR', 'Acceso completo al sistema'),
('INSTRUCTOR', 'Puede ver y gestionar datos de aprendices'),
('APRENDIZ', 'Usuario estándar con acceso a sus propios datos')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- Insertar conceptos de ingreso comunes
INSERT INTO concepto_ingreso (nombre, descripcion) VALUES
('Salario', 'Ingreso por salario mensual'),
('Bonificación', 'Bonos o incentivos laborales'),
('Freelance', 'Ingresos por trabajos independientes'),
('Ventas', 'Ingresos por ventas de productos o servicios'),
('Inversiones', 'Rendimientos de inversiones'),
('Otros', 'Otros ingresos no categorizados')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- Insertar conceptos de egreso comunes
INSERT INTO concepto_egreso (nombre, descripcion) VALUES
('Alimentación', 'Gastos en comida y bebidas'),
('Transporte', 'Gastos en transporte público o privado'),
('Vivienda', 'Alquiler, servicios públicos, mantenimiento'),
('Educación', 'Gastos educativos y formación'),
('Salud', 'Gastos médicos y medicamentos'),
('Entretenimiento', 'Ocio, streaming, cine, etc.'),
('Vestimenta', 'Ropa y accesorios'),
('Tecnología', 'Dispositivos y servicios tecnológicos'),
('Deudas', 'Pagos de préstamos y tarjetas de crédito'),
('Otros', 'Otros gastos no categorizados')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- Crear usuario administrador por defecto (IMPORTANTE: cambiar password en producción)
-- Password: Admin123! (encriptado con BCrypt)
INSERT INTO usuario (nombre, correo, telefono, password, rol_id)
SELECT
    'Administrador',
    'admin@gastu.com',
    '3001234567',
    '$2a$10$sUajTrDC/u8CcOW.gMgrseeVrL12OGa4ztT.G76JfZXDyfFx5qsvG',  -- Admin123!
    rol_id
FROM rol
WHERE nombre = 'ADMINISTRADOR'
LIMIT 1
ON DUPLICATE KEY UPDATE correo = VALUES(correo);