package com.VentaMex.apiVentaMex.presentation.api;

import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Validated
@RequestMapping("/api/history")
public interface ReportHistorySalesAPI {
    @GetMapping("/pdf")
    ResponseEntity<Resource> exportarHistorialVentasPdf(@RequestParam LocalDate fechaInicio,
                                                        @RequestParam LocalDate fechaFin) throws IOException;

    @GetMapping("/excell")
    ResponseEntity<Resource> exportarHistorialExcel(@RequestParam LocalDate fechaInicio,
                                                    @RequestParam LocalDate fechaFin) throws IOException;
}
