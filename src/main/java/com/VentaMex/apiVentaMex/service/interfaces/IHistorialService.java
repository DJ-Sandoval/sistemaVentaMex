package com.VentaMex.apiVentaMex.service.interfaces;

import com.VentaMex.apiVentaMex.presentation.dto.HistorialVentaDTO;

import java.util.List;

public interface IHistorialService {
    void exportarHistorialVentasPdf(List<HistorialVentaDTO> historial);
}
