package com.VentaMex.apiVentaMex.service.exception;

public class MedidaException extends RuntimeException {
    public MedidaException(Long id) {
        super("Medida no encontrada con el id: " + id);
    }
}
