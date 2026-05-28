package com.example.Ventas.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="boleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();

    @NotNull(message = "El valor total es obligatorio")
    @PositiveOrZero(message = "El valor total no puede ser negativo")
    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @NotNull(message = "El id del cliente es obligatorio")
    @Positive(message = "El id del cliente debe ser mayor a 0")
    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<DetalleBoleta> detalles;


}
