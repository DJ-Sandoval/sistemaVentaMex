package com.VentaMex.apiVentaMex.presentation.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(ReportesPDFAPI.BASE_URL)
public interface ReportesPDFAPI {
    String BASE_URL="/api/reportes";

    @GetMapping(value = "/productos-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> exportarProductosPDF(HttpServletRequest request) throws Exception;

    @GetMapping(value = "/clientes-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> exportarClientesPDF(HttpServletRequest request) throws Exception;

    @GetMapping(value = "/ventas-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> exportarVentasPDF(HttpServletRequest request) throws Exception;

    @GetMapping(value = "/categorias-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> exportarCategoriasPDF(HttpServletRequest request) throws Exception;

    @GetMapping(value = "/medidas-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> exportarMedidasPDF(HttpServletRequest request) throws Exception;
}
