package com.VentaMex.apiVentaMex.service.exception;

public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(Long id) {
        super("producto no encontrado con el ID: " + id);
    }
}
