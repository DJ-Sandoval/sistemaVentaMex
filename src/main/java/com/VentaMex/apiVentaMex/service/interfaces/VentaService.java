package com.VentaMex.apiVentaMex.service.interfaces;
import com.VentaMex.apiVentaMex.persistence.entities.Producto;
import com.VentaMex.apiVentaMex.persistence.entities.Venta;
import com.VentaMex.apiVentaMex.presentation.dto.VentaRequestDTO;
import com.VentaMex.apiVentaMex.presentation.dto.VentaResponseDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface VentaService {
    Page<VentaResponseDTO> obtenerTodasLasVentas(Pageable pageable);
    VentaResponseDTO registrarVenta(VentaRequestDTO ventaRequest);
    @Cacheable(value = "ventas", key = "#id")
    VentaResponseDTO obtenerVentaPorId(Long id);
    List<VentaResponseDTO> obtenerVentasPorCliente(Long clienteId);
    @CacheEvict(value = "ventas", key = "#id")
    void eliminarVenta(Long id);
    Page<VentaResponseDTO> buscarVentas(String search, Pageable pageable);
}
