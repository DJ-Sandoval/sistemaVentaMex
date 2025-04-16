package com.VentaMex.apiVentaMex.service.interfaces;
import com.VentaMex.apiVentaMex.persistence.entities.Producto;
import com.VentaMex.apiVentaMex.persistence.entities.Venta;
import com.VentaMex.apiVentaMex.presentation.dto.VentaRequestDTO;
import com.VentaMex.apiVentaMex.presentation.dto.VentaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface VentaService {
    Page<Venta> obtenerTodasLasVentas(Pageable pageable);
    VentaResponseDTO registrarVenta(VentaRequestDTO ventaRequest);
    VentaResponseDTO obtenerVentaPorId(Long id);
    List<VentaResponseDTO> obtenerVentasPorCliente(Long clienteId);
}
