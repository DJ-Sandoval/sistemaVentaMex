package com.VentaMex.apiVentaMex.presentation.api;
import com.VentaMex.apiVentaMex.persistence.entities.Cliente;
import com.VentaMex.apiVentaMex.presentation.dto.ClienteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequestMapping(ClientAPI.BASE_URL)
@Tag(name = "API de Clientes", description = "Operaciones CRUD sobre Clientes")
public interface ClientAPI {
    String BASE_URL = "/api/clientes";
    @Operation(summary = "Crear un nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content)
    })
    @PostMapping
    ResponseEntity<Cliente> crearCliente(
            @Valid @RequestBody(description = "Datos del cliente a crear", required = true)
            Cliente cliente);

    @Operation(summary = "Obtener todos los clientes paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class)))
    })
    @GetMapping
    ResponseEntity<Page<ClienteDTO>> obtenerTodosClientes(
            @Parameter(description = "Página y tamaño de resultados", required = true) Pageable pageable);

    @Operation(summary = "Obtener un cliente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<ClienteDTO> obtenerClientePorId(
            @Parameter(description = "ID del cliente", required = true) @PathVariable Long id);

    @Operation(summary = "Actualizar un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<Cliente> actualizarCliente(
            @Parameter(description = "ID del cliente a actualizar", required = true) @PathVariable Long id,
            @Valid @RequestBody(description = "Datos actualizados del cliente", required = true) Cliente cliente);

    @Operation(summary = "Eliminar un cliente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminarCliente(
            @Parameter(description = "ID del cliente a eliminar", required = true) @PathVariable Long id);
}
