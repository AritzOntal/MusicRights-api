-- =============================================================================
-- 1. REGISTRO DE MÚSICOS (Dejamos que el ID sea AUTO_INCREMENT)
-- =============================================================================
INSERT IGNORE INTO musicians (first_name, last_name, birth_date, affiliated, dni, performance_fee, affiliated_number, address, postal_code, phone, email, contact_person)
VALUES
('Carlos', 'Santana', '1947-07-20', true, '12345678A', 1500.00, 987654, 'Calle Melancolía 13', '50001', '600112233', 'carlos@santana.com', 'Manager Carlos'),
('Eva', 'Amaral', '1972-08-04', true, '87654321B', 2500.00, 456123, 'Av. de la Jota 45', '50015', '611223344', 'eva@amaral.es', 'Juan Aguirre'),
('Rob', 'Halford', '1951-08-25', true, '99887766M', 5000.00, 112233, 'Metal Avenue 66', '28001', '622334455', 'rob@judas.com', 'Metal God Management'),
('Rosalía', 'Vila', '1992-09-25', false, '55443322C', 8000.00, 0, 'Carrer de la Pau 8', '08001', '633445566', 'info@rosalia.com', 'Rosalía Contact'),
('Fito', 'Cabrales', '1966-10-06', true, '44556677D', 3500.00, 789101, 'Calle Estrecha 4', '48001', '644556677', 'fito@fitipaldis.com', 'Cultura Rock'),
('KaseO', 'Ibarra', '1980-03-20', true, '33221199E', 3000.00, 223344, 'Barrio El Gancho 12', '50003', '655667788', 'javier@kaseo.com', 'Violadores del Verso Mng'),
('C. Tangana', 'Álvarez', '1990-07-16', true, '11223344F', 7500.00, 556677, 'Gran Vía 32', '28013', '666778899', 'pucho@elmadrileno.com', 'Sony Music'),
('Chano', 'Domínguez', '1960-03-29', true, '77889900G', 2000.00, 889900, 'Plaza de las Flores 2', '11001', '677889900', 'chano@jazz.es', 'Chano Management'),
('Sílvia', 'Pérez Cruz', '1983-02-15', true, '66778899H', 2200.00, 114477, 'Rambla Principal 55', '17200', '688990011', 'silvia@perezcruz.com', 'Música Global'),
('Enrique', 'Bunbury', '1967-08-11', true, '55667788J', 4500.00, 334455, 'Camino del Caos s/n', '50008', '699001122', 'enrique@bunbury.com', 'Nacho Royo');

