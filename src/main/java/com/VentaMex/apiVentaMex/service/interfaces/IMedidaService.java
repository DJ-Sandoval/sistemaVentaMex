package com.VentaMex.apiVentaMex.service.interfaces;

import com.VentaMex.apiVentaMex.presentation.dto.CategoriaDTO;
import com.VentaMex.apiVentaMex.presentation.dto.MedidaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMedidaService {
    MedidaDTO crearMedida(MedidaDTO medidaDTO);
    MedidaDTO obtenerMedidaPorId(Long id);
    Page<MedidaDTO> obtenerTodasMedidas(Pageable pageable);
    MedidaDTO actualizarMedida(Long id, MedidaDTO MedidaDTO);
    void eliminarMedida(Long id);
}
