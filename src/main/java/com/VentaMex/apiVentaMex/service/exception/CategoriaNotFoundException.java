package com.VentaMex.apiVentaMex.service.exception;

public class CategoriaNotFoundException extends RuntimeException {
    public CategoriaNotFoundException(Long id) {
        super("Categoria no encontrada con el id: " + id);
    }
}