-- =============================================================================
-- 2. REGISTRO DE OBRAS (Tabla: works)
-- =============================================================================
INSERT IGNORE INTO works (id, title, isrc, genre, duration, composed_at, registred)
VALUES 
(1, 'Samba Pa Ti', 'ESABC7000123', 'Rock', 4.45, '1970-05-10', true),
(2, 'Marta, Sebas, Guille y los demás', 'ESXYZ0500456', 'Pop', 3.28, '2005-03-15', true),
(3, 'Painkiller', 'GBJUD9000789', 'Heavy Metal', 6.06, '1990-09-03', true),
(4, 'Malamente', 'ESROS1800111', 'Flamenco Urbano', 2.30, '2018-05-30', true),
(5, 'Obra No Registrada de Prueba', 'ESNON2500000', 'Experimental', 5.12, '2025-01-01', false),
(6, 'Soldadito Marinero', 'ESFIT0300123', 'Rock', 4.10, '2003-09-15', true),
(7, 'Por la boca vive el pez', 'ESFIT0600456', 'Rock', 4.20, '2006-11-20', true),
(8, 'Mierda', 'ESRAP9800789', 'Rap', 5.45, '1998-04-10', true),
(9, 'Demasiado de lo Mismo', 'ESRAP2200111', 'Rap', 3.50, '2022-02-14', true),
(10, 'Demasiadas Mujeres', 'ESTAN2000222', 'Urbano / Pop', 2.45, '2020-10-08', true),
(11, 'Tú me dejaste de querer', 'ESTAN2100333', 'Rumba / Pop', 3.18, '2021-03-05', true),
(12, 'Ziryab (Piano Cover)', 'ESJAZ9200444', 'Flamenco Jazz', 7.15, '1992-06-12', true),
(13, 'No hay tanto pan', 'ESFOL1600555', 'Folk / Canción de Autor', 4.05, '2016-01-20', true),
(14, 'Entre dos tierras', 'ESHER9000666', 'Rock Alternativo', 6.05, '1990-04-02', true),
(15, 'Lady Blue', 'ESBUN0200777', 'Glam Rock', 5.30, '2002-03-25', true),
(16, 'Ecos de Medianoche', 'ESARZ2600016', 'Rock Alternativo', 4.15, '2023-04-10', true),
(17, 'Fuego en el Asfalto', 'ESARZ2600017', 'Hard Rock', 3.58, '2022-08-19', true),
(18, 'Caminos Cruzados', 'ESARZ2600018', 'Rock / Blues', 5.12, '2023-01-15', true),
(19, 'El Vals de la Locura', 'ESARZ2600019', 'Rock Progresivo', 6.40, '2024-05-20', true),
(20, 'Luz de Gas', 'ESARZ2600020', 'Rock Pop', 3.22, '2022-11-02', true),
(21, 'Sombras en la Niebla', 'ESARZ2600021', 'Gothic Rock', 4.45, '2023-09-05', true),
(22, 'Gritos al Viento', 'ESARZ2600022', 'Hard Rock', 3.34, '2024-02-14', true),
(23, 'La Última Estación', 'ESARZ2600023', 'Rock Alternativo', 5.02, '2021-06-30', true),
(24, 'Bajo la Lluvia', 'ESARZ2600024', 'Rock / Blues', 4.28, '2023-12-25', true),
(25, 'Despierta el Motor', 'ESARZ2600025', 'Heavy Rock', 3.49, '2024-07-01', true),
(26, 'Horizonte de Arena', 'ESARZ2600026', 'Stoner Rock', 5.30, '2022-03-11', true),
(27, 'Viento de Cara', 'ESARZ2600027', 'Rock Urbano', 4.05, '2023-07-22', true),
(28, 'Espejos Rotos', 'ESARZ2600028', 'Rock Alternativo', 3.52, '2024-10-05', true),
(29, 'El Último Trago', 'ESARZ2600029', 'Rock / Blues', 4.55, '2021-09-18', true),
(30, 'Cielo Eléctrico', 'ESARZ2600030', 'Hard Rock', 4.18, '2024-12-01', true);

-- =============================================================================
-- 3. RELACIÓN COMPUESTA (Tabla intermedia: musician_work)
-- =============================================================================
INSERT IGNORE INTO musician_work (work_id, musician_id) VALUES 
(1, 1), (2, 2), (3, 3), (4, 4), (4, 1), (6, 5), (7, 5), 
(8, 6), (9, 6), (10, 7), (11, 7), (12, 8), (13, 9), (14, 10), (15, 10),
(16, 2), (17, 2), (18, 2), (19, 2), (20, 2), (21, 2), (22, 2),
(23, 2), (24, 2), (25, 2), (26, 2), (27, 2), (28, 2), (29, 2), (30, 2);

-- =============================================================================
-- 4. REGISTRO DE CONCIERTOS (Asociados a Aritz - musician_id = 2)
-- =============================================================================

