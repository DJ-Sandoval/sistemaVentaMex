package com.VentaMex.apiVentaMex.service.imp;

import com.VentaMex.apiVentaMex.persistence.entities.Cliente;
import com.VentaMex.apiVentaMex.persistence.repository.ClienteRepository;
import com.VentaMex.apiVentaMex.presentation.dto.ClienteDTO;
import com.VentaMex.apiVentaMex.presentation.dto.VentaDTO;
import com.VentaMex.apiVentaMex.service.exception.ClienteNotFoundException;
import com.VentaMex.apiVentaMex.service.interfaces.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@CacheConfig(cacheNames = "clientes")
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }


    @Override
    public Page<ClienteDTO> obtenerTodosClientes(Pageable pageable) {
        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        return clientes.map(this::mapToDTO);
    }

    @Override
    public ClienteDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));

        // Convertir a DTO
        return mapToDTO(cliente);
    }

    @Override
    @Transactional
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(clienteActualizado.getNombre());
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }

    @Override
    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
        clienteRepository.delete(cliente);
    }

    public ClienteDTO mapToDTO(Cliente cliente) {
        List<VentaDTO> ventasDTO = cliente.getVentas().stream()
                .map(venta -> new VentaDTO(
                        venta.getId(),
                        venta.getFecha(),
                        venta.getTotal()
                ))
                .collect(Collectors.toList());
        return new ClienteDTO(cliente.getId(), cliente.getNombre(), ventasDTO);
    }
}