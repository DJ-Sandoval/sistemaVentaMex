package com.VentaMex.apiVentaMex.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medida")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String unidad;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @OneToMany(mappedBy = "medida", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();
}