INSERT IGNORE INTO concerts (
    id, show_title, city, province, date, status, performed, ticket_price,
    longitude, latitude, time, venue_name, venue_address, capacity,
    venue_owner, performers, ticket_class, total_tickets, tariff_type, musician_id
)
VALUES
-- Conciertos Pasados (COMPLETED / ARCHIVED)
(13, 'Gira El Despertar 2022', 'Zaragoza', 'Zaragoza', '2022-03-15', 'ARCHIVED', true, 20.00, -0.8891, 41.6488, '20:00:00', 'Sala López', 'Calle Sixto Celorrio 2', 400, 'Privado', 'Aritz Solo', 'General', 380, 'FLAT', 2),
(14, 'Noche de Rock Urbano', 'Madrid', 'Madrid', '2022-05-20', 'COMPLETED', true, 25.00, -3.7038, 40.4167, '21:00:00', 'Sala Riviera', 'Paseo bajo de la Virgen s/n', 2500, 'Privado', 'Aritz & Banda', 'General', 2400, 'PERCENTAGE', 2),
(15, 'Festival de Verano Independiente', 'Zaragoza', 'Zaragoza', '2022-07-08', 'COMPLETED', true, 40.00, -0.8833, 41.6563, '22:30:00', 'Espacio Zity', 'Recinto Ferial Valdespartera', 15000, 'Ayto Zaragoza', 'Aritz Live', 'General', 14200, 'PERCENTAGE', 2),
(16, 'Concierto Íntimo y Acústico', 'Barcelona', 'Barcelona', '2022-11-12', 'COMPLETED', true, 30.00, 2.1734, 41.3851, '19:30:00', 'Barts Sala', 'Avinguda del Paral·lel 62', 1500, 'Privado', 'Aritz Trío', 'Platea', 1350, 'FLAT', 2),
(17, 'Gira El Despertar 2023', 'Valencia', 'Valencia', '2023-02-18', 'COMPLETED', true, 22.50, -0.3763, 39.4699, '21:00:00', 'Sala Moon', 'Calle San Vicente Martir 200', 1000, 'Privado', 'Aritz & Banda', 'General', 950, 'FLAT', 2),
(18, 'Fiestas de San Isidro Especial', 'Madrid', 'Madrid', '2023-05-15', 'COMPLETED', true, 0.00, -3.7145, 40.4192, '21:30:00', 'Plaza Mayor', 'Plaza Mayor de Madrid', 10000, 'Ayto Madrid', 'Aritz Folk Band', 'Gratuito', 10000, 'FLAT', 2),
(19, 'Directo en el Norte', 'Bilbao', 'Vizcaya', '2023-08-22', 'COMPLETED', true, 18.00, -2.9350, 43.2630, '22:00:00', 'Kafe Antzokia', 'Done Bikendi Kalea 2', 800, 'Privado', 'Aritz Band', 'General', 780, 'PERCENTAGE', 2),
(20, 'Fin de Gira El Despertar', 'Zaragoza', 'Zaragoza', '2023-10-21', 'COMPLETED', true, 28.00, -0.8891, 41.6488, '21:00:00', 'Teatro de las Esquinas', 'Via Universitas 30', 1000, 'Privado', 'Aritz & Amigos', 'Platea', 1000, 'PERCENTAGE', 2),
(21, 'Presentación Oficial El Eco 2024', 'Sevilla', 'Sevilla', '2024-03-02', 'COMPLETED', true, 25.00, -5.9845, 37.3891, '20:30:00', 'Sala Custom', 'Calle Metalurgia 25', 800, 'Privado', 'Aritz Solo', 'General', 710, 'FLAT', 2),
(22, 'Gira El Eco - Ciclo de Teatros', 'Murcia', 'Murcia', '2024-05-17', 'COMPLETED', true, 35.00, -1.1307, 37.9838, '20:00:00', 'Teatro Romea', 'Plaza Julián Romea s/n', 1100, 'Ayto Murcia', 'Aritz Quintet', 'Butaca', 1050, 'FLAT', 2),
(23, 'Directo de Verano Costa del Sol', 'Málaga', 'Málaga', '2024-07-19', 'COMPLETED', true, 45.00, -4.4214, 36.7213, '23:00:00', 'Auditorio Municipal', 'Cortijo de Torres s/n', 8000, 'Ayto Málaga', 'Aritz & Banda', 'General', 7600, 'PERCENTAGE', 2),
(24, 'Concierto de las Fiestas del Pilar', 'Zaragoza', 'Zaragoza', '2024-10-11', 'COMPLETED', true, 30.00, -0.8891, 41.6488, '22:00:00', 'Pabellón Príncipe Felipe', 'Av. Cesáreo Alierta 120', 12000, 'Ayto Zaragoza', 'Aritz & Heavy Band', 'General', 11800, 'PERCENTAGE', 2),

