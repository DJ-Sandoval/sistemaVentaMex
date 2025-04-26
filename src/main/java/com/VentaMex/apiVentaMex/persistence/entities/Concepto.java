package com.VentaMex.apiVentaMex.persistence.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "concepto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Concepto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", nullable = false)
    @JsonBackReference
    private Venta venta;

    @Positive(message = "La cantidad debe ser positiva")
    @Column(nullable = false)
    private Integer cantidad;

    @Positive(message = "El precio unitario debe ser positivo")
    @Column(nullable = false)
    private Double precioUnitario;

    @PositiveOrZero(message = "El importe debe ser positivo o cero")
    @Column(nullable = false)
    private Double importe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;
}
