package com.example.Ventas.service;


import com.example.Ventas.model.MetodoPago;
import com.example.Ventas.repository.MetodoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    public MetodoPago saveMetodoPago (MetodoPago metodoPago){return metodoPagoRepository.save(metodoPago);}


    public List<MetodoPago> obtenerMetodoPago (){return metodoPagoRepository.findAll();}

    public Optional<MetodoPago> obtenerPorIdMetodoPago(Integer id){return metodoPagoRepository.findById(id);}
}
