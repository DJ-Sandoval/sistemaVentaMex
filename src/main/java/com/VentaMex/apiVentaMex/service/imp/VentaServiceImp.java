package com.VentaMex.apiVentaMex.service.imp;
import com.VentaMex.apiVentaMex.persistence.entities.Cliente;
import com.VentaMex.apiVentaMex.persistence.entities.Concepto;
import com.VentaMex.apiVentaMex.persistence.entities.Producto;
import com.VentaMex.apiVentaMex.persistence.entities.Venta;
import com.VentaMex.apiVentaMex.persistence.repository.ClienteRepository;
import com.VentaMex.apiVentaMex.persistence.repository.ConceptoRepository;
import com.VentaMex.apiVentaMex.persistence.repository.ProductoRepository;
import com.VentaMex.apiVentaMex.persistence.repository.VentaRepository;
import com.VentaMex.apiVentaMex.presentation.dto.ConceptoRequestDTO;
import com.VentaMex.apiVentaMex.presentation.dto.ConceptoResponseDTO;
import com.VentaMex.apiVentaMex.presentation.dto.VentaRequestDTO;
import com.VentaMex.apiVentaMex.presentation.dto.VentaResponseDTO;
import com.VentaMex.apiVentaMex.service.exception.VentaException;
import com.VentaMex.apiVentaMex.service.exception.VentaNotFoundException;
import com.VentaMex.apiVentaMex.service.interfaces.TicketService;
import com.VentaMex.apiVentaMex.service.interfaces.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class VentaServiceImp implements VentaService {
    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final ConceptoRepository conceptoRepository;
    private final TicketService ticketService;
    private final ImpresoraService impresoraService;


    @Override
    @Transactional(readOnly = true)
    public Page<VentaResponseDTO> obtenerTodasLasVentas(Pageable pageable) {
        return ventaRepository.findAll(pageable)
                .map(this::convertirAVentaResponseDTO);
    }


    @Override
    public VentaResponseDTO registrarVenta(VentaRequestDTO ventaRequest) {
        Cliente cliente = clienteRepository.findById(ventaRequest.getClienteId())
                .orElseThrow(() -> new VentaException("Cliente no encontrado con ID: " + ventaRequest.getClienteId()));

        Venta venta = Venta.builder()
                .fecha(LocalDateTime.now())
                .cliente(cliente)
                .total(0.0)
                .build();

        Venta ventaGuardada = ventaRepository.save(venta);

        List<Concepto> conceptos = new ArrayList<>();
        for (ConceptoRequestDTO conceptoRequest : ventaRequest.getConceptos()) {
            Producto producto = productoRepository.findById(conceptoRequest.getProductoId())
                    .orElseThrow(() -> new VentaException("Producto no encontrado con ID: " + conceptoRequest.getProductoId()));

            Double importe = producto.getPrecioUnitario() * conceptoRequest.getCantidad();

            Concepto concepto = Concepto.builder()
                    .venta(ventaGuardada)
                    .producto(producto)
                    .cantidad(conceptoRequest.getCantidad())
                    .precioUnitario(producto.getPrecioUnitario())
                    .importe(importe)
                    .build();

            conceptos.add(concepto);
            ventaGuardada.setTotal(ventaGuardada.getTotal() + importe);
        }

        conceptoRepository.saveAll(conceptos);
        ventaRepository.save(ventaGuardada);

        ventaGuardada.setConceptos(conceptos);

        // 📌 Generar ticket PDF
        try {
            String ticketTexto = ticketService.generarTicketTexto(ventaGuardada.getId());
            impresoraService.imprimirTicket(ticketTexto);
        } catch (Exception e) {
            throw new VentaException("Error al generar/imprimir el ticket: " + e.getMessage());
        }

        return convertirAVentaResponseDTO(ventaGuardada);
    }


    @Override
    @Transactional(readOnly = true)
    public VentaResponseDTO obtenerVentaPorId(Long id) {
        Venta venta = ventaRepository.findWithConceptosById(id)
                .orElseThrow(() -> new VentaException("Venta no encontrada con ID: " + id));
        return convertirAVentaResponseDTO(venta);
    }



    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorCliente(Long clienteId) {
        List<Venta> ventas = ventaRepository.findByClienteId(clienteId);
        return ventas.stream()
                .map(this::convertirAVentaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNotFoundException(id));
        ventaRepository.delete(venta);
    }

    @Override
    public Page<VentaResponseDTO> buscarVentas(String search, Pageable pageable) {
        if (search == null || search.isEmpty()) {
            return ventaRepository.findAll(pageable)
                    .map(this::convertirAVentaResponseDTO);
        } else {
            return ventaRepository.findByClienteNombreContainingIgnoreCase(search, pageable)
                    .map(this::convertirAVentaResponseDTO);
        }
    }

    private VentaResponseDTO convertirAVentaResponseDTO(Venta venta) {
        List<ConceptoResponseDTO> conceptosDTO = venta.getConceptos().stream()
                .map(concepto -> ConceptoResponseDTO.builder()
                        .productoId(concepto.getProducto().getId())
                        .productoNombre(concepto.getProducto().getNombre())
                        .cantidad(concepto.getCantidad())
                        .precioUnitario(concepto.getPrecioUnitario())
                        .importe(concepto.getImporte())
                        .build())
                .collect(Collectors.toList());

        return VentaResponseDTO.builder()
                .id(venta.getId())
                .fecha(venta.getFecha())
                .clienteNombre(venta.getCliente().getNombre())
                .total(venta.getTotal())
                .conceptos(conceptosDTO)
                .rutaTicket(venta.getRutaTicket())
                .build();
    }

}
