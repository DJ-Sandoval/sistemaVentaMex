package com.VentaMex.apiVentaMex.presentation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Validated
@RequestMapping(ReportesExcellAPI.BASE_URL)
public interface ReportesExcellAPI {
    String BASE_URL="/api/reportes-excell";

    @GetMapping("/productos")
    public ResponseEntity<byte[]> descargarReporteProductos() throws IOException;
}
