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
    private Long categoriaId;
    private Long medidaId;
    private CategoriaDTO categoria;
    private MedidaDTO medida;
    private List<ConceptoSimpleDTO> conceptos;
}