-- Conciertos Presentes / Futuros (PROGRAMMED / PENDING)
(25, 'Gira El Eco Eléctrico 2025', 'Salamanca', 'Salamanca', '2025-02-14', 'PROGRAMMED', true, 25.00, -5.6635, 40.9688, '21:00:00', 'Palacio de Congresos', 'Plaza de San Román', 1200, 'Junta CyL', 'Aritz Rock', 'General', 950, 'FLAT', 2),
(26, 'Especial Día de San Jorge', 'Huesca', 'Huesca', '2025-04-23', 'PROGRAMMED', true, 15.00, -0.4084, 42.1362, '19:00:00', 'Centro Cultural Manuel Benito', 'Plaza Alcalde José Luis Rubió', 500, 'Ayto Huesca', 'Aritz Acústico', 'General', 450, 'FLAT', 2),
(27, 'Open Air Festival Nocturno', 'Alicante', 'Alicante', '2025-06-28', 'PROGRAMMED', false, 35.00, -0.4815, 38.3452, '22:00:00', 'Plaza de Toros Alicante', 'Plaza de España 7', 8000, 'Privado', 'Aritz Tour Eléctrico', 'Ruedo', 4200, 'PERCENTAGE', 2),
(28, 'Gira El Eco - Cierre de Verano', 'Santander', 'Cantabria', '2025-09-05', 'PROGRAMMED', false, 28.00, -3.8094, 43.4623, '21:30:00', 'Escenario Santander', 'Parque de las Llamas', 1000, 'Ayto Santander', 'Aritz & Banda', 'General', 600, 'PERCENTAGE', 2),
(29, 'Acústico de Otoño', 'Teruel', 'Teruel', '2025-11-08', 'PENDING', false, 20.00, -1.1065, 40.3456, '20:00:00', 'Teatro Marín', 'Plaza de San Juan 3', 600, 'Ayto Teruel', 'Aritz Solo', 'Platea', 0, 'FLAT', 2),
(30, 'Concierto Especial de Año Nuevo', 'Logroño', 'La Rioja', '2026-01-03', 'PENDING', false, 32.00, -2.4456, 42.4667, '19:30:00', 'Riojaforum', 'Calle San Millán 25', 1200, 'Gobierno Rioja', 'Aritz Symphonic', 'Anfiteatro', 0, 'FLAT', 2),
(31, 'Grandes Éxitos en Vivo 2026', 'Burgos', 'Burgos', '2026-05-22', 'PENDING', false, 26.00, -3.6969, 42.3440, '21:00:00', 'Sala Andén 56', 'Calle San Pedro y San Felices', 1000, 'Privado', 'Aritz Electro Band', 'General', 0, 'PERCENTAGE', 2),
(32, 'Gira Universo Infinito 2027', 'Pamplona', 'Navarra', '2027-02-12', 'PENDING', false, 40.00, -1.6432, 42.8125, '22:00:00', 'Navarra Arena', 'Plaza de Aizagerria 1', 10000, 'Gob de Navarra', 'Aritz Big Band', 'General', 0, 'PERCENTAGE', 2);

-- =============================================================================
-- 5. SEGURIDAD Y ACCESOS (Con el prefijo $2a$ compatible con tu Spring Security)
-- =============================================================================

-- Este admin usa el formato $2a$ y la contraseña en texto plano es: admin1234
INSERT IGNORE INTO `users` (username, password, role)
VALUES ('admin', '$2b$10$gpxxv4hl/A0aT/IcjmDjpOJ1MMxIQRlRjtH9122l/TYQk8T/J9HYW', 'ROLE_ADMIN');

-- Usuarios vinculados a los músicos
INSERT IGNORE INTO `users` (id, username, password, role, musician_id)
VALUES (1, 'admin_musician', '$2b$10$SVfm6kRrcAhNOMVirm71rOgLzFGkiiEGX.Cej4gmJRJnKV/3d2cne', 'ROLE_ADMIN', 1);

INSERT IGNORE INTO `users` (id, username, password, role, musician_id)
VALUES (2, 'Aritz', '$2b$10$.zia4ocy6de84q0/nqL.VeLl2jLDNEzMUMLgQOtW3D1FOu/2RZAB6', 'ROLE_MUSICIAN', 2);