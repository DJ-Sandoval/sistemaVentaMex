package com.VentaMex.apiVentaMex.persistence.repository;

import com.VentaMex.apiVentaMex.persistence.entities.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Page<Producto> findAll(Pageable pageable);
    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.categoria LEFT JOIN FETCH p.medida")
    Page<Producto> findAllWithCategoriesAndMeasures(Pageable pageable);
}
