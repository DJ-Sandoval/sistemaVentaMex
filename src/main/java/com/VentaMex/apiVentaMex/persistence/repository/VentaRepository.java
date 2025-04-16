package com.VentaMex.apiVentaMex.persistence.repository;

import com.VentaMex.apiVentaMex.persistence.entities.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    @EntityGraph(attributePaths = {"cliente", "conceptos", "conceptos.producto"})
    List<Venta> findByClienteId(Long clienteId);
    @EntityGraph(attributePaths = {"cliente", "conceptos", "conceptos.producto"})
    Optional<Venta> findWithConceptosById(Long id);

}
