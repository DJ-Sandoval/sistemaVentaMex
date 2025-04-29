package com.VentaMex.apiVentaMex.service.reports.excell;

import com.VentaMex.apiVentaMex.presentation.dto.HistorialVentaDTO;
import com.VentaMex.apiVentaMex.service.interfaces.IHistoryVentasExcell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class HistorialVentasExcelService implements IHistoryVentasExcell {
    @Override
    public void exportarHistorialVentasExcel(List<HistorialVentaDTO> historial) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Historial Ventas");

            // Crear carpeta si no existe
            File carpeta = new File("C:/historialVentas");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String rutaArchivo = "C:/historialVentas/reporte_historial.xlsx";

            // Estilo del header: azul Docker (#0db7ed) con letras blancas
            CellStyle headerStyle = workbook.createCellStyle();
            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Crear fila de encabezado
            Row headerRow = sheet.createRow(0);
            String[] columnas = {"ID Cliente", "Nombre", "Ventas", "Total Compras", "Última Compra"};

            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Estilo de celdas normales
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT);

            // Llenar datos
            int rowNum = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (HistorialVentaDTO h : historial) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(h.getIdCliente());
                row.createCell(1).setCellValue(h.getNombreCliente());
                row.createCell(2).setCellValue(h.getCantidadVentas());
                row.createCell(3).setCellValue(h.getTotalCompras());
                row.createCell(4).setCellValue(h.getUltimaCompra().format(formatter));
            }

            // Ajustar tamaño columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
