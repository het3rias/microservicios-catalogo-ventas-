package com.example.Ventas.service;


import com.example.Ventas.exception.BadRequestException;
import com.example.Ventas.exception.ResourceNotFoundException;
import com.example.Ventas.model.Boleta;
import com.example.Ventas.repository.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    public Boleta guardar(Boleta boleta) {
        if (boleta.getFecha() == null) boleta.setFecha(LocalDate.now());
        return boletaRepository.save(boleta);
    }

    public List<Boleta> listaBoletas() {
        return boletaRepository.findAll();
    }

    public Boleta obtenerPorId(Integer id) {
        return boletaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boleta con id " + id + " no encontrada"));
    }


    // ── Filtros ──────────────────────────────────────

    public List<Boleta> filtrarPorFecha(LocalDate fecha) {
        if (fecha == null) throw new BadRequestException("La fecha no puede ser nula");
        List<Boleta> resultado = boletaRepository.findByFecha(fecha);
        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No hay boletas para la fecha: " + fecha);
        return resultado;
    }

    public List<Boleta> filtrarPorRangoFecha(LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta))
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha fin");
        List<Boleta> resultado = boletaRepository.findByFechaBetween(desde, hasta);
        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No hay boletas en ese rango de fechas");
        return resultado;
    }

    public List<Boleta> filtrarPorCliente(Integer idCliente) {
        if (idCliente <= 0) throw new BadRequestException("El id del cliente debe ser mayor a 0");
        List<Boleta> resultado = boletaRepository.findByIdCliente(idCliente);
        if (resultado.isEmpty())
            throw new ResourceNotFoundException("No hay boletas para el cliente: " + idCliente);
        return resultado;
    }

    public List<Boleta> boletasMasBaratas() { return boletaRepository.findAllByOrderByValorTotalAsc(); }
    public List<Boleta> boletasMasCaras()   { return boletaRepository.findAllByOrderByValorTotalDesc(); }
}
