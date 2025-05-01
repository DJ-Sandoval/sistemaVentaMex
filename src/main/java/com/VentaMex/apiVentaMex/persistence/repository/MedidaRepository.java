package com.VentaMex.apiVentaMex.persistence.repository;

import com.VentaMex.apiVentaMex.persistence.entities.Medida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaRepository extends JpaRepository<Medida, Long> {
    Page<Medida> findAll(Pageable pageable);
}
