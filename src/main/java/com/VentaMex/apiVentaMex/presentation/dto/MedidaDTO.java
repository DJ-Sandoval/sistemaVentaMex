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
public class MedidaDTO {
    private Long id;
    private String nombre;
    private String unidad;
    private Estado estado;
    @JsonIgnore
    private List<ProductoDTO> productos;
}
