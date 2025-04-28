package com.VentaMex.apiVentaMex.presentation.api;

import com.VentaMex.apiVentaMex.presentation.dto.VentaRequestDTO;
import com.VentaMex.apiVentaMex.presentation.dto.VentaResponseDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequestMapping(SaleAPI.BASE_URL)
@Tag(name = "API de Ventas", description = "Operaciones sobre la api de ventas")
public interface SaleAPI {
    String BASE_URL="/api/ventas";

    @PostMapping
    ResponseEntity<VentaResponseDTO> registrarVenta(@Valid @RequestBody VentaRequestDTO ventaRequest);

    @GetMapping
    ResponseEntity<Page<VentaResponseDTO>> listarVentas(
            @RequestParam(required = false) String search,
            @ParameterObject Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<VentaResponseDTO> obtenerVentaPorId(@PathVariable Long id);

    @GetMapping("/cliente/{clienteId}")
    ResponseEntity<List<VentaResponseDTO>> obtenerVentasPorCliente(@PathVariable Long clienteId);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminarVenta(
            @Parameter(description = "ID de la venta a eliminar", required = true) @PathVariable Long id);
}
