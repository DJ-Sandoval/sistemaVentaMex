package com.VentaMex.apiVentaMex.service.interfaces;
import com.VentaMex.apiVentaMex.persistence.entities.Cliente;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClienteService {
    Cliente crearCliente(Cliente cliente);
    Page<Cliente> obtenerTodosClientes(Pageable pageable);
    @Cacheable(value = "clientes", key = "#id")
    Cliente obtenerClientePorId(Long id);
    @CachePut(value = "clientes", key = "#result.id")
    Cliente actualizarCliente(Long id, Cliente cliente);
    @CacheEvict(value = "clientes", key = "#id")
    void eliminarCliente(Long id);
}
