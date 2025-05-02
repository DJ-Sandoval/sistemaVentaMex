package com.VentaMex.apiVentaMex.service.exception;

public class MedidaNotFoundException extends RuntimeException {
    public MedidaNotFoundException(Long id) {
        super("Medida no encontrada con el id: " + id);
    }
}
