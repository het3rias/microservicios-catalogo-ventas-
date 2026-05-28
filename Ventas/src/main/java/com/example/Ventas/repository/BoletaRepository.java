package com.example.Ventas.repository;

import com.example.Ventas.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Integer> {


    List<Boleta> findByFecha(LocalDate fecha);

    List<Boleta> findByFechaBetween(LocalDate desde, LocalDate hasta);

    List<Boleta> findByIdCliente(Integer idCliente);

    List<Boleta> findAllByOrderByValorTotalAsc();

    List<Boleta> findAllByOrderByValorTotalDesc();

}
