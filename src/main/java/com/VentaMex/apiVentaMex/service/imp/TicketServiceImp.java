package com.VentaMex.apiVentaMex.service.imp;
import com.VentaMex.apiVentaMex.persistence.entities.Concepto;
import com.VentaMex.apiVentaMex.persistence.entities.NegocioEntity;
import com.VentaMex.apiVentaMex.persistence.entities.Venta;
import com.VentaMex.apiVentaMex.persistence.repository.NegocioRepository;
import com.VentaMex.apiVentaMex.persistence.repository.VentaRepository;
import com.VentaMex.apiVentaMex.service.exception.VentaException;
import com.VentaMex.apiVentaMex.service.interfaces.TicketService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImp implements TicketService {

    private final VentaRepository ventaRepository;
    private final NegocioRepository negocioRepository;

    @Override
    public String generarTicketTexto(Long ventaId) {
        Venta venta = ventaRepository.findWithConceptosById(ventaId)
                .orElseThrow(() -> new VentaException("Venta no encontrada con ID: " + ventaId));

        // Fetch the first negocio (assuming one business for simplicity)
        NegocioEntity negocio = negocioRepository.findAll().stream().findFirst()
                .orElse(NegocioEntity.builder()
                        .nombreNegocio("Negocio no configurado")
                        .nombrePropietario("")
                        .rfc("")
                        .domicilio("")
                        .telefono("")
                        .imagen("")
                        .build());

        StringBuilder sb = new StringBuilder();
        sb.append("*************************\n");
        sb.append(negocio.getNombreNegocio() + "\n");
        sb.append("*************************\n");
        // Add negocio details
        if (!negocio.getNombrePropietario().isEmpty()) {
            sb.append("Propietario: " + negocio.getNombrePropietario() + "\n");
        }
        if (!negocio.getRfc().isEmpty()) {
            sb.append("RFC: " + negocio.getRfc() + "\n");
        }
        if (!negocio.getDomicilio().isEmpty()) {
            sb.append("Domicilio: " + negocio.getDomicilio() + "\n");
        }
        if (!negocio.getTelefono().isEmpty()) {
            sb.append("Telefono: " + negocio.getTelefono() + "\n");
        }
        if (!negocio.getImagen().isEmpty()) {
            sb.append("[Imagen en blanco y negro]\n"); // Placeholder for image
        }
        sb.append("-----------------------------\n");

        // Original ticket content
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




