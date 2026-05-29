package com.example.Catalogo.controller;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Catalogo.exception.ResourceNotFoundException;
import com.example.Catalogo.model.Autor;
import com.example.Catalogo.model.Categoria;
import com.example.Catalogo.model.Editorial;
import com.example.Catalogo.model.Libro;
import com.example.Catalogo.service.AutorService;
import com.example.Catalogo.service.CategoriaService;
import com.example.Catalogo.service.EditorialService;
import com.example.Catalogo.service.LibroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/catalogo")
@Tag(name = "Catálogo de Libros", description = "Endpoints para gestionar el catálogo de libros, autores, editoriales y categorías")
public class CatalogoController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private EditorialService editorialService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AutorService autorService;

    @DeleteMapping("/libros/{id}")
    @Operation(summary = "Eliminar un libro por ID", description = "Permite eliminar un libro específico por su ID.")
    public ResponseEntity<Void> deleteLibro(@PathVariable int id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/libros")
    @Operation(summary = "Crear un nuevo libro", description = "Permite crear un nuevo libro en el catálogo. Se deben proporcionar todos los campos requeridos.")
    public ResponseEntity<Libro> saveLibro(@Valid @RequestBody Libro libro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.guardarLibro(libro));
    }

    @PostMapping("/autor")
    @Operation(summary = "Crear un nuevo autor", description = "Permite crear un nuevo autor en el catálogo. Se deben proporcionar todos los campos requeridos.")
    public ResponseEntity<Autor> saveAutor(@Valid @RequestBody Autor autor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.guardarAutor(autor));
    }

    @PostMapping("/editorial")
    @Operation(summary = "Crear una nueva editorial", description = "Permite crear una nueva editorial en el catálogo. Se deben proporcionar todos los campos requeridos.")
    public ResponseEntity<Editorial> saveEditorial(@Valid @RequestBody Editorial editorial) {
        return ResponseEntity.status(HttpStatus.CREATED).body(editorialService.guardarEditorial(editorial));
    }

    @PostMapping("/categorias")
    @Operation(summary = "Crear una nueva categoría", description = "Permite crear una nueva categoría en el catálogo. Se deben proporcionar todos los campos requeridos.")
    public ResponseEntity<Categoria> saveCategoria(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.guardarCategoria(categoria));
    }

    @GetMapping("/libros")
    @Operation(summary = "Listar todos los libros", description = "Permite obtener una lista de todos los libros en el catálogo.")
    public List<Libro> findAllLibros() {return libroService.findAll();}

    @GetMapping("/editorial")
    @Operation(summary = "Listar todas las editoriales", description = "Permite obtener una lista de todas las editoriales en el catálogo.")
    public List<Editorial> findAllEditorials() {return editorialService.findAll();}

    @GetMapping("/autor")
    @Operation(summary = "Listar todos los autores", description = "Permite obtener una lista de todos los autores en el catálogo.")
    public List<Autor> findAllAutores() {return autorService.findAll();}

    @GetMapping("/categorias")
    @Operation(summary = "Listar todas las categorías", description = "Permite obtener una lista de todas las categorías en el catálogo.")
    public List<Categoria> findAllCategorias() {return categoriaService.findAll();}

    @GetMapping("/libros/{id}")
    @Operation(summary = "Obtener un libro por ID", description = "Permite obtener un libro específico por su ID.")
    public ResponseEntity<Libro> findLibroById(@PathVariable int id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    @GetMapping("/editorial/{id}")
    @Operation(summary = "Obtener una editorial por ID", description = "Permite obtener una editorial específica por su ID.")
    public ResponseEntity<Editorial> findEditorialById(@PathVariable int id) {
        return ResponseEntity.ok(editorialService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editorial con id " + id + " no encontrada")));
    }

    @GetMapping("/autor/{id}")
    @Operation(summary = "Obtener un autor por ID", description = "Permite obtener un autor específico por su ID.")
    public ResponseEntity<Autor> findAutorById(@PathVariable int id) {
        return ResponseEntity.ok(autorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor con id " + id + " no encontrado")));
    }

    @GetMapping("/categorias/{id}")
    @Operation(summary = "Obtener una categoría por ID", description = "Permite obtener una categoría específica por su ID.")
    public ResponseEntity<Categoria> findCategoriaById(@PathVariable int id) {
        return ResponseEntity.ok(categoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con id " + id + " no encontrada")));
    }

    //  Filtros de Libro

    @GetMapping("/libros/categoria/{idCategoria}")
    @Operation(summary = "Obtener libros por categoría", description = "Permite obtener una lista de libros filtrados por su categoría.")
    public List<Libro> librosPorCategoria(@PathVariable Integer idCategoria) {
        return libroService.filtrarPorCategoria(idCategoria);
    }

    @GetMapping("/libros/buscar")
    @Operation(summary = "Buscar libros por título", description = "Permite obtener una lista de libros filtrados por su título.")
    public List<Libro> librosPorTitulo(@RequestParam String titulo) {
        return libroService.filtrarPorTitulo(titulo);
    }

    @GetMapping("/libros/autor")
    @Operation(summary = "Obtener libros por autor", description = "Permite obtener una lista de libros filtrados por su autor.")
    public List<Libro> librosPorAutor(@RequestParam String autor) {
        return libroService.filtrarPorAutor(autor);
    }

    @GetMapping("/libros/editorial")
    @Operation(summary = "Obtener libros por editorial", description = "Permite obtener una lista de libros filtrados por su editorial.")
    public List<Libro> librosPorEditorial(@RequestParam String editorial) {
        return libroService.filtrarPorEditorial(editorial);
    }

    @GetMapping("/libros/disponibles")
    @Operation(summary = "Obtener libros por disponibilidad", description = "Permite obtener una lista de libros filtrados por su disponibilidad.")
    public List<Libro> librosPorDisponibilidad(@RequestParam Boolean disponible) {
        return libroService.filtrarPorDisponibilidad(disponible);
    }

    @GetMapping("/libros/precio")
    @Operation(summary = "Obtener libros por rango de precio", description = "Permite obtener una lista de libros filtrados por un rango de precio.")
    public List<Libro> librosPorRangoPrecio(@RequestParam Double min, @RequestParam Double max) {
        return libroService.filtrarPorRangoPrecio(min, max);
    }

    @GetMapping("/libros/precio/asc")
    @Operation(summary = "Obtener libros más baratos", description = "Permite obtener una lista de libros ordenados por precio ascendente.")
    public List<Libro> librosMasBaratos() {
        return libroService.librosMasBaratos();
    }

    @GetMapping("/libros/precio/desc")
    @Operation(summary = "Obtener libros más caros", description = "Permite obtener una lista de libros ordenados por precio descendente.")
    public List<Libro> librosMasCaros() {
        return libroService.librosMasCaros();
    }

    @GetMapping("/libros/fecha")
    @Operation(summary = "Obtener libros por rango de fecha", description = "Permite obtener una lista de libros filtrados por un rango de fechas.")
    public List<Libro> librosPorRangoFecha(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta) {
        return libroService.filtrarPorRangoFecha(desde, hasta);
    }

    @GetMapping("/libros/filtros")
    @Operation(summary = "Obtener libros con filtros combinados", description = "Permite obtener una lista de libros filtrados por múltiples criterios.")
    public List<Libro> librosFiltrosCombinados(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String editorial,
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(required = false) Boolean disponible) {
        return libroService.buscarConFiltros(titulo, autor, editorial, idCategoria, disponible);
    }


}
