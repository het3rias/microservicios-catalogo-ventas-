package com.example.Catalogo.controller;


import com.example.Catalogo.exception.ResourceNotFoundException;
import com.example.Catalogo.model.Autor;
import com.example.Catalogo.model.Categoria;
import com.example.Catalogo.model.Editorial;
import com.example.Catalogo.model.Libro;
import com.example.Catalogo.service.AutorService;
import com.example.Catalogo.service.CategoriaService;
import com.example.Catalogo.service.EditorialService;
import com.example.Catalogo.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogo")
public class CatalogoController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private EditorialService editorialService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AutorService autorService;

    @PostMapping("/libros")
    public ResponseEntity<Libro> saveLibro(@Valid @RequestBody Libro libro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.guardarLibro(libro));
    }

    @PostMapping("/autor")
    public ResponseEntity<Autor> saveAutor(@Valid @RequestBody Autor autor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.guardarAutor(autor));
    }

    @PostMapping("/editorial")
    public ResponseEntity<Editorial> saveEditorial(@Valid @RequestBody Editorial editorial) {
        return ResponseEntity.status(HttpStatus.CREATED).body(editorialService.guardarEditorial(editorial));
    }

    @PostMapping("/categorias")
    public ResponseEntity<Categoria> saveCategoria(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.guardarCategoria(categoria));
    }

    @GetMapping("/libros")
    public List<Libro> findAllLibros() {return libroService.findAll();}

    @GetMapping("/editorial")
    public List<Editorial> findAllEditorials() {return editorialService.findAll();}

    @GetMapping("/autor")
    public List<Autor> findAllAutores() {return autorService.findAll();}

    @GetMapping("/categorias")
    public List<Categoria> findAllCategorias() {return categoriaService.findAll();}

    @GetMapping("/libros/{id}")
    public ResponseEntity<Libro> findLibroById(@PathVariable int id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    @GetMapping("/editorial/{id}")
    public ResponseEntity<Editorial> findEditorialById(@PathVariable int id) {
        return ResponseEntity.ok(editorialService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editorial con id " + id + " no encontrada")));
    }

    @GetMapping("/autor/{id}")
    public ResponseEntity<Autor> findAutorById(@PathVariable int id) {
        return ResponseEntity.ok(autorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor con id " + id + " no encontrado")));
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> findCategoriaById(@PathVariable int id) {
        return ResponseEntity.ok(categoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con id " + id + " no encontrada")));
    }

    //  Filtros de Libro

    @GetMapping("/libros/categoria/{idCategoria}")
    public List<Libro> librosPorCategoria(@PathVariable Integer idCategoria) {
        return libroService.filtrarPorCategoria(idCategoria);
    }

    @GetMapping("/libros/buscar")
    public List<Libro> librosPorTitulo(@RequestParam String titulo) {
        return libroService.filtrarPorTitulo(titulo);
    }

    @GetMapping("/libros/autor")
    public List<Libro> librosPorAutor(@RequestParam String autor) {
        return libroService.filtrarPorAutor(autor);
    }

    @GetMapping("/libros/editorial")
    public List<Libro> librosPorEditorial(@RequestParam String editorial) {
        return libroService.filtrarPorEditorial(editorial);
    }

    @GetMapping("/libros/disponibles")
    public List<Libro> librosPorDisponibilidad(@RequestParam Boolean disponible) {
        return libroService.filtrarPorDisponibilidad(disponible);
    }

    @GetMapping("/libros/precio")
    public List<Libro> librosPorRangoPrecio(@RequestParam Double min, @RequestParam Double max) {
        return libroService.filtrarPorRangoPrecio(min, max);
    }

    @GetMapping("/libros/precio/asc")
    public List<Libro> librosMasBaratos() {
        return libroService.librosMasBaratos();
    }

    @GetMapping("/libros/precio/desc")
    public List<Libro> librosMasCaros() {
        return libroService.librosMasCaros();
    }

    @GetMapping("/libros/fecha")
    public List<Libro> librosPorRangoFecha(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta) {
        return libroService.filtrarPorRangoFecha(desde, hasta);
    }

    @GetMapping("/libros/filtros")
    public List<Libro> librosFiltrosCombinados(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String editorial,
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(required = false) Boolean disponible) {
        return libroService.buscarConFiltros(titulo, autor, editorial, idCategoria, disponible);
    }


}
