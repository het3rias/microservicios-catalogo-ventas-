package com.example.Catalogo.controller;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import com.example.Catalogo.assemblers.AutorModelAssembler;
import com.example.Catalogo.assemblers.CategoriaModelAssembler;
import com.example.Catalogo.assemblers.EditorialModelAssembler;
import com.example.Catalogo.assemblers.LibroModelAssembler;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
 
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
 
@RestController
@RequestMapping("/api/v2/catalogo")
@Tag(name = "Catálogo de Libros V2", description = "Endpoints HATEOAS para gestionar el catálogo de libros, autores, editoriales y categorías")
public class CatalogoControllerV2 {
 
    @Autowired
    private LibroService libroService;
 
    @Autowired
    private EditorialService editorialService;
 
    @Autowired
    private CategoriaService categoriaService;
 
    @Autowired
    private AutorService autorService;
 
    @Autowired
    private LibroModelAssembler libroAssembler;
 
    @Autowired
    private AutorModelAssembler autorAssembler;
 
    @Autowired
    private EditorialModelAssembler editorialAssembler;
 
    @Autowired
    private CategoriaModelAssembler categoriaAssembler;
 
    // ─── LIBROS ───────────────────────────────────────────────────────────────
 
    @GetMapping(value = "/libros", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todos los libros", description = "Retorna todos los libros con enlaces HATEOAS.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> getAllLibros() {
        List<EntityModel<Libro>> libros = libroService.findAll().stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).getAllLibros()).withSelfRel());
    }
 
    @GetMapping(value = "/libros/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un libro por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro obtenido exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public EntityModel<Libro> getLibroById(@PathVariable int id) {
        return libroAssembler.toModel(libroService.findById(id));
    }
 
    @PostMapping(value = "/libros", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo libro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Libro creado exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Libro>> createLibro(@Valid @RequestBody Libro libro) {
        Libro nuevo = libroService.guardarLibro(libro);
        return ResponseEntity
                .created(linkTo(methodOn(CatalogoControllerV2.class).getLibroById(nuevo.getId())).toUri())
                .body(libroAssembler.toModel(nuevo));
    }
 
    @DeleteMapping(value = "/libros/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un libro por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<?> deleteLibro(@PathVariable int id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
 
    // ─── AUTORES ──────────────────────────────────────────────────────────────
 
    @GetMapping(value = "/autor", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todos los autores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Autor.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron autores")
    })
    public CollectionModel<EntityModel<Autor>> getAllAutores() {
        List<EntityModel<Autor>> autores = autorService.findAll().stream()
                .map(autorAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(autores,
                linkTo(methodOn(CatalogoControllerV2.class).getAllAutores()).withSelfRel());
    }
 
    @GetMapping(value = "/autor/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un autor por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autor obtenido exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Autor.class))),
        @ApiResponse(responseCode = "404", description = "Autor no encontrado")
    })
    public EntityModel<Autor> getAutorById(@PathVariable int id) {
        return autorAssembler.toModel(autorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor con id " + id + " no encontrado")));
    }
 
    @PostMapping(value = "/autor", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo autor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Autor creado exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Autor.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Autor>> createAutor(@Valid @RequestBody Autor autor) {
        Autor nuevo = autorService.guardarAutor(autor);
        return ResponseEntity
                .created(linkTo(methodOn(CatalogoControllerV2.class).getAutorById(nuevo.getId())).toUri())
                .body(autorAssembler.toModel(nuevo));
    }
 
    // ─── EDITORIALES ──────────────────────────────────────────────────────────
 
    @GetMapping(value = "/editorial", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todas las editoriales")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Editorial.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron editoriales")
    })
    public CollectionModel<EntityModel<Editorial>> getAllEditoriales() {
        List<EntityModel<Editorial>> editoriales = editorialService.findAll().stream()
                .map(editorialAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(editoriales,
                linkTo(methodOn(CatalogoControllerV2.class).getAllEditoriales()).withSelfRel());
    }
 
    @GetMapping(value = "/editorial/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener una editorial por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Editorial obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Editorial.class))),
        @ApiResponse(responseCode = "404", description = "Editorial no encontrada")
    })
    public EntityModel<Editorial> getEditorialById(@PathVariable int id) {
        return editorialAssembler.toModel(editorialService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editorial con id " + id + " no encontrada")));
    }
 
    @PostMapping(value = "/editorial", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear una nueva editorial")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Editorial creada exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Editorial.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Editorial>> createEditorial(@Valid @RequestBody Editorial editorial) {
        Editorial nueva = editorialService.guardarEditorial(editorial);
        return ResponseEntity
                .created(linkTo(methodOn(CatalogoControllerV2.class).getEditorialById(nueva.getId())).toUri())
                .body(editorialAssembler.toModel(nueva));
    }
 
    // ─── CATEGORÍAS ───────────────────────────────────────────────────────────
 
    @GetMapping(value = "/categorias", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todas las categorías")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron categorías")
    })
    public CollectionModel<EntityModel<Categoria>> getAllCategorias() {
        List<EntityModel<Categoria>> categorias = categoriaService.findAll().stream()
                .map(categoriaAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(categorias,
                linkTo(methodOn(CatalogoControllerV2.class).getAllCategorias()).withSelfRel());
    }
 
    @GetMapping(value = "/categorias/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener una categoría por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public EntityModel<Categoria> getCategoriaById(@PathVariable int id) {
        return categoriaAssembler.toModel(categoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con id " + id + " no encontrada")));
    }
 
    @PostMapping(value = "/categorias", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear una nueva categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Categoria.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Categoria>> createCategoria(@Valid @RequestBody Categoria categoria) {
        Categoria nueva = categoriaService.guardarCategoria(categoria);
        return ResponseEntity
                .created(linkTo(methodOn(CatalogoControllerV2.class).getCategoriaById(nueva.getId())).toUri())
                .body(categoriaAssembler.toModel(nueva));
    }
 
    // ─── FILTROS DE LIBRO ─────────────────────────────────────────────────────
 
    @GetMapping(value = "/libros/categoria/{idCategoria}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros por categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> librosPorCategoria(@PathVariable Integer idCategoria) {
        List<EntityModel<Libro>> libros = libroService.filtrarPorCategoria(idCategoria).stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).librosPorCategoria(idCategoria)).withSelfRel());
    }
 
    @GetMapping(value = "/libros/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar libros por título")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> librosPorTitulo(@RequestParam String titulo) {
        List<EntityModel<Libro>> libros = libroService.filtrarPorTitulo(titulo).stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).librosPorTitulo(titulo)).withSelfRel());
    }
 
    @GetMapping(value = "/libros/autor", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros por autor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> librosPorAutor(@RequestParam String autor) {
        List<EntityModel<Libro>> libros = libroService.filtrarPorAutor(autor).stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).librosPorAutor(autor)).withSelfRel());
    }
 
    @GetMapping(value = "/libros/editorial", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros por editorial")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> librosPorEditorial(@RequestParam String editorial) {
        List<EntityModel<Libro>> libros = libroService.filtrarPorEditorial(editorial).stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).librosPorEditorial(editorial)).withSelfRel());
    }
 
    @GetMapping(value = "/libros/disponibles", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros por disponibilidad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> librosPorDisponibilidad(@RequestParam Boolean disponible) {
        List<EntityModel<Libro>> libros = libroService.filtrarPorDisponibilidad(disponible).stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).librosPorDisponibilidad(disponible)).withSelfRel());
    }
 
    @GetMapping(value = "/libros/precio", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros por rango de precio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> librosPorRangoPrecio(@RequestParam Double min, @RequestParam Double max) {
        List<EntityModel<Libro>> libros = libroService.filtrarPorRangoPrecio(min, max).stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).librosPorRangoPrecio(min, max)).withSelfRel());
    }
 
    @GetMapping(value = "/libros/precio/asc", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros más baratos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> librosMasBaratos() {
        List<EntityModel<Libro>> libros = libroService.librosMasBaratos().stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).librosMasBaratos()).withSelfRel());
    }
 
    @GetMapping(value = "/libros/precio/desc", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros más caros")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> librosMasCaros() {
        List<EntityModel<Libro>> libros = libroService.librosMasCaros().stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).librosMasCaros()).withSelfRel());
    }
 
    @GetMapping(value = "/libros/fecha", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros por rango de fecha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> librosPorRangoFecha(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta) {
        List<EntityModel<Libro>> libros = libroService.filtrarPorRangoFecha(desde, hasta).stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).librosPorRangoFecha(desde, hasta)).withSelfRel());
    }
 
    @GetMapping(value = "/libros/filtros", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros con filtros combinados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Libro.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron libros")
    })
    public CollectionModel<EntityModel<Libro>> librosFiltrosCombinados(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String editorial,
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(required = false) Boolean disponible) {
        List<EntityModel<Libro>> libros = libroService.buscarConFiltros(titulo, autor, editorial, idCategoria, disponible).stream()
                .map(libroAssembler::toModel)
                .collect(Collectors.toList());
 
        return CollectionModel.of(libros,
                linkTo(methodOn(CatalogoControllerV2.class).librosFiltrosCombinados(titulo, autor, editorial, idCategoria, disponible)).withSelfRel());
    }
}