package com.VentaMex.apiVentaMex.presentation.controller;

import com.VentaMex.apiVentaMex.presentation.api.ReportesExcellAPI;
import com.VentaMex.apiVentaMex.presentation.dto.ConceptoSimpleDTO;
import com.VentaMex.apiVentaMex.presentation.dto.ProductoDTO;
import com.VentaMex.apiVentaMex.service.interfaces.IClienteService;
import com.VentaMex.apiVentaMex.service.interfaces.IProductoService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestController
public class ReportesExcellReportController  implements ReportesExcellAPI {
    @Autowired
    private IProductoService productoService;

    @Autowired
    private IClienteService clienteService;


    @Override
    public ResponseEntity<byte[]> descargarReporteProductos() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Listado de Productos");

        // Estilos
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle titleStyle = createTitleStyle(workbook);

        // Título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Listado de Productos");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        // Encabezados
        Row headerRow = sheet.createRow(2);
        String[] headers = {"ID", "Nombre", "Precio Unitario", "Costo"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos
        Page<ProductoDTO> productos = productoService.obtenerTodosLosProductos(PageRequest.of(0, Integer.MAX_VALUE));
        int rowNum = 3;
        for (ProductoDTO producto : productos.getContent()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(producto.getId());
            row.createCell(1).setCellValue(producto.getNombre());
            row.createCell(2).setCellValue(producto.getPrecioUnitario());
            row.createCell(3).setCellValue(producto.getCosto());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Crear nombre de archivo
        String fileName = "Reporte_Productos_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

        // Escribir a byte[]
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // ✅ Guardar en disco
        String folderPath = "C:/reportes";
        File carpeta = new File(folderPath);
        if (!carpeta.exists()) carpeta.mkdirs();

        FileOutputStream fos = new FileOutputStream(folderPath + "/" + fileName);
        fos.write(outputStream.toByteArray());
        fos.close();

        // ✅ Enviar como descarga al navegador
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headersResponse.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(outputStream.toByteArray());
    }


    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        style.setFont(font);

        // Usa un color permitido (por ejemplo, BLUE o CORNFLOWER_BLUE)
        style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }


    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
