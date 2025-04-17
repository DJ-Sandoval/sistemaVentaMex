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

        document.close();

        return rutaRelativa;
    }
}
