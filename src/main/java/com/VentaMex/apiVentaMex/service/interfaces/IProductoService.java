package com.VentaMex.apiVentaMex.service.interfaces;
import com.VentaMex.apiVentaMex.persistence.entities.Producto;
import com.VentaMex.apiVentaMex.presentation.dto.ProductoDTO;
import com.VentaMex.apiVentaMex.service.exception.ProductoNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface IProductoService {
    /**
     * Crea un nuevo producto
     * @param productoDTO DTO con los datos del producto a crear
     * @return ProductoDTO creado
     */
    ProductoDTO crearProducto(ProductoDTO productoDTO);

    /**
     * Obtiene todos los productos paginados
     * @param pageable Configuración de paginación
     * @return Página de ProductoDTO
     */
    Page<ProductoDTO> obtenerTodosLosProductos(Pageable pageable);

    /**
     * Obtiene un producto por su ID
     * @param id ID del producto a buscar
     * @return ProductoDTO encontrado
     * @throws ProductoNotFoundException si el producto no existe
     */
    @Cacheable(value = "productos", key = "#id")
    ProductoDTO obtenerProductoPorId(Long id);

    /**
     * Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param productoDTO DTO con los nuevos datos del producto
     * @return ProductoDTO actualizado
     * @throws ProductoNotFoundException si el producto no existe
     */
    @CachePut(value = "productos", key = "#result.id")
    ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO);

    /**
     * Elimina un producto por su ID
     * @param id ID del producto a eliminar
     * @throws ProductoNotFoundException si el producto no existe
     */
    @CacheEvict(value = "productos", key = "#id")
    void eliminarProducto(Long id);
}
