package com.example.Ventas.controller;


import com.example.Ventas.exception.ResourceNotFoundException;
import com.example.Ventas.model.Boleta;
import com.example.Ventas.model.DetalleBoleta;
import com.example.Ventas.model.MetodoPago;
import com.example.Ventas.model.Pago;
import com.example.Ventas.service.BoletaService;
import com.example.Ventas.service.DetalleBoletaService;
import com.example.Ventas.service.MetodoPagoService;
import com.example.Ventas.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
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
    public ResponseEntity<Boleta> newBoleta(@Valid @RequestBody Boleta boleta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boletaService.guardar(boleta));
    }

    @PostMapping("/detalleBoletas")
    public ResponseEntity<DetalleBoleta> newDetalle(@Valid @RequestBody DetalleBoleta detalleBoleta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleBoletaService.guardarDetalleBoleta(detalleBoleta));
    }

    @PostMapping("/pagos")
    public ResponseEntity<Pago> newPago(@Valid @RequestBody Pago pago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.savePago(pago));
    }

    @PostMapping("/metodoPagos")
    public ResponseEntity<MetodoPago> createMetodoPago(@Valid @RequestBody MetodoPago metodoPago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoPagoService.saveMetodoPago(metodoPago));
    }

    @GetMapping("/boletas")
    public List<Boleta> listaBoletas() {return boletaService.listaBoletas();}

    @GetMapping("/detalleBoletas")
    public ResponseEntity<List<DetalleBoleta>> listarDetalleBoletas() {
        return ResponseEntity.ok(detalleBoletaService.obtenerDetalleBoletas());
    }

    @GetMapping("/pagos")
    public ResponseEntity<List<Pago>> listarPagos() {
        return ResponseEntity.ok(pagoService.obtenerPagos());
    }

    @GetMapping("/metodoPagos")
    public ResponseEntity<List<MetodoPago>> obtenerMetodoPagos() {
        return ResponseEntity.ok(metodoPagoService.obtenerMetodoPago());
    }

    @GetMapping("/boletas/{id}")
    public ResponseEntity<Boleta> obtenerBoletaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(boletaService.obtenerPorId(id));
    }

    @GetMapping("/detalleBoletas/{id}")
    public ResponseEntity<DetalleBoleta> obtenerPorDetalleBoleta(@PathVariable Integer id) {
        return ResponseEntity.ok(detalleBoletaService.listarPorDetalleBoleta(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle con id " + id + " no encontrado")));
    }

    @GetMapping("/pagos/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con id " + id + " no encontrado")));
    }

    @GetMapping("/metodoPagos/{id}")
    public ResponseEntity<MetodoPago> buscarPorIdMetodoPago(@PathVariable Integer id) {
        return ResponseEntity.ok(metodoPagoService.obtenerPorIdMetodoPago(id)
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago con id " + id + " no encontrado")));
    }

    // Filtros de Boleta

    @GetMapping("/boletas/fecha")
    public List<Boleta> boletasPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return boletaService.filtrarPorFecha(fecha);
    }

    @GetMapping("/boletas/fecha/rango")
    public List<Boleta> boletasPorRangoFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return boletaService.filtrarPorRangoFecha(desde, hasta);
    }

    @GetMapping("/boletas/cliente/{idCliente}")
    public List<Boleta> boletasPorCliente(@PathVariable Integer idCliente) {
        return boletaService.filtrarPorCliente(idCliente);
    }

    @GetMapping("/boletas/valor/asc")
    public List<Boleta> boletasMasBaratas() {
        return boletaService.boletasMasBaratas();
    }

    @GetMapping("/boletas/valor/desc")
    public List<Boleta> boletasMasCaras() {
        return boletaService.boletasMasCaras();
    }


}
