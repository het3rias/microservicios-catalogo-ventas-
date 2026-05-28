package com.example.Ventas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="metodo_pago")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPago {
@Id
@GeneratedValue(strategy= GenerationType.IDENTITY)
private Integer id;

    @NotBlank(message = "El nombre del método de pago no puede estar vacío")
    @Column(nullable = false)
    private String nombre;
}
