package com.example.Ventas.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="detalle_boleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalleBoleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull(message = "La boleta es obligatoria")
    @ManyToOne
    @JoinColumn(name = "id_boleta", nullable = false)
    @JsonIgnoreProperties("detalles")
    private Boleta boleta;

    @NotNull(message = "El id del libro es obligatorio")
    @Positive(message = "El id del libro debe ser mayor a 0")
    @Column(name = "id_libro", nullable = false)
    private Integer idLibro;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

}
