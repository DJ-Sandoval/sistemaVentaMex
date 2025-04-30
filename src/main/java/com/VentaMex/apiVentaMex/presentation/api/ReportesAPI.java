package com.VentaMex.apiVentaMex.presentation.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequestMapping(ReportesAPI.BASE_URL)
public interface ReportesAPI {
    String BASE_URL="/api/reportes";

    @GetMapping(value = "/productos-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> exportarProductosPDF(HttpServletRequest request) throws Exception;

    @GetMapping(value = "/clientes-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> exportarClientesPDF(HttpServletRequest request) throws Exception;

    @GetMapping(value = "/ventas-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> exportarVentasPDF(HttpServletRequest request) throws Exception;
}
