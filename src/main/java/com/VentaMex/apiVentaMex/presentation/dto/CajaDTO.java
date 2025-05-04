package com.VentaMex.apiVentaMex.presentation.dto;

import com.VentaMex.apiVentaMex.persistence.entities.EstadoCaja;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaDTO {
    private Long id;
    private Long idUsuario;
    private Double montoInicial;
    private Double montoCierre;
    private Double totalVentas;
    private Double totalEfectivo;
    private EstadoCaja estado;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
}
