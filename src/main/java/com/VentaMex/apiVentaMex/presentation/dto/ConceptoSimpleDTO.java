package com.VentaMex.apiVentaMex.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoSimpleDTO {
    private Long id;
    private Integer cantidad;
    private Double precioUnitario;
    private Double importe;
    private Long ventaId;
}
