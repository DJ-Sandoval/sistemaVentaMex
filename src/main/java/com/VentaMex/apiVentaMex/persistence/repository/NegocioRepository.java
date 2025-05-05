package com.VentaMex.apiVentaMex.persistence.repository;

import com.VentaMex.apiVentaMex.persistence.entities.NegocioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NegocioRepository extends JpaRepository<NegocioEntity, Long> {
}
