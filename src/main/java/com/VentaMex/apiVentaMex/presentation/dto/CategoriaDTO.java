package com.VentaMex.apiVentaMex.presentation.dto;

import com.VentaMex.apiVentaMex.persistence.entities.Estado;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
    @JsonIgnore
    private List<ProductoDTO> productos;
}
