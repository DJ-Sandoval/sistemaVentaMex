package com.VentaMex.apiVentaMex.presentation.api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.VentaMex.apiVentaMex.presentation.dto.CategoriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Categorías", description = "Operaciones CRUD para categorías")
@RequestMapping(CategoryAPI.BASE_URL)
public interface CategoryAPI {
    String BASE_URL="/api/categorias";

    @Operation(summary = "Obtener todas las categorías paginadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida correctamente")
    })
    @GetMapping
    ResponseEntity<Page<CategoriaDTO>> obtenerTodasCategorias(Pageable pageable);

    @Operation(summary = "Obtener una categoría por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(@PathVariable Long id);

    @Operation(summary = "Crear una nueva categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente")
    })
    @PostMapping
    ResponseEntity<CategoriaDTO> crearCategoria(@RequestBody CategoriaDTO categoriaDTO);

    @Operation(summary = "Actualizar una categoría existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO);

    @Operation(summary = "Eliminar una categoría por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminarCategoria(@PathVariable Long id);
}
