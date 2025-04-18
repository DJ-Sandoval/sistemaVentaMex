package com.VentaMex.apiVentaMex.service.imp;
import com.VentaMex.apiVentaMex.persistence.entities.Concepto;
import com.VentaMex.apiVentaMex.persistence.entities.Venta;
import com.VentaMex.apiVentaMex.persistence.repository.VentaRepository;
import com.VentaMex.apiVentaMex.presentation.dto.ConceptoResponseDTO;
import com.VentaMex.apiVentaMex.presentation.dto.VentaResponseDTO;
import com.VentaMex.apiVentaMex.service.exception.VentaException;
import com.VentaMex.apiVentaMex.service.interfaces.TicketService;
import com.VentaMex.apiVentaMex.service.interfaces.VentaService;

import com.itextpdf.text.*;

import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.*;
import java.awt.print.PrinterException;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class TicketServiceImp implements TicketService {

    private final VentaRepository ventaRepository;

    @Override
    public String generarTicketPdf(Long ventaId) throws Exception {
        Venta venta = ventaRepository.findWithConceptosById(ventaId)
                .orElseThrow(() -> new VentaException("Venta no encontrada con ID: " + ventaId));

        String nombreArchivo = "ticket_venta_" + ventaId + ".pdf";
        String rutaRelativa = "src/main/resources/tickets/" + nombreArchivo;

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(rutaRelativa));
        document.open();

        document.add(new Paragraph("Ticket de Venta #" + venta.getId()));
        document.add(new Paragraph("Cliente: " + venta.getCliente().getNombre()));
        document.add(new Paragraph("Fecha: " + venta.getFecha().toString()));
        document.add(new Paragraph("Total: $" + venta.getTotal()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Conceptos:"));
        for (Concepto concepto : venta.getConceptos()) {
            document.add(new Paragraph("- " + concepto.getProducto().getNombre() +
                    " x" + concepto.getCantidad() +
                    " $" + concepto.getImporte()));
        }
        document.add(new Paragraph("Gracias por su compra"));
        document.add(new Paragraph("Este no es un comprobante fiscal"));
        document.add(new Paragraph("VentaMex-POS v.25"));

        document.close();

        return rutaRelativa;
    }

    @Override
    public String generarTicketTexto(Long ventaId) {
        Venta venta = ventaRepository.findWithConceptosById(ventaId)
                .orElseThrow(() -> new VentaException("Venta no encontrada con ID: " + ventaId));

        StringBuilder sb = new StringBuilder();
        sb.append("*************************\n");
        sb.append("  VentaMex POS v.25\n");
        sb.append("*************************\n");
        sb.append("Venta #" + venta.getId() + "\n");
        sb.append("Cliente: " + venta.getCliente().getNombre() + "\n");
        sb.append("Fecha: " + venta.getFecha().toString() + "\n");
        sb.append("-----------------------------\n");

        for (Concepto c : venta.getConceptos()) {
            sb.append(c.getProducto().getNombre() + " x" + c.getCantidad() + " $" + c.getImporte() + "\n");
        }
        int anchoTicket = 30;
        // Total
        sb.append("------------------------------\n");
        sb.append(String.format("%-10s $%.2f\n", "TOTAL:", venta.getTotal()));
        sb.append("------------------------------\n");

        // Mensaje de agradecimiento centrado
        String mensajeGracias = "Gracias por su compra!";
        int espaciosGracias = (anchoTicket - mensajeGracias.length()) / 2;
        sb.append(" ".repeat(Math.max(0, espaciosGracias)));
        sb.append(mensajeGracias + "\n");

        // Línea de no comprobante fiscal centrada
        String mensajeAviso = "Este no es un comprobante fiscal";
        int espaciosAviso = (anchoTicket - mensajeAviso.length()) / 2;
        sb.append(" ".repeat(Math.max(0, espaciosAviso)));
        sb.append(mensajeAviso + "\n");

        // Versión del sistema centrada
        String mensajeVersion = "VentaMex-POS v.25";
        int espaciosVersion = (anchoTicket - mensajeVersion.length()) / 2;
        sb.append(" ".repeat(Math.max(0, espaciosVersion)));
        sb.append(mensajeVersion + "\n");

        // Espacio para corte
        sb.append("\n\n\n");

        return sb.toString();
    }



}
