package com.VentaMex.apiVentaMex.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "producto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Positive(message = "El precio unitario debe ser positivo")
    @Column(nullable = false)
    private Double precioUnitario;

    @PositiveOrZero(message = "El costo debe ser positivo o cero")
    @Column(nullable = false)
    private Double costo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private  Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medida_id")
    private Medida medida;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Concepto> conceptos = new ArrayList<>();
}

