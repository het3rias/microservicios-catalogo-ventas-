package com.example.Ventas.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Ventas.assemblers.BoletaModelAssembler;
import com.example.Ventas.assemblers.DetalleBoletaModelAssembler;
import com.example.Ventas.assemblers.MetodoPagoModelAssembler;
import com.example.Ventas.assemblers.PagoModelAssembler;
import com.example.Ventas.exception.ResourceNotFoundException;
import com.example.Ventas.model.Boleta;
import com.example.Ventas.model.DetalleBoleta;
import com.example.Ventas.model.MetodoPago;
import com.example.Ventas.model.Pago;
import com.example.Ventas.service.BoletaService;
import com.example.Ventas.service.DetalleBoletaService;
import com.example.Ventas.service.MetodoPagoService;
import com.example.Ventas.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/ventas")
@Tag(name = "Ventas API V2", description = "Endpoints HATEOAS para gestionar ventas, boletas, detalles, pagos y métodos de pago.")
public class VentasControllerV2 {

    @Autowired
    private BoletaService boletaService;

    @Autowired
    private DetalleBoletaService detalleBoletaService;

    @Autowired
    private PagoService pagoService;

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Autowired
    private BoletaModelAssembler boletaAssembler;

    @Autowired
    private DetalleBoletaModelAssembler detalleBoletaAssembler;

    @Autowired
    private PagoModelAssembler pagoAssembler;

    @Autowired
    private MetodoPagoModelAssembler metodoPagoAssembler;

    // ─── BOLETAS ──────────────────────────────────────────────────────────────

