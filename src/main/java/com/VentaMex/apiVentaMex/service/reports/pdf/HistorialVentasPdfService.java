package com.VentaMex.apiVentaMex.service.reports.pdf;
import com.VentaMex.apiVentaMex.presentation.dto.HistorialVentaDTO;
import com.VentaMex.apiVentaMex.service.interfaces.IHistorialService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class HistorialVentasPdfService implements IHistorialService {

    @Override
    public void exportarHistorialVentasPdf(List<HistorialVentaDTO> historial) {
        try {
            // Crear carpeta si no existe
            File carpeta = new File("C:/historialVentas");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String rutaArchivo = "C:/historialVentas/reporte_historial.pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
            document.open();

            // Agregar imagen
            Image logo = Image.getInstance(new ClassPathResource("static/historialVentas.png").getURL());
            logo.scaleToFit(100, 100);
            logo.setAlignment(Image.LEFT);
            document.add(logo);

            // Título alineado a la derecha
            Paragraph titulo = new Paragraph("Reporte de historial de ventas", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            titulo.setAlignment(Element.ALIGN_RIGHT);
            document.add(titulo);
            document.add(new Paragraph("\n"));

            // Tabla
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 4f, 2f, 3f, 4f});
            table.addCell("ID Cliente");
            table.addCell("Nombre");
            table.addCell("Ventas");
            table.addCell("Total Compras");
            table.addCell("Última Compra");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (HistorialVentaDTO h : historial) {
                table.addCell(String.valueOf(h.getIdCliente()));
                table.addCell(h.getNombreCliente());
                table.addCell(String.valueOf(h.getCantidadVentas()));
                table.addCell(String.format("$%.2f", h.getTotalCompras()));
                table.addCell(h.getUltimaCompra().format(formatter));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
