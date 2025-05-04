package com.VentaMex.apiVentaMex.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CajaRequestDTO {
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;

    @PositiveOrZero(message = "El monto inicial debe ser positivo o cero")
    @NotNull(message = "El monto inicial es obligatorio")
    private Double montoInicial;
}
