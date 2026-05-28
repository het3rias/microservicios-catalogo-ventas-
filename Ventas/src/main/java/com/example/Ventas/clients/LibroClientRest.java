package com.example.Ventas.clients;


import com.example.Ventas.dto.LibroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name= "Catalogo", url="http://localhost:8082")
public interface LibroClientRest {
    @GetMapping("/api/v1/catalogo/libros/{id}")
    LibroDTO getLibroById(@PathVariable("id") Integer id);

}
