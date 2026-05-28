package com.example.Ventas.service;


import com.example.Ventas.model.Pago;
import com.example.Ventas.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public Pago savePago (Pago pago){return pagoRepository.save(pago);}

    public List<Pago> obtenerPagos(){return pagoRepository.findAll();}

    public Optional<Pago> obtenerPorId(Integer id){return pagoRepository.findById(id);}

}
