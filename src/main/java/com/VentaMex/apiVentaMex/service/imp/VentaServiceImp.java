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

    @Override
    public Page<Venta> obtenerTodasLasVentas(Pageable pageable) {
        return ventaRepository.findAll(pageable);
    }

    @Override
    public VentaResponseDTO registrarVenta(VentaRequestDTO ventaRequest) {
        // Validar cliente
        Cliente cliente = clienteRepository.findById(ventaRequest.getClienteId())
                .orElseThrow(() -> new VentaException("Cliente no encontrado con ID: " + ventaRequest.getClienteId()));

        // Crear venta
        Venta venta = Venta.builder()
                .fecha(LocalDateTime.now())
                .cliente(cliente)
                .total(0.0)
                .build();

        // Guardar la venta primero para obtener el ID
        Venta ventaGuardada = ventaRepository.save(venta);

        // Procesar conceptos
        List<Concepto> conceptos = new ArrayList<>();
        for (ConceptoRequestDTO conceptoRequest : ventaRequest.getConceptos()) {
            Producto producto = productoRepository.findById(conceptoRequest.getProductoId())
                    .orElseThrow(() -> new VentaException("Producto no encontrado con ID: " + conceptoRequest.getProductoId()));

            Double importe = producto.getPrecioUnitario() * conceptoRequest.getCantidad();

            Concepto concepto = Concepto.builder()
                    .venta(ventaGuardada)  // Usar la venta guardada con ID
                    .producto(producto)
                    .cantidad(conceptoRequest.getCantidad())
                    .precioUnitario(producto.getPrecioUnitario())
                    .importe(importe)
                    .build();

            conceptos.add(concepto);
            ventaGuardada.setTotal(ventaGuardada.getTotal() + importe);
        }

        // Guardar conceptos y actualizar venta
        conceptoRepository.saveAll(conceptos);
        ventaRepository.save(ventaGuardada);  // Actualizar el total

        // Forzar la carga de los conceptos antes de convertir a DTO
        ventaGuardada.setConceptos(conceptos);

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
                .build();
    }
}
