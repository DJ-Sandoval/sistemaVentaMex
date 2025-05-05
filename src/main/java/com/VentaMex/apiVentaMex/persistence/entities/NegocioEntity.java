package com.VentaMex.apiVentaMex.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "negocio")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NegocioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String nombreNegocio;

    @Column(nullable = false)
    private String nombrePropietario;

    @Column
    private String rfc;

    @Column
    private String domicilio;

    @Column
    private String telefono;

    @Column(length = 10485760) // Large enough for Base64 image
    private String imagen; // Base64 encoded black-and-white image
}
