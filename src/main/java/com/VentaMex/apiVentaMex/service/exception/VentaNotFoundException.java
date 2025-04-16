package com.VentaMex.apiVentaMex.service.exception;

public class VentaNotFoundException extends RuntimeException {
    public VentaNotFoundException(Long id) {
        super("venta no encontrada con el ID: " + id);
    }
}
