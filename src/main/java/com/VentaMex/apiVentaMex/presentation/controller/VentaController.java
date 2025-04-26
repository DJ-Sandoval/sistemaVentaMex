package com.VentaMex.apiVentaMex.presentation.controller;
import com.VentaMex.apiVentaMex.persistence.entities.Venta;
import com.VentaMex.apiVentaMex.presentation.api.SaleAPI;
import com.VentaMex.apiVentaMex.presentation.dto.VentaDTO;
import com.VentaMex.apiVentaMex.presentation.dto.VentaRequestDTO;
import com.VentaMex.apiVentaMex.presentation.dto.VentaResponseDTO;
import com.VentaMex.apiVentaMex.service.exception.VentaException;
import com.VentaMex.apiVentaMex.service.exception.VentaNotFoundException;
import com.VentaMex.apiVentaMex.service.interfaces.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VentaController implements SaleAPI {
    private final VentaService ventaService;

    @Override
    public ResponseEntity<VentaResponseDTO> registrarVenta(@Valid @RequestBody VentaRequestDTO ventaRequest) {
        VentaResponseDTO ventaResponse = ventaService.registrarVenta(ventaRequest);
        return new ResponseEntity<>(ventaResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Page<VentaResponseDTO>> listarVentas(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ventaService.obtenerTodasLasVentas(pageable));
    }

    @Override
    public ResponseEntity<VentaResponseDTO> obtenerVentaPorId(@PathVariable Long id) {
        VentaResponseDTO ventaResponse = ventaService.obtenerVentaPorId(id);
        return ResponseEntity.ok(ventaResponse);
    }

    @Override
    public ResponseEntity<List<VentaResponseDTO>> obtenerVentasPorCliente(@PathVariable Long clienteId) {
        List<VentaResponseDTO> ventas = ventaService.obtenerVentasPorCliente(clienteId);
        return ResponseEntity.ok(ventas);
    }

    @Override
    public ResponseEntity<Void> eliminarVenta(Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(VentaException.class)
    public ResponseEntity<String> handleVentaException(VentaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
