package com.example.Ventas.dto;

import lombok.Data;

@Data
public class LibroDTO {
    private Integer id;
    private String titulo;
    private Double precio;
    private Boolean disponible;
}
