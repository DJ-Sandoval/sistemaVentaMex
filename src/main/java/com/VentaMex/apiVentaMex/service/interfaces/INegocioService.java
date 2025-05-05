package com.VentaMex.apiVentaMex.service.interfaces;

import com.VentaMex.apiVentaMex.persistence.entities.NegocioEntity;

import java.util.List;
import java.util.Optional;

public interface INegocioService {
    NegocioEntity crearNegocio(NegocioEntity negocio);
    NegocioEntity actualizarNegocio(Long id, NegocioEntity negocio);
    Optional<NegocioEntity> obtenerNegocioPorId(Long id);
    List<NegocioEntity> obtenerTodosNegocios();
}
