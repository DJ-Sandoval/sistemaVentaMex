package com.VentaMex.apiVentaMex.service.imp;

import com.VentaMex.apiVentaMex.persistence.entities.Medida;
import com.VentaMex.apiVentaMex.persistence.entities.Producto;
import com.VentaMex.apiVentaMex.persistence.repository.MedidaRepository;
import com.VentaMex.apiVentaMex.presentation.dto.ConceptoSimpleDTO;
import com.VentaMex.apiVentaMex.presentation.dto.MedidaDTO;
import com.VentaMex.apiVentaMex.presentation.dto.ProductoDTO;
import com.VentaMex.apiVentaMex.service.exception.MedidaNotFoundException;
import com.VentaMex.apiVentaMex.service.interfaces.IMedidaService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedidaServiceImp implements IMedidaService {
    private static final String MEDIDA_SERVICE = "medidaService";
    private final MedidaRepository medidaRepository;

    @Transactional
    @CircuitBreaker(name = MEDIDA_SERVICE, fallbackMethod = "fallbackCrearMedida")
    @Retry(name = MEDIDA_SERVICE)
    @TimeLimiter(name = MEDIDA_SERVICE)
    public MedidaDTO crearMedida(MedidaDTO medidaDTO) {
        Medida medida = mapToEntity(medidaDTO);
        Medida savedMedida = medidaRepository.save(medida);
        return mapToDTO(savedMedida);
    }

    @CircuitBreaker(name = MEDIDA_SERVICE, fallbackMethod = "fallbackObtenerMedida")
    @Retry(name = MEDIDA_SERVICE)
    @TimeLimiter(name = MEDIDA_SERVICE)
    public MedidaDTO obtenerMedidaPorId(Long id) {
        Medida medida = medidaRepository.findById(id)
                .orElseThrow(() -> new MedidaNotFoundException(id));
        return mapToDTO(medida);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = MEDIDA_SERVICE, fallbackMethod = "fallbackObtenerTodasMedidas")
    @Retry(name = MEDIDA_SERVICE)
    public Page<MedidaDTO> obtenerTodasMedidas(Pageable pageable) {
        return medidaRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Transactional
    @CircuitBreaker(name = MEDIDA_SERVICE, fallbackMethod = "fallbackActualizarMedida")
    @Retry(name = MEDIDA_SERVICE)
    @TimeLimiter(name = MEDIDA_SERVICE)
    public MedidaDTO actualizarMedida(Long id, MedidaDTO medidaDTO) {
        Medida medida = medidaRepository.findById(id)
                .orElseThrow(() -> new MedidaNotFoundException(id));

        medida.setNombre(medidaDTO.getNombre());
        medida.setUnidad(medidaDTO.getUnidad());
        medida.setEstado(medidaDTO.getEstado());

        if (medidaDTO.getProductos() != null) {
            medida.getProductos().clear();
            medida.getProductos().addAll(medidaDTO.getProductos().stream()
                    .map(this::mapProductoDTOToEntity)
                    .collect(Collectors.toList()));
        }

        Medida updatedMedida = medidaRepository.save(medida);
        return mapToDTO(updatedMedida);
    }

    @Transactional
    @CircuitBreaker(name = MEDIDA_SERVICE, fallbackMethod = "fallbackEliminarMedida")
    @Retry(name = MEDIDA_SERVICE)
    @TimeLimiter(name = MEDIDA_SERVICE)
    public void eliminarMedida(Long id) {
        if (!medidaRepository.existsById(id)) {
            throw new MedidaNotFoundException(id);
        }
        medidaRepository.deleteById(id);
    }

    public MedidaDTO mapToDTO(Medida medida) {
        MedidaDTO medidaDTO = MedidaDTO.builder()
                .id(medida.getId())
                .nombre(medida.getNombre())
                .unidad(medida.getUnidad())
                .estado(medida.getEstado())
                .build();

        List<ProductoDTO> productoDTOs = medida.getProductos().stream()
                .map(this::mapProductoToDTO)
                .collect(Collectors.toList());
        medidaDTO.setProductos(productoDTOs);

        return medidaDTO;
    }

    private Medida mapToEntity(MedidaDTO medidaDTO) {
        Medida medida = Medida.builder()
                .id(medidaDTO.getId())
                .nombre(medidaDTO.getNombre())
                .unidad(medidaDTO.getUnidad())
                .estado(medidaDTO.getEstado())
                .build();

        if (medidaDTO.getProductos() != null) {
            List<Producto> productos = medidaDTO.getProductos().stream()
                    .map(this::mapProductoDTOToEntity)
                    .collect(Collectors.toList());
            medida.setProductos(productos);
            productos.forEach(producto -> producto.setMedida(medida));
        }

        return medida;
    }

    private ProductoDTO mapProductoToDTO(Producto producto) {
        ProductoDTO productoDTO = ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .precioUnitario(producto.getPrecioUnitario())
                .costo(producto.getCosto())
                .categoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null)
                .medidaId(producto.getMedida() != null ? producto.getMedida().getId() : null)
                .build();

        if (producto.getConceptos() != null) {
            List<ConceptoSimpleDTO> conceptoDTOs = producto.getConceptos().stream()
                    .map(concepto -> ConceptoSimpleDTO.builder()
                            .id(concepto.getId())
                            .cantidad(concepto.getCantidad())
                            .precioUnitario(concepto.getPrecioUnitario())
                            .importe(concepto.getImporte())
                            .ventaId(concepto.getVenta() != null ? concepto.getVenta().getId() : null)
                            .build())
                    .collect(Collectors.toList());
            productoDTO.setConceptos(conceptoDTOs);
        }

        return productoDTO;
    }

    private Producto mapProductoDTOToEntity(ProductoDTO productoDTO) {
        Producto producto = Producto.builder()
                .id(productoDTO.getId())
                .nombre(productoDTO.getNombre())
                .precioUnitario(productoDTO.getPrecioUnitario())
                .costo(productoDTO.getCosto())
                .build();

        return producto;
    }

    // Fallback methods
    private MedidaDTO fallbackCrearMedida(MedidaDTO medidaDTO, Throwable t) {
        return MedidaDTO.builder()
                .id(-1L)
                .nombre("Error: No se pudo crear la medida")
                .build();
    }

    private MedidaDTO fallbackObtenerMedida(Long id, Throwable t) {
        return MedidaDTO.builder()
                .id(id)
                .nombre("Error: Medida no disponible")
                .build();
    }

    private Page<MedidaDTO> fallbackObtenerTodasMedidas(Pageable pageable, Throwable t) {
        return Page.empty(pageable);
    }

    private MedidaDTO fallbackActualizarMedida(Long id, MedidaDTO medidaDTO, Throwable t) {
        return MedidaDTO.builder()
                .id(id)
                .nombre("Error: No se pudo actualizar la medida")
                .build();
    }

    private void fallbackEliminarMedida(Long id, Throwable t) {
        throw new RuntimeException("No se pudo eliminar la medida con id: " + id, t);
    }
}