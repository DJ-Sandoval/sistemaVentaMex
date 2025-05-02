package com.VentaMex.apiVentaMex.presentation.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    // Usar @JsonBackReference para la relación inversa
    @JsonBackReference(value = "categoria-productos")
    private CategoriaDTO categoria;

    @JsonBackReference(value = "medida-productos")
    private MedidaDTO medida;

    private List<ConceptoSimpleDTO> conceptos;
}
