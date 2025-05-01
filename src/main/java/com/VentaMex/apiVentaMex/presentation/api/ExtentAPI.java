package com.VentaMex.apiVentaMex.presentation.api;
import com.VentaMex.apiVentaMex.presentation.dto.MedidaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Medidas", description = "Operaciones CRUD para medidas")
@RequestMapping(ExtentAPI.BASE_URL)
public interface ExtentAPI {
    String BASE_URL="/api/medidas";

    @Operation(summary = "Obtener todas las medidas paginadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de medidas obtenida correctamente")
    })
    @GetMapping
    ResponseEntity<Page<MedidaDTO>> obtenerTodasMedidas(Pageable pageable);

    @Operation(summary = "Obtener una medida por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medida encontrada"),
            @ApiResponse(responseCode = "404", description = "Medida no encontrada")
    })
    @GetMapping("/{id}")
    ResponseEntity<MedidaDTO> obtenerMedidaPorId(@PathVariable Long id);

    @Operation(summary = "Crear una nueva medida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medida creada exitosamente")
    })
    @PostMapping
    ResponseEntity<MedidaDTO> crearMedida(@RequestBody MedidaDTO medidaDTO);

    @Operation(summary = "Actualizar una medida existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medida actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Medida no encontrada")
    })
    @PutMapping("/{id}")
    ResponseEntity<MedidaDTO> actualizarMedida(@PathVariable Long id, @RequestBody MedidaDTO medidaDTO);

    @Operation(summary = "Eliminar una medida por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medida eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Medida no encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminarMedida(@PathVariable Long id);
}
