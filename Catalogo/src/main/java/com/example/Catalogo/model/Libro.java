package com.example.Catalogo.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "libro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 1, max = 200, message = "El título debe tener entre 1 y 200 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotNull(message = "La fecha de publicación es obligatoria")
    @Column(nullable = false)
    private Date anioPublicacion;

    @NotNull(message = "La disponibilidad es obligatoria")
    @Column(nullable = false)
    private Boolean disponible;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    @Column(nullable = false)
    private Double precio;

    @NotNull(message = "El autor es obligatorio")
    @ManyToOne
    @JoinColumn(name = "id_autor", nullable = false)
    @JsonIgnoreProperties("libros")
    private Autor autor;

    @NotNull(message = "La editorial es obligatoria")
    @ManyToOne
    @JoinColumn(name = "id_editorial", nullable = false)
    @JsonIgnoreProperties("libros")
    private Editorial editorial;

    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    @JsonIgnoreProperties("libros")
    private Categoria categoria;
}
