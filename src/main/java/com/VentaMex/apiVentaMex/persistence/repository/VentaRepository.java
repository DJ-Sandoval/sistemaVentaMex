package com.VentaMex.apiVentaMex.persistence.repository;

import com.VentaMex.apiVentaMex.persistence.entities.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    @EntityGraph(attributePaths = {"cliente", "conceptos", "conceptos.producto"})
    List<Venta> findByClienteId(Long clienteId);
    @EntityGraph(attributePaths = {"cliente", "conceptos", "conceptos.producto"})
    Optional<Venta> findWithConceptosById(Long id);
    Page<Venta> findByClienteNombreContainingIgnoreCase(String nombreCliente, Pageable pageable);
    @Query(value = """
        SELECT 
            v.id_cliente AS idCliente,
            c.nombre AS nombreCliente,
            COUNT(v.id) AS cantidadVentas,
            SUM(v.total) AS totalCompras,
            MIN(v.fecha) AS primeraCompra,
            MAX(v.fecha) AS ultimaCompra
        FROM venta v
        JOIN cliente c ON v.id_cliente = c.id
        WHERE (:fechaInicio IS NULL OR v.fecha >= :fechaInicio)
          AND (:fechaFin IS NULL OR v.fecha <= :fechaFin)
        GROUP BY v.id_cliente, c.nombre
        ORDER BY totalCompras DESC
        """, nativeQuery = true)
    List<Map<String, Object>> obtenerHistorialVentasPorFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
}
