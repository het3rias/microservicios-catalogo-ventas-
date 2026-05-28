package com.example.Catalogo.service;

import com.example.Catalogo.model.Categoria;
import com.example.Catalogo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria guardarCategoria(Categoria categoria) {return categoriaRepository.save(categoria);}

    public List<Categoria> findAll() {return categoriaRepository.findAll();}

    public Optional<Categoria> findById(Integer id) {return categoriaRepository.findById(id);}
}
