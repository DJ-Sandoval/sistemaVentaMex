package com.VentaMex.apiVentaMex.presentation.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialVentaDTO {
    private Long idCliente;
    private String nombreCliente;
    private Long cantidadVentas;
    private Double totalCompras;
    private LocalDate primeraCompra;
    private LocalDate ultimaCompra;
}
