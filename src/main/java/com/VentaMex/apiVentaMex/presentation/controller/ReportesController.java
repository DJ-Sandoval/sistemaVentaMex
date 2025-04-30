package com.VentaMex.apiVentaMex.presentation.controller;
import com.VentaMex.apiVentaMex.presentation.api.ReportesAPI;
import com.VentaMex.apiVentaMex.presentation.dto.ClienteDTO;
import com.VentaMex.apiVentaMex.presentation.dto.ConceptoSimpleDTO;
import com.VentaMex.apiVentaMex.presentation.dto.ProductoDTO;
import com.VentaMex.apiVentaMex.presentation.api.ReportesAPI;
import com.VentaMex.apiVentaMex.presentation.dto.VentaResponseDTO;
import com.VentaMex.apiVentaMex.service.interfaces.IClienteService;
import com.VentaMex.apiVentaMex.service.interfaces.IProductoService;
import com.VentaMex.apiVentaMex.service.interfaces.VentaService;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class ReportesController implements ReportesAPI {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private VentaService ventaService;

    @Override
    public ResponseEntity<byte[]> exportarProductosPDF(HttpServletRequest request) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        // Ruta de imagen (debe estar en src/main/resources/static/logo.png por ejemplo)
        ClassPathResource imgFile = new ClassPathResource("static/productos.png");
        try (InputStream imgStream = imgFile.getInputStream()) {
            Image logo = Image.getInstance(imgStream.readAllBytes());
            logo.scaleToFit(100, 100);
            logo.setAlignment(Image.LEFT);
            document.add(logo);
        }

        // Título centrado a la derecha
        Paragraph titulo = new Paragraph("Reporte de productos", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" ")); // Espacio

        // Tabla
        PdfPTable tabla = new PdfPTable(4); // ID, Nombre, Precio, Costo
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10f);

        // Color Docker #0db7ed
        BaseColor dockerBlue = new BaseColor(13, 183, 237);

        Stream.of("ID", "Nombre", "Precio Unitario", "Costo")
                .forEach(header -> {
                    PdfPCell headerCell = new PdfPCell();
                    headerCell.setBackgroundColor(dockerBlue);
                    headerCell.setPhrase(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
                    tabla.addCell(headerCell);
                });

        List<ProductoDTO> productos = productoService.obtenerTodosLosProductos(PageRequest.of(0, 100)).getContent();
        for (ProductoDTO p : productos) {
            tabla.addCell(p.getId().toString());
            tabla.addCell(p.getNombre());
            tabla.addCell(p.getPrecioUnitario().toString());
            tabla.addCell(p.getCosto().toString());
        }

        document.add(tabla);
        document.close();

        // Guardar copia en disco C:/reportesSistema
        File carpeta = new File("C:/reportes");
        if (!carpeta.exists()) carpeta.mkdirs();

        String filePath = "C:/reportes/reporte_productos_" + System.currentTimeMillis() + ".pdf";
        Files.write(Paths.get(filePath), baos.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_productos.pdf");

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> exportarClientesPDF(HttpServletRequest request) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        // Imagen del encabezado
        ClassPathResource imgFile = new ClassPathResource("static/clientes.png"); // o usa logo.png si es el mismo
        try (InputStream imgStream = imgFile.getInputStream()) {
            Image logo = Image.getInstance(imgStream.readAllBytes());
            logo.scaleToFit(100, 100);
            logo.setAlignment(Image.LEFT);
            document.add(logo);
        }

        // Título
        Paragraph titulo = new Paragraph("Reporte de Clientes", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph(" ")); // Espacio

        // Tabla
        PdfPTable tabla = new PdfPTable(2); // ID, Nombre
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10f);

        BaseColor azul = new BaseColor(13, 183, 237);
        Stream.of("ID", "Nombre")
                .forEach(header -> {
                    PdfPCell headerCell = new PdfPCell();
                    headerCell.setBackgroundColor(azul);
                    headerCell.setPhrase(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
                    tabla.addCell(headerCell);
                });

        List<ClienteDTO> clientes = clienteService.obtenerTodosClientes(PageRequest.of(0, 100)).getContent();
        for (ClienteDTO c : clientes) {
            tabla.addCell(c.getId().toString());
            tabla.addCell(c.getNombre());
        }

        document.add(tabla);
        document.close();

        // Guardar copia local
        File carpeta = new File("C:/reportes");
        if (!carpeta.exists()) carpeta.mkdirs();

        String filePath = "C:/reportes/reporte_clientes_" + System.currentTimeMillis() + ".pdf";
        Files.write(Paths.get(filePath), baos.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_clientes.pdf");

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> exportarVentasPDF(HttpServletRequest request) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate()); // Usamos orientación horizontal
        PdfWriter.getInstance(document, baos);
        document.open();

        // Logo o imagen de encabezado
        try {
            ClassPathResource imgFile = new ClassPathResource("static/ventas.png");
            if (imgFile.exists()) {
                try (InputStream imgStream = imgFile.getInputStream()) {
                    Image logo = Image.getInstance(imgStream.readAllBytes());
                    logo.scaleToFit(100, 100);
                    logo.setAlignment(Image.LEFT);
                    document.add(logo);
                }
            }
        } catch (Exception e) {
            // Si no hay imagen, continuar sin ella
            System.err.println("No se encontró la imagen de ventas: " + e.getMessage());
        }

        // Título del reporte
        Paragraph titulo = new Paragraph("Reporte de Ventas",
                new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        // Fecha de generación
        Paragraph fechaGen = new Paragraph("Generado el: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                new Font(Font.FontFamily.HELVETICA, 10));
        fechaGen.setAlignment(Element.ALIGN_RIGHT);
        document.add(fechaGen);

        document.add(new Paragraph(" ")); // Espacio

        // Tabla principal de ventas
        PdfPTable tablaVentas = new PdfPTable(5); // ID, Fecha, Cliente, Total, Conceptos
        tablaVentas.setWidthPercentage(100);
        tablaVentas.setSpacingBefore(10f);

        // Color para encabezados
        BaseColor colorEncabezado = new BaseColor(13, 183, 237);

        // Encabezados de la tabla
        Stream.of("ID", "Fecha", "Cliente", "Total", "Productos")
                .forEach(header -> {
                    PdfPCell headerCell = new PdfPCell();
                    headerCell.setBackgroundColor(colorEncabezado);
                    headerCell.setPhrase(new Phrase(header,
                            new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tablaVentas.addCell(headerCell);
                });

        // Obtener las ventas (primera página con 100 registros)
        Page<VentaResponseDTO> ventasPage = ventaService.obtenerTodasLasVentas(PageRequest.of(0, 100));
        List<VentaResponseDTO> ventas = ventasPage.getContent();

        // Llenar la tabla con datos
        for (VentaResponseDTO venta : ventas) {
            // ID
            tablaVentas.addCell(new Phrase(venta.getId().toString()));

            // Fecha formateada
            String fechaStr = venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            tablaVentas.addCell(new Phrase(fechaStr));

            // Nombre del cliente
            tablaVentas.addCell(new Phrase(venta.getClienteNombre()));

            // Total formateado como moneda
            tablaVentas.addCell(new Phrase(String.format("$%,.2f", venta.getTotal())));

            // Lista de productos (concatenados)
            String productos = venta.getConceptos().stream()
                    .map(c -> c.getProductoNombre() + " (" + c.getCantidad() + ")")
                    .collect(Collectors.joining(", "));
            tablaVentas.addCell(new Phrase(productos));
        }

        document.add(tablaVentas);

        // Pie de página
        Paragraph piePagina = new Paragraph(
                "Total de ventas: " + ventas.size() + " | Total generado: $" +
                        ventas.stream().mapToDouble(VentaResponseDTO::getTotal).sum(),
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
        piePagina.setAlignment(Element.ALIGN_RIGHT);
        document.add(piePagina);

        document.close();

        // Guardar copia en disco C:/reportesSistema
        File carpeta = new File("C:/reportes");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        String nombreArchivo = "reporte_ventas_" + System.currentTimeMillis() + ".pdf";
        String filePath = "C:/reportes/" + nombreArchivo;
        Files.write(Paths.get(filePath), baos.toByteArray());

        // Configurar respuesta para descarga
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_ventas.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }
}
