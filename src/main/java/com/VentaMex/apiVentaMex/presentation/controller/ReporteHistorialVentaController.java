package com.VentaMex.apiVentaMex.presentation.controller;

import com.VentaMex.apiVentaMex.presentation.api.ReportHistorySalesAPI;
import com.VentaMex.apiVentaMex.presentation.dto.HistorialVentaDTO;
import com.VentaMex.apiVentaMex.service.interfaces.VentaService;
import com.VentaMex.apiVentaMex.service.reports.excell.HistorialVentasExcelService;
import com.VentaMex.apiVentaMex.service.reports.pdf.HistorialVentasPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

@RestController
public class ReporteHistorialVentaController implements ReportHistorySalesAPI {
    @Autowired
    private HistorialVentasPdfService pdfService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private HistorialVentasExcelService excelService;

    @Override
    public ResponseEntity<Resource> exportarHistorialVentasPdf(@RequestParam LocalDate fechaInicio,
                                                               @RequestParam LocalDate fechaFin) throws IOException {
        List<HistorialVentaDTO> historial = ventaService.obtenerHistorialVentas(fechaInicio, fechaFin);
        pdfService.exportarHistorialVentasPdf(historial);

        File archivo = new File("C:/historialVentas/reporte_historial.pdf");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(archivo));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_historial.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(archivo.length())
                .body(resource);
    }

    @Override
    public ResponseEntity<Resource> exportarHistorialExcel(@RequestParam LocalDate fechaInicio,
                                                           @RequestParam LocalDate fechaFin) throws IOException {
        List<HistorialVentaDTO> historial = ventaService.obtenerHistorialVentas(fechaInicio, fechaFin);
        excelService.exportarHistorialVentasExcel(historial);

        File archivo = new File("C:/historialVentas/reporte_historial.xlsx");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(archivo));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_historial.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(archivo.length())
                .body(resource);
    }


}
