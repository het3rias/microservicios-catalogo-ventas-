package com.example.Ventas.controller;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ventas")
@Tag(name = "Ventas API", description = "API para gestionar ventas, incluyendo boletas, detalles de boleta, pagos y métodos de pago.")
public class VentasController {

    @Autowired
    private BoletaService boletaService;

    @Autowired
    private DetalleBoletaService detalleBoletaService;

    @Autowired
    private PagoService pagoService;

    @Autowired
    private MetodoPagoService metodoPagoService;

    @PostMapping("/boletas")
    @Operation(summary = "Crear una nueva boleta", description = "Permite crear una nueva boleta en el sistema. Se deben proporcionar todos los campos requeridos.")
    public ResponseEntity<Boleta> newBoleta(@Valid @RequestBody Boleta boleta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boletaService.guardar(boleta));
    }

    @PostMapping("/detalleBoletas")
    @Operation(summary = "Crear un nuevo detalle de boleta", description = "Permite crear un nuevo detalle de boleta en el sistema. Se deben proporcionar todos los campos requeridos.")
    public ResponseEntity<DetalleBoleta> newDetalle(@Valid @RequestBody DetalleBoleta detalleBoleta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleBoletaService.guardarDetalleBoleta(detalleBoleta));
    }

    @PostMapping("/pagos")
    @Operation(summary = "Crear un nuevo pago", description = "Permite crear un nuevo pago en el sistema. Se deben proporcionar todos los campos requeridos.")
    public ResponseEntity<Pago> newPago(@Valid @RequestBody Pago pago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.savePago(pago));
    }

    @PostMapping("/metodoPagos")
    @Operation(summary = "Crear un nuevo método de pago", description = "Permite crear un nuevo método de pago en el sistema. Se deben proporcionar todos los campos requeridos.")
    public ResponseEntity<MetodoPago> createMetodoPago(@Valid @RequestBody MetodoPago metodoPago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoPagoService.saveMetodoPago(metodoPago));
    }

    @GetMapping("/boletas")
    @Operation(summary = "Listar todas las boletas", description = "Permite obtener una lista de todas las boletas en el sistema.")
    public List<Boleta> listaBoletas() {return boletaService.listaBoletas();}

    @GetMapping("/detalleBoletas")
    @Operation(summary = "Listar todos los detalles de boleta", description = "Permite obtener una lista de todos los detalles de boleta en el sistema.")
    public ResponseEntity<List<DetalleBoleta>> listarDetalleBoletas() {
        return ResponseEntity.ok(detalleBoletaService.obtenerDetalleBoletas());
    }

    @GetMapping("/pagos")
    @Operation(summary = "Listar todos los pagos", description = "Permite obtener una lista de todos los pagos en el sistema.")
    public ResponseEntity<List<Pago>> listarPagos() {
        return ResponseEntity.ok(pagoService.obtenerPagos());
    }

    @GetMapping("/metodoPagos")
    @Operation(summary = "Listar todos los métodos de pago", description = "Permite obtener una lisa de todos los método de pago en el sistema.")
    public ResponseEntity<List<MetodoPago>> obtenerMetodoPagos() {
        return ResponseEntity.ok(metodoPagoService.obtenerMetodoPago());
    }

    @GetMapping("/boletas/{id}")
    @Operation(summary = "Obtener una boleta por su ID", description = "Permite obtener los detalles de una boleta específica por su ID.")
    public ResponseEntity<Boleta> obtenerBoletaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(boletaService.obtenerPorId(id));
    }

    @GetMapping("/detalleBoletas/{id}")
    @Operation(summary = "Obtener un detalle de boleta por su ID", description = "Permite obtener los detalles de un detalle de boleta específico por su ID.")
    public ResponseEntity<DetalleBoleta> obtenerPorDetalleBoleta(@PathVariable Integer id) {
        return ResponseEntity.ok(detalleBoletaService.listarPorDetalleBoleta(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle con id " + id + " no encontrado")));
    }

    @GetMapping("/pagos/{id}")
    @Operation(summary = "Obtener un pago por su ID", description = "Permite obtener los detalles de un pago específico por su ID.")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con id " + id + " no encontrado")));
    }

    @GetMapping("/metodoPagos/{id}")
    @Operation(summary = "Obtener un método de pago por su ID", description = "Permite obtener los detalles de un método de pago específico por su ID.")
    public ResponseEntity<MetodoPago> buscarPorIdMetodoPago(@PathVariable Integer id) {
        return ResponseEntity.ok(metodoPagoService.obtenerPorIdMetodoPago(id)
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago con id " + id + " no encontrado")));
    }

    // Filtros de Boleta

    @GetMapping("/boletas/fecha")
    @Operation(summary = "Obtener boletas por fecha", description = "Permite obtener boletas filtradas por una fecha específica.")
    public List<Boleta> boletasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return boletaService.filtrarPorFecha(fecha);
    }

    @GetMapping("/boletas/fecha/rango")
    @Operation(summary = "Obtener boletas por rango de fechas", description = "Permite obtener boletas filtradas por un rango de fechas.")
    public List<Boleta> boletasPorRangoFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return boletaService.filtrarPorRangoFecha(desde, hasta);
    }

    @GetMapping("/boletas/cliente/{idCliente}")
    @Operation(summary = "Obtener boletas por cliente", description = "Permite obtener boletas filtradas por un cliente específico.")
    public List<Boleta> boletasPorCliente(@PathVariable Integer idCliente) {
        return boletaService.filtrarPorCliente(idCliente);
    }

    @GetMapping("/boletas/valor/asc")
    @Operation(summary = "Obtener boletas más baratas", description = "Permite obtener una lista de boletas ordenadas por valor ascendente.")
    public List<Boleta> boletasMasBaratas() {
        return boletaService.boletasMasBaratas();
    }

    @GetMapping("/boletas/valor/desc")
    @Operation(summary = "Obtener boletas más caras", description = "Permite obtener una lista de boletas ordenadas por valor descendente.")
    public List<Boleta> boletasMasCaras() {
        return boletaService.boletasMasCaras();
    }


}
