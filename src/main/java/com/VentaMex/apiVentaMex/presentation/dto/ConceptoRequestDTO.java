package com.VentaMex.apiVentaMex.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoRequestDTO {
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;
}
