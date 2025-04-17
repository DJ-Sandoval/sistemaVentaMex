package com.VentaMex.apiVentaMex.presentation.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private Long id;
    private String nombre;
    private List<VentaDTO> ventas;
}
