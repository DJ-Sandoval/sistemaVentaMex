package com.VentaMex.apiVentaMex.presentation.dto;

import com.VentaMex.apiVentaMex.persistence.entities.Estado;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO {
    private Long id;
    private String nombre;
    private Estado estado;

    // Mantener @JsonManagedReference para la relación principal
    @JsonManagedReference(value = "categoria-productos")
    @Builder.Default
    private List<ProductoDTO> productos = new ArrayList<>();
}
