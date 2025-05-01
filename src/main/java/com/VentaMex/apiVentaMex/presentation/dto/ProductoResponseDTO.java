package com.VentaMex.apiVentaMex.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private Double precioUnitario;
    private Double costo;
    private Long categoriaId;
    private Long medidaId;
    private List<ConceptoProductoDTO> conceptos;
}
