package com.example.Ventas.service;

import com.example.Ventas.clients.LibroClientRest;
import com.example.Ventas.dto.LibroDTO;
import com.example.Ventas.model.DetalleBoleta;
import com.example.Ventas.repository.DetalleBoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleBoletaService {

    @Autowired
    private DetalleBoletaRepository detalleBoletaRepository;
    @Autowired
    private LibroClientRest catalogoClient;

    public DetalleBoleta guardarDetalleBoleta(DetalleBoleta detalleBoleta){
        LibroDTO libro = catalogoClient.getLibroById(detalleBoleta.getIdLibro());
        detalleBoleta.setPrecioUnitario(libro.getPrecio());

        return detalleBoletaRepository.save(detalleBoleta);
    }

    public List<DetalleBoleta> obtenerDetalleBoletas(){return detalleBoletaRepository.findAll();}

    public Optional<DetalleBoleta> listarPorDetalleBoleta(Integer id){
        return detalleBoletaRepository.findById(id);
    }

}
