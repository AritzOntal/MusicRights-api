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
(15, 'Lady Blue', 'ESBUN0200777', 'Glam Rock', 5.30, '2002-03-25', true);

-- =============================================================================
-- 3. RELACIÓN COMPUESTA (Tabla intermedia: musician_work)
-- =============================================================================
INSERT IGNORE INTO musician_work (work_id, musician_id) VALUES 
(1, 1), (2, 2), (3, 3), (4, 4), (4, 1), (6, 5), (7, 5), 
(8, 6), (9, 6), (10, 7), (11, 7), (12, 8), (13, 9), (14, 10), (15, 10);

-- =============================================================================
-- 4. REGISTRO DE CONCIERTOS (Tabla: concerts)
-- =============================================================================
INSERT IGNORE INTO concerts (
    id, show_title, city, province, date, status, performed, ticket_price, 
    longitude, latitude, time, venue_name, venue_address, capacity, 
    venue_owner, performers, ticket_class, total_tickets, tariff_type, musician_id
)
VALUES 
(1, 'Gira Santana 2025', 'Zaragoza', 'Zaragoza', '2025-06-15', 'PROGRAMMED', true, 45.50, -0.8891, 41.6488, '21:30:00', 'Pabellón Príncipe Felipe', 'Av. de Cesáreo Alierta 120', 10000, 'Ayto Zaragoza', 'Carlos Santana Band', 'General', 8500, 'PERCENTAGE', 1),
(2, 'Rosalía Motomami En Vivo', 'Sevilla', 'Sevilla', '2026-09-20', 'PROGRAMMED', false, 80.00, -5.9845, 37.3891, '22:30:00', 'Estadio de La Cartuja', 'Isla de la Cartuja s/n', 60000, 'Junta de Andalucía', 'Rosalía', 'Golden Ring', 55000, 'PERCENTAGE', 4),
(3, 'Fito & Fitipaldis Teatros', 'Bilbao', 'Vizcaya', '2024-11-12', 'COMPLETED', true, 50.00, -2.9350, 43.2630, '20:30:00', 'Teatro Arriaga', 'Plaza Arriaga 1', 1200, 'Ayto Bilbao', 'Fito y los Fitipaldis', 'Platea', 1150, 'FLAT', 5),
(4, 'El Madrileño Open Air', 'Valencia', 'Valencia', '2025-07-04', 'COMPLETED', true, 65.00, -0.3763, 39.4699, '22:00:00', 'Marina de Valencia', 'Carrer del Moll de la Duana', 25000, 'Consorcio Marina', 'C. Tangana', 'General', 24200, 'PERCENTAGE', 7),
(5, 'Fito Gira 20 Aniversario', 'Santander', 'Cantabria', '2021-09-10', 'COMPLETED', true, 35.00, -3.8094, 43.4623, '21:00:00', 'Campa de la Magdalena', 'Av. de la Magdalena', 12000, 'Ayto Santander', 'Fito & Fitipaldis', 'General', 11000, 'PERCENTAGE', 5),
(6, 'KaseO Jazz Magnetism Tour', 'Granada', 'Granada', '2021-08-05', 'COMPLETED', true, 28.00, -3.5986, 37.1773, '22:00:00', 'Plaza de Toros', 'Av. de Doctor Olóriz 25', 9000, 'Chopera Toros', 'KaseO + Banda', 'Ruedo', 8200, 'FLAT', 6),
(7, 'Chano Domínguez Trío Jazz', 'Cádiz', 'Cádiz', '2021-12-23', 'COMPLETED', true, 22.00, -6.2925, 36.5271, '19:30:00', 'Gran Teatro Falla', 'Plaza de Fragela s/n', 1000, 'Ayto Cádiz', 'Chano Domínguez', 'Anfiteatro', 950, 'FLAT', 8),
(8, 'Amaral Fin de Gira Nocturnal', 'Madrid', 'Madrid', '2021-05-29', 'PENDING', true, 30.00, -3.7038, 40.4167, '22:00:00', 'WiZink Center', 'Av. Felipe II s/n', 15000, 'Comunidad de Madrid', 'Amaral', 'Pista', 14000, 'PERCENTAGE', 2),
(9, 'Bunbury Mutaciones Especial', 'Huesca', 'Huesca', '2021-05-27', 'PENDING', true, 40.00, -0.4084, 42.1362, '21:30:00', 'Plaza de Toros Huesca', 'Calle de la Palma', 5000, 'Ayto Huesca', 'Bunbury', 'General', 4800, 'PERCENTAGE', 10),
(10, 'Judas Priest Nostalgia Tour', 'Barcelona', 'Barcelona', '2020-03-10', 'ARCHIVED', true, 60.00, 2.1734, 41.3851, '20:00:00', 'Palau Sant Jordi', 'Passeig Olímpic 5', 18000, 'Barcelona Serveis', 'Judas Priest + Guest', 'General', 17500, 'FLAT', 3),
(11, 'KaseO El Círculo Arena', 'Zaragoza', 'Zaragoza', '2018-10-12', 'ARCHIVED', true, 25.00, -0.8891, 41.6488, '22:00:00', 'Pabellón Príncipe Felipe', 'Av. Cesáreo Alierta', 12000, 'Ayto Zaragoza', 'Kase.O', 'General', 12000, 'PERCENTAGE', 6),
(12, 'Sílvia Pérez Cruz Íntimo', 'Girona', 'Girona', '2020-07-18', 'ARCHIVED', true, 35.00, 2.8214, 41.9794, '20:00:00', 'Auditori de Girona', 'Passeig de la Devesa 35', 1500, 'Auditori G.', 'Sílvia Pérez Cruz', 'Platea', 1420, 'FLAT', 9);

-- =============================================================================
-- 5. SEGURIDAD Y ACCESOS (Manejo de palabras reservadas del motor)
-- =============================================================================

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
VALUES (2, 'eva_amaral', '$2b$10$aLeuhpYrvuDkSndppDS.kOSuCTvE49s6TULNiwAlspgtHxRzNyhP6', 'ROLE_MUSICIAN', 2);