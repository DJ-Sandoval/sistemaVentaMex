package com.VentaMex.apiVentaMex.service.imp;

import com.VentaMex.apiVentaMex.persistence.entities.Producto;
import com.VentaMex.apiVentaMex.persistence.repository.ProductoRepository;
import com.VentaMex.apiVentaMex.presentation.dto.ConceptoProductoDTO;
import com.VentaMex.apiVentaMex.presentation.dto.ConceptoSimpleDTO;
import com.VentaMex.apiVentaMex.presentation.dto.ProductoDTO;
import com.VentaMex.apiVentaMex.presentation.dto.ProductoResponseDTO;
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
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Producto producto = modelMapper.map(productoDTO, Producto.class);
        Producto productoGuardado = productoRepository.save(producto);
        return convertirADTO(productoGuardado);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> obtenerTodosLosProductos(Pageable pageable) {
        return productoRepository.findAll(pageable)
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
    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoDTO.getNombre());
                    producto.setCosto(productoDTO.getCosto());
                    producto.setPrecioUnitario(productoDTO.getPrecioUnitario());
                    Producto productoActualizado = productoRepository.save(producto);
                    return convertirADTO(productoActualizado);
                })
                .orElseThrow(() -> new ProductoNotFoundException(id));
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
        ProductoDTO dto = modelMapper.map(producto, ProductoDTO.class);

        if (producto.getConceptos() != null) {
            dto.setConceptos(producto.getConceptos().stream()
                    .map(concepto -> ConceptoSimpleDTO.builder()
                            .id(concepto.getId())
                            .cantidad(concepto.getCantidad())
                            .precioUnitario(concepto.getPrecioUnitario())
                            .importe(concepto.getImporte())
                            .ventaId(concepto.getVenta().getId())
                            .build())
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public ProductoDTO fallbackObtenerProducto(Long id, Throwable ex) {
        log.error("Error al obtener producto con ID {}: {}", id, ex.toString());
        return new ProductoDTO(); // o una respuesta default
    }

}
