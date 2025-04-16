package com.VentaMex.apiVentaMex.service.exception;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(Long id) {
        super("Cliente no encontrado con ID: " + id);
    }
}
