package com.VentaMex.apiVentaMex.presentation.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private Double precioUnitario;
    private Double costo;
    private List<ConceptoSimpleDTO> conceptos;
}