    @GetMapping(value = "/boletas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todas las boletas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Boleta.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron boletas")
    })
    public CollectionModel<EntityModel<Boleta>> getAllBoletas() {
        List<EntityModel<Boleta>> boletas = boletaService.listaBoletas().stream()
                .map(boletaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(boletas,
                linkTo(methodOn(VentasControllerV2.class).getAllBoletas()).withSelfRel());
    }

    @GetMapping(value = "/boletas/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener una boleta por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boleta obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Boleta.class))),
        @ApiResponse(responseCode = "404", description = "Boleta no encontrada")
    })
    public EntityModel<Boleta> getBoletaById(@PathVariable Integer id) {
        return boletaAssembler.toModel(boletaService.obtenerPorId(id));
    }

    @PostMapping(value = "/boletas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear una nueva boleta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Boleta creada exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Boleta.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Boleta>> createBoleta(@Valid @RequestBody Boleta boleta) {
        Boleta nueva = boletaService.guardar(boleta);
        return ResponseEntity
                .created(linkTo(methodOn(VentasControllerV2.class).getBoletaById(nueva.getId())).toUri())
                .body(boletaAssembler.toModel(nueva));
    }

    // Filtros de Boleta

    @GetMapping(value = "/boletas/fecha", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener boletas por fecha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Boleta.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron boletas")
    })
    public CollectionModel<EntityModel<Boleta>> boletasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<EntityModel<Boleta>> boletas = boletaService.filtrarPorFecha(fecha).stream()
                .map(boletaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(boletas,
                linkTo(methodOn(VentasControllerV2.class).boletasPorFecha(fecha)).withSelfRel());
    }

    @GetMapping(value = "/boletas/fecha/rango", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener boletas por rango de fechas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Boleta.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron boletas")
    })
    public CollectionModel<EntityModel<Boleta>> boletasPorRangoFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        List<EntityModel<Boleta>> boletas = boletaService.filtrarPorRangoFecha(desde, hasta).stream()
                .map(boletaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(boletas,
                linkTo(methodOn(VentasControllerV2.class).boletasPorRangoFecha(desde, hasta)).withSelfRel());
    }

    @GetMapping(value = "/boletas/cliente/{idCliente}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener boletas por cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Boleta.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron boletas")
    })
    public CollectionModel<EntityModel<Boleta>> boletasPorCliente(@PathVariable Integer idCliente) {
        List<EntityModel<Boleta>> boletas = boletaService.filtrarPorCliente(idCliente).stream()
                .map(boletaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(boletas,
                linkTo(methodOn(VentasControllerV2.class).boletasPorCliente(idCliente)).withSelfRel());
    }

    @GetMapping(value = "/boletas/valor/asc", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener boletas más baratas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Boleta.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron boletas")
    })
    public CollectionModel<EntityModel<Boleta>> boletasMasBaratas() {
        List<EntityModel<Boleta>> boletas = boletaService.boletasMasBaratas().stream()
                .map(boletaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(boletas,
                linkTo(methodOn(VentasControllerV2.class).boletasMasBaratas()).withSelfRel());
    }

    @GetMapping(value = "/boletas/valor/desc", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener boletas más caras")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Boleta.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron boletas")
    })
    public CollectionModel<EntityModel<Boleta>> boletasMasCaras() {
        List<EntityModel<Boleta>> boletas = boletaService.boletasMasCaras().stream()
                .map(boletaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(boletas,
                linkTo(methodOn(VentasControllerV2.class).boletasMasCaras()).withSelfRel());
    }

    // ─── DETALLE BOLETAS ──────────────────────────────────────────────────────

    @GetMapping(value = "/detalleBoletas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todos los detalles de boleta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = DetalleBoleta.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron detalles")
    })
    public CollectionModel<EntityModel<DetalleBoleta>> getAllDetalleBoletas() {
        List<EntityModel<DetalleBoleta>> detalles = detalleBoletaService.obtenerDetalleBoletas().stream()
                .map(detalleBoletaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(detalles,
                linkTo(methodOn(VentasControllerV2.class).getAllDetalleBoletas()).withSelfRel());
    }

    @GetMapping(value = "/detalleBoletas/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un detalle de boleta por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle obtenido exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = DetalleBoleta.class))),
        @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    public EntityModel<DetalleBoleta> getDetalleBoletaById(@PathVariable Integer id) {
        return detalleBoletaAssembler.toModel(detalleBoletaService.listarPorDetalleBoleta(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle con id " + id + " no encontrado")));
    }

    @PostMapping(value = "/detalleBoletas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo detalle de boleta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Detalle creado exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = DetalleBoleta.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<DetalleBoleta>> createDetalleBoleta(@Valid @RequestBody DetalleBoleta detalleBoleta) {
        DetalleBoleta nuevo = detalleBoletaService.guardarDetalleBoleta(detalleBoleta);
        return ResponseEntity
                .created(linkTo(methodOn(VentasControllerV2.class).getDetalleBoletaById(nuevo.getId())).toUri())
                .body(detalleBoletaAssembler.toModel(nuevo));
    }

    // ─── PAGOS ────────────────────────────────────────────────────────────────

    @GetMapping(value = "/pagos", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todos los pagos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron pagos")
    })
    public CollectionModel<EntityModel<Pago>> getAllPagos() {
        List<EntityModel<Pago>> pagos = pagoService.obtenerPagos().stream()
                .map(pagoAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pagos,
                linkTo(methodOn(VentasControllerV2.class).getAllPagos()).withSelfRel());
    }

    @GetMapping(value = "/pagos/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un pago por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago obtenido exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public EntityModel<Pago> getPagoById(@PathVariable Integer id) {
        return pagoAssembler.toModel(pagoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con id " + id + " no encontrado")));
    }

    @PostMapping(value = "/pagos", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo pago")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pago creado exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<Pago>> createPago(@Valid @RequestBody Pago pago) {
        Pago nuevo = pagoService.savePago(pago);
        return ResponseEntity
                .created(linkTo(methodOn(VentasControllerV2.class).getPagoById(nuevo.getId())).toUri())
                .body(pagoAssembler.toModel(nuevo));
    }

    // ─── MÉTODOS DE PAGO ──────────────────────────────────────────────────────

    @GetMapping(value = "/metodoPagos", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todos los métodos de pago")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = MetodoPago.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron métodos de pago")
    })
    public CollectionModel<EntityModel<MetodoPago>> getAllMetodoPagos() {
        List<EntityModel<MetodoPago>> metodos = metodoPagoService.obtenerMetodoPago().stream()
                .map(metodoPagoAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(metodos,
                linkTo(methodOn(VentasControllerV2.class).getAllMetodoPagos()).withSelfRel());
    }

    @GetMapping(value = "/metodoPagos/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un método de pago por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Método de pago obtenido exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = MetodoPago.class))),
        @ApiResponse(responseCode = "404", description = "Método de pago no encontrado")
    })
    public EntityModel<MetodoPago> getMetodoPagoById(@PathVariable Integer id) {
        return metodoPagoAssembler.toModel(metodoPagoService.obtenerPorIdMetodoPago(id)
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago con id " + id + " no encontrado")));
    }

    @PostMapping(value = "/metodoPagos", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo método de pago")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Método de pago creado exitosamente",
            content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = MetodoPago.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<EntityModel<MetodoPago>> createMetodoPago(@Valid @RequestBody MetodoPago metodoPago) {
        MetodoPago nuevo = metodoPagoService.saveMetodoPago(metodoPago);
        return ResponseEntity
                .created(linkTo(methodOn(VentasControllerV2.class).getMetodoPagoById(nuevo.getId())).toUri())
                .body(metodoPagoAssembler.toModel(nuevo));
    }
}