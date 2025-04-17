package com.VentaMex.apiVentaMex.presentation.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponseDTO {
    private Long id;
    private LocalDateTime fecha;
    private String clienteNombre;
    private Double total;
    private List<ConceptoResponseDTO> conceptos;
    private String rutaTicket;
}

