package com.VentaMex.apiVentaMex.presentation.controller;


import com.VentaMex.apiVentaMex.persistence.entities.Cliente;
import com.VentaMex.apiVentaMex.presentation.api.ClientAPI;
import com.VentaMex.apiVentaMex.presentation.dto.ClienteDTO;
import com.VentaMex.apiVentaMex.service.exception.ClienteNotFoundException;
import com.VentaMex.apiVentaMex.service.interfaces.IClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ClienteController implements ClientAPI {
    private final IClienteService clienteService;

    @Override
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crearCliente(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Page<ClienteDTO>> obtenerTodosClientes(Pageable pageable) {
        Page<ClienteDTO> clientes = clienteService.obtenerTodosClientes(pageable);
        return ResponseEntity.ok(clientes);
    }

    @Override
    public ResponseEntity<ClienteDTO> obtenerClientePorId(Long id) {
        ClienteDTO cliente = clienteService.obtenerClientePorId(id);
        return ResponseEntity.ok(cliente);
    }

    @Override
    public ResponseEntity<Cliente> actualizarCliente(Long id, @Valid @RequestBody Cliente cliente) {
        Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    @Override
    public ResponseEntity<Void> eliminarCliente(Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<String> handleClienteNotFound(ClienteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
