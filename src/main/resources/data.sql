-- Consulta básica para obtener todas las ventas
SELECT id, fecha, total, id_cliente, ruta_ticket
FROM venta;

-- Consulta con filtros por fecha y cliente
SELECT id, fecha, total, id_cliente, ruta_ticket
FROM venta
WHERE
    (fecha BETWEEN '2025-04-01' AND '2025-04-30') -- Filtro por rango de fechas
    AND (id_cliente = 3 OR id_cliente IS NULL); -- Filtro por cliente (puede ser NULL)


-- Consulta con parámetros variables (para usar en una aplicación)
SELECT id, fecha, total, id_cliente, ruta_ticket
FROM ventas
WHERE
    (fecha BETWEEN '2025-04-01' AND '2025-04-30')
    AND (id_cliente = 3 OR 3 IS NULL)
LIMIT 0, 25;


-- Consulta agrupada por cliente con totales
SELECT
    id_cliente,
    COUNT(*) AS cantidad_ventas,
    SUM(total) AS total_compras
FROM venta
WHERE fecha BETWEEN '2025-04-01' AND '2025-04-30'
GROUP BY id_cliente
LIMIT 0, 25;

-- Consulta para buscar ventas en un día específico
SELECT id, fecha, total, id_cliente, ruta_ticket
FROM venta
WHERE DATE(fecha) = '2025-04-23';