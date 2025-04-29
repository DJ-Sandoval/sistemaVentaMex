-- Consulta para historial de ventas con filtros y totales por cliente
SELECT
    v.id_cliente,
    c.nombre AS nombre_cliente,  -- Asumo que hay una tabla cliente con campo nombre
    COUNT(v.id) AS cantidad_ventas,
    SUM(v.total) AS total_compras,
    MIN(v.fecha) AS primera_compra,
    MAX(v.fecha) AS ultima_compra
FROM
    venta v
    JOIN cliente c ON v.id_cliente = c.id  -- Asumo que existe esta relación
WHERE
    -- Filtros opcionales (puedes comentar/descomentar según necesites)
    (v.fecha BETWEEN '2025-04-01' AND '2025-04-30')  -- Filtro por rango de fechas
    -- AND v.id_cliente = 3  -- Filtro por cliente específico
GROUP BY
    v.id_cliente, c.nombre
ORDER BY
    total_compras DESC;