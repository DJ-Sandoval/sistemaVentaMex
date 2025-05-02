package com.VentaMex.apiVentaMex.service.imp;

import com.VentaMex.apiVentaMex.persistence.entities.Categoria;
import com.VentaMex.apiVentaMex.persistence.entities.Medida;
import com.VentaMex.apiVentaMex.persistence.entities.Producto;
import com.VentaMex.apiVentaMex.persistence.repository.CategoriaRepository;
import com.VentaMex.apiVentaMex.persistence.repository.MedidaRepository;
import com.VentaMex.apiVentaMex.persistence.repository.ProductoRepository;
import com.VentaMex.apiVentaMex.presentation.dto.*;
import com.VentaMex.apiVentaMex.service.exception.CategoriaNotFoundException;
import com.VentaMex.apiVentaMex.service.exception.MedidaNotFoundException;
import com.VentaMex.apiVentaMex.service.exception.ProductoNotFoundException;
import com.VentaMex.apiVentaMex.service.interfaces.IProductoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "productos")
public class ProductoServiceImp implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MedidaRepository medidaRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Producto producto = mapToEntity(productoDTO);
        Producto productoGuardado = productoRepository.save(producto);
        return convertirADTO(productoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> obtenerTodosLosProductos(Pageable pageable) {
        return productoRepository.findAllWithCategoriesAndMeasures(pageable)
                .map(this::convertirADTO);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "productoService", fallbackMethod = "fallbackObtenerProducto")
    @Retry(name = "productoService", fallbackMethod = "fallbackObtenerProducto")
    @Cacheable(value = "productos", key = "#id")
    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
        return convertirADTO(producto);
    }

    @Override
    @Transactional
    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));

        producto.setNombre(productoDTO.getNombre());
        producto.setCosto(productoDTO.getCosto());
        producto.setPrecioUnitario(productoDTO.getPrecioUnitario());

        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new CategoriaNotFoundException(productoDTO.getCategoriaId()));
            producto.setCategoria(categoria);
        }

        if (productoDTO.getMedidaId() != null) {
            Medida medida = medidaRepository.findById(productoDTO.getMedidaId())
                    .orElseThrow(() -> new MedidaNotFoundException(productoDTO.getMedidaId()));
            producto.setMedida(medida);
        }

        Producto productoActualizado = productoRepository.save(producto);
        return convertirADTO(productoActualizado);
    }

    @Override
    @Transactional
    @CacheEvict(value = "productos", key = "#id")
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
        productoRepository.delete(producto);
    }

    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO productoDTO = ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .precioUnitario(producto.getPrecioUnitario())
                .costo(producto.getCosto())
                .categoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null)
                .medidaId(producto.getMedida() != null ? producto.getMedida().getId() : null)
                .build();

        // Set CategoriaDTO
        if (producto.getCategoria() != null) {
            CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                    .id(producto.getCategoria().getId())
                    .nombre(producto.getCategoria().getNombre())
                    .estado(producto.getCategoria().getEstado())
                    .build();
            productoDTO.setCategoria(categoriaDTO);
        }

        // Set MedidaDTO
        if (producto.getMedida() != null) {
            MedidaDTO medidaDTO = MedidaDTO.builder()
                    .id(producto.getMedida().getId())
                    .nombre(producto.getMedida().getNombre())
                    .unidad(producto.getMedida().getUnidad())
                    .estado(producto.getMedida().getEstado())
                    .build();
            productoDTO.setMedida(medidaDTO);
        }

        // Set Conceptos
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

    private Producto mapToEntity(ProductoDTO productoDTO) {
        Producto producto = Producto.builder()
                .id(productoDTO.getId())
                .nombre(productoDTO.getNombre())
                .precioUnitario(productoDTO.getPrecioUnitario())
                .costo(productoDTO.getCosto())
                .build();

        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new CategoriaNotFoundException(productoDTO.getCategoriaId()));
            producto.setCategoria(categoria);
        }

        if (productoDTO.getMedidaId() != null) {
            Medida medida = medidaRepository.findById(productoDTO.getMedidaId())
                    .orElseThrow(() -> new MedidaNotFoundException(productoDTO.getMedidaId()));
            producto.setMedida(medida);
        }

        return producto;
    }

    public ProductoDTO fallbackObtenerProducto(Long id, Throwable ex) {
        log.error("Error al obtener producto con ID {}: {}", id, ex.toString());
        return ProductoDTO.builder()
                .id(id)
                .nombre("Error: Producto no disponible")
                .build();
    }
}