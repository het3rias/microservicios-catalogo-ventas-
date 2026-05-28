package com.example.Catalogo.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "autor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del autor no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Column(nullable = false)
    private String nombre;


    @NotBlank(message = "El apellido del autor no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede superar 100 caracteres")
    @Column(nullable = false)
    private String apellido;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnoreProperties("autor")
    private List<Libro> libros;
}
