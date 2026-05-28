package com.example.Ventas.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_boleta", nullable = false)
    @JsonIgnoreProperties("boletas")
    private Boleta Boleta;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    @JsonIgnoreProperties("pagos")
    private MetodoPago metodoPago;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private LocalDate fecha;
}
