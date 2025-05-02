package com.VentaMex.apiVentaMex.service.imp;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import com.VentaMex.apiVentaMex.persistence.entities.Categoria;
import com.VentaMex.apiVentaMex.persistence.entities.Producto;
import com.VentaMex.apiVentaMex.persistence.repository.CategoriaRepository;
import com.VentaMex.apiVentaMex.presentation.dto.CategoriaDTO;
import com.VentaMex.apiVentaMex.presentation.dto.ConceptoSimpleDTO;
import com.VentaMex.apiVentaMex.presentation.dto.ProductoDTO;
import com.VentaMex.apiVentaMex.service.exception.CategoriaNotFoundException;
import com.VentaMex.apiVentaMex.service.interfaces.ICategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImp implements ICategoriaService {
    private static final String CATEGORIA_SERVICE = "categoriaService";
    private final CategoriaRepository categoriaRepository;

    @Transactional
    @CircuitBreaker(name = CATEGORIA_SERVICE, fallbackMethod = "fallbackCrearCategoria")
    @Retry(name = CATEGORIA_SERVICE)
    @TimeLimiter(name = CATEGORIA_SERVICE)
    @Override
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = mapToEntity(categoriaDTO);
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return mapToDTO(savedCategoria);
    }

    @CircuitBreaker(name = CATEGORIA_SERVICE, fallbackMethod = "fallbackObtenerCategoria")
    @Retry(name = CATEGORIA_SERVICE)
    @TimeLimiter(name = CATEGORIA_SERVICE)
    @Override
    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException(id));
        return mapToDTO(categoria);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = CATEGORIA_SERVICE, fallbackMethod = "fallbackObtenerTodasCategorias")
    @Retry(name = CATEGORIA_SERVICE)
    @Override
    public Page<CategoriaDTO> obtenerTodasCategorias(Pageable pageable) {
        return categoriaRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Transactional
    @CircuitBreaker(name = CATEGORIA_SERVICE, fallbackMethod = "fallbackActualizarCategoria")
    @Retry(name = CATEGORIA_SERVICE)
    @TimeLimiter(name = CATEGORIA_SERVICE)
    @Override
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException(id));

        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setEstado(categoriaDTO.getEstado());

        if (categoriaDTO.getProductos() != null) {
            categoria.getProductos().clear();
            categoria.getProductos().addAll(categoriaDTO.getProductos().stream()
                    .map(this::mapProductoDTOToEntity)
                    .collect(Collectors.toList()));
        }

        Categoria updatedCategoria = categoriaRepository.save(categoria);
        return mapToDTO(updatedCategoria);
    }

    @Transactional
    @CircuitBreaker(name = CATEGORIA_SERVICE, fallbackMethod = "fallbackEliminarCategoria")
    @Retry(name = CATEGORIA_SERVICE)
    @TimeLimiter(name = CATEGORIA_SERVICE)
    @Override
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new CategoriaNotFoundException(id);
        }
        categoriaRepository.deleteById(id);
    }

    // Convert DTO to entity
    public CategoriaDTO mapToDTO(Categoria categoria) {
        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .estado(categoria.getEstado())
                .build();

        // Map products without categoria to avoid recursion
        List<ProductoDTO> productoDTOs = categoria.getProductos().stream()
                .map(this::mapProductoToDTO)
                .collect(Collectors.toList());
        categoriaDTO.setProductos(productoDTOs);

        return categoriaDTO;
    }

    private Categoria mapToEntity(CategoriaDTO categoriaDTO) {
        Categoria categoria = Categoria.builder()
                .id(categoriaDTO.getId())
                .nombre(categoriaDTO.getNombre())
                .estado(categoriaDTO.getEstado())
                .build();

        if (categoriaDTO.getProductos() != null) {
            List<Producto> productos = categoriaDTO.getProductos().stream()
                    .map(this::mapProductoDTOToEntity)
                    .collect(Collectors.toList());
            categoria.setProductos(productos);
            // Set the categoria reference in each product to maintain the relationship
            productos.forEach(producto -> producto.setCategoria(categoria));
        }

        return categoria;
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

        // Map conceptos
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

        // Do not set categoria or medida in ProductoDTO to rely on @JsonBackReference
        return productoDTO;
    }

    private Producto mapProductoDTOToEntity(ProductoDTO productoDTO) {
        Producto producto = Producto.builder()
                .id(productoDTO.getId())
                .nombre(productoDTO.getNombre())
                .precioUnitario(productoDTO.getPrecioUnitario())
                .costo(productoDTO.getCosto())
                .build();

        // Categoria and Medida should be set by the caller (e.g., in mapToEntity)
        return producto;
    }

    // Fallback methods
    private CategoriaDTO fallbackCrearCategoria(CategoriaDTO categoriaDTO, Throwable t) {
        return CategoriaDTO.builder()
                .id(-1L)
                .nombre("Error: No se pudo crear la categoría")
                .build();
    }

    private CategoriaDTO fallbackObtenerCategoria(Long id, Throwable t) {
        return CategoriaDTO.builder()
                .id(id)
                .nombre("Error: Categoría no disponible")
                .build();
    }

    private Page<CategoriaDTO> fallbackObtenerTodasCategorias(Pageable pageable, Throwable t) {
        return Page.empty(pageable);
    }

    private CategoriaDTO fallbackActualizarCategoria(Long id, CategoriaDTO categoriaDTO, Throwable t) {
        return CategoriaDTO.builder()
                .id(id)
                .nombre("Error: No se pudo actualizar la categoría")
                .build();
    }

    private void fallbackEliminarCategoria(Long id, Throwable t) {
        throw new RuntimeException("No se pudo eliminar la categoría con id: " + id, t);
    }
}
