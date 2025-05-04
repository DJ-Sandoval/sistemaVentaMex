package com.VentaMex.apiVentaMex.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "caja")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Caja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @PositiveOrZero(message = "El monto inicial debe ser positivo o cero")
    @Column(name = "monto_inicial", nullable = false)
    private Double montoInicial;

    @PositiveOrZero(message = "El monto de cierre debe ser positivo o cero")
    @Column(name = "monto_cierre")
    private Double montoCierre;

    @PositiveOrZero(message = "El total de ventas debe ser positivo o cero")
    @Column(name = "total_ventas")
    private Double totalVentas;

    @PositiveOrZero(message = "El total en efectivo debe ser positivo o cero")
    @Column(name = "total_efectivo")
    private Double totalEfectivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCaja estado;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;
}
